package v20170926.xulaAtmServerController;

import v20170926.xulaAtmModel.LoginResult;
import v20170926.xulaAtmModel.Result;
import v20170926.xulaAtmModel.XulaATM;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientHandler implements Runnable {

    private final int TIMEOUT = 5000;
    private final Socket SOCKET;
    private final InputStream IN;
    private final DataInputStream DATA_IN;
    private final DataOutputStream DATA_OUT;
    private static SessionList sessionList = new SessionList();
    private final int ACK_CODE = 10101010;

    private final XulaATM xulaATM;

    public ClientHandler(Socket socket, final XulaATM xulaATM) throws IOException {

        SOCKET = socket;
        IN = socket.getInputStream();
        DATA_IN = new DataInputStream(IN);
        DATA_OUT = new DataOutputStream(socket.getOutputStream());

        this.xulaATM = xulaATM;

    }


    @Override
    public void run() {

        try {

            //read sessionId
            long sessionId = readLongWTimeout();
            System.out.println("\nRead sessionId = " + sessionId);

            //Send ACK
            DATA_OUT.writeInt(ACK_CODE);
            System.out.println("Sent ACK");


            //check for new session
            if (sessionId <= -1) {

                System.out.println("\nNewSessionCMD Start");
                handleNewSession();
                System.out.println("NewSessionCMD End\n");

                SOCKET.close();

                return;
            }

            //check for valid session
            if (!sessionList.sessionExists(sessionId)){

                System.out.println("\nInvalidSessionCMD Start");

                handleInvalidSession();

                System.out.println("InvalidSessionCMD End\n");


                SOCKET.close();

                return;
            }


            SOCKET.close();

        } catch (SocketTimeoutException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void handleLoginCommand() throws IOException {
        int ack;

        System.out.println("\nLoginCMD Start");

        //Read userName Length
        int userNameLen = readIntWTimeout();
        System.out.println("\tRead userName Length");

        //Send ACK
        DATA_OUT.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        //Read userName Bytes
        byte[] userNameBytes = readBytesWTimeout(userNameLen);
        String userName = new String(userNameBytes);
        System.out.println("\tRead userName: " + userName);

        //Send ACK
        DATA_OUT.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        //Read password Length
        int passwordLen = readIntWTimeout();
        System.out.println("\tRead password Length");

        //Send ACK
        DATA_OUT.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        //Read password Bytes
        byte[] passwordBytes = readBytesWTimeout(passwordLen);
        String password = new String(passwordBytes);
        System.out.println("\tRead password: " + password);

        //Send ACK
        DATA_OUT.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

        LoginResult loginResult = xulaATM.login(userName, password);

        //Send LoginResult Status
        DATA_OUT.writeInt(loginResult.getStatus());
        System.out.println("\tSent LoginResult Status Length");

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

        boolean isUserLoggedIn;
        isUserLoggedIn = (loginResult.getStatus() == Result.SUCCESS_CODE);

        if (!isUserLoggedIn){

            //Send LoginResult Message Length
            DATA_OUT.writeInt(loginResult.getMessage().length());
            System.out.println("\tSent LoginResult Message Length");

            //Read ACK
            ack = readIntWTimeout();
            printACKResult(ack);

            //Send LoginResult Message Bytes
            DATA_OUT.write(loginResult.getMessage().getBytes());
            System.out.println("\tSent LoginResult Message Bytes");

            //Read ACK
            ack = readIntWTimeout();
            printACKResult(ack);

            //Send ACK
            DATA_OUT.writeInt(ACK_CODE);
            System.out.println("\tSent ACK");

            System.out.println("LoginCMD End\n");

            return;
        }

        //Send ACK
        DATA_OUT.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        System.out.println("LoginCMD End\n");

    }

    private void handleInvalidCommand() {

    }


    private void handleNewSession() throws IOException {
        int ack;
        //get new session
        long sessionId = sessionList.newSession();

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

        //Send sessionId
        DATA_OUT.writeLong(sessionId);
        System.out.println("\tSent sessionId = " + sessionId);

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

        //Send ACK
        DATA_OUT.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

    }

    private void handleInvalidSession() throws IOException {

        int ack;

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

        //Send Invalid Session
        DATA_OUT.writeInt(Session.INVALID_SESSION_CODE);
        System.out.println("\tSent INVALID_SESSION_CODE");

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

        String errMsg = "Invalid Session!!";

        //Send errMsg Length
        DATA_OUT.writeInt(errMsg.length());
        System.out.println("\tSent errMsg Length");

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

        //Send errMsg bytes
        DATA_OUT.write(errMsg.getBytes());
        System.out.println("\tSent errMsg bytes");

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

        //Send ACK
        DATA_OUT.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

    }

    private int readIntWTimeout() throws IOException {

        final int BYTE_SIZE_OF_INT = Integer.SIZE / Byte.SIZE;

        boolean hasInt;

        long startTime = System.currentTimeMillis();

        do {
            hasInt = (IN.available() >= BYTE_SIZE_OF_INT);
        } while (!hasInt && (System.currentTimeMillis() - startTime) < TIMEOUT);

        if (hasInt) {
            return DATA_IN.readInt();
        } else {
            throw new SocketTimeoutException();
        }
    }

    private long readLongWTimeout() throws IOException {

        final long BYTE_SIZE_OF_LONG = Long.SIZE / Byte.SIZE;

        boolean hasLong;

        long startTime = System.currentTimeMillis();

        do {
            hasLong = (IN.available() >= BYTE_SIZE_OF_LONG);
        } while (!hasLong && (System.currentTimeMillis() - startTime) < TIMEOUT);

        if (hasLong) {
            return DATA_IN.readLong();
        } else {
            throw new SocketTimeoutException();
        }
    }

    private byte[] readBytesWTimeout(int numOfBytes) throws IOException {

        final int BYTE_SIZE = 1;

        byte[] readBytes = new byte[numOfBytes];

        boolean hasByte;
        for (int i = 0; i < readBytes.length; i++) {

            long startTime = System.currentTimeMillis();

            do {
                hasByte = (IN.available() >= BYTE_SIZE);
            } while (!hasByte && (System.currentTimeMillis() - startTime) < TIMEOUT);

            if (hasByte) {
                readBytes[i] = DATA_IN.readByte();
            } else {
                throw new SocketTimeoutException();
            }

        }

        return readBytes;
    }


    private void printACKResult(int ack) {

        if (ack == ACK_CODE) {
            System.out.println("\tRead ACK");
        } else {
            System.out.println("\tACK Read Error");
        }

    }

}
