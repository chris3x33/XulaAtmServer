package v20170926.xulaAtmServerController;

import v20170926.xulaAtmModel.CreateNewUserResult;
import v20170926.xulaAtmModel.LoginResult;
import v20170926.xulaAtmModel.Result;
import v20170926.xulaAtmModel.XulaATM;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private final int TIMEOUT = 5000;
    private final Socket SOCKET;
    private final InputStream IN;
    private final DataInputStream DATA_IN;
    private final DataOutputStream DATA_OUT;

    private static SessionList sessionList = new SessionList();
    private long sessionId;

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
            sessionId = readLongWTimeout();
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

            int ack;

            //Read ACK
            ack = readIntWTimeout();
            printACKResult(ack);

            //Send Valid session
            DATA_OUT.writeInt(Session.VALID_SESSION_CODE);
            System.out.println("Send VALID_SESSION_CODE");

            //Read ACK
            ack = readIntWTimeout();
            printACKResult(ack);

            //Send ACK
            DATA_OUT.writeInt(ACK_CODE);
            System.out.println("Sent ACK");

            //read Command
            int command = readIntWTimeout();
            System.out.println("read Command: "+command);

            //Send ACK
            DATA_OUT.writeInt(ACK_CODE);
            System.out.println("Sent ACK");

            handleCommand(command);

            SOCKET.close();

        } catch (SocketTimeoutException e) {
            System.out.println("SocketTimeoutException");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void handleCommand(int command) throws IOException {

        switch (command){

            case XulaAtmServerCommands.LOGIN_CMD:

                handleLoginCommand();

                break;

            case XulaAtmServerCommands.CREATE_NEW_USER_CMD:

                handleCreateNewUserCommand();

                break;

            case XulaAtmServerCommands.LOGOUT_CMD:

                handleLogoutCommand();

                break;



            default://Invalid Command

                handleInvalidCommand();

                break;
        }

    }

    private void handleLogoutCommand() throws IOException {

        int ack;

        System.out.println("\nLogoutCMD Start");

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

        sessionList.deleteSession(sessionId);
        System.out.println("\tSession Ld: "+sessionId+" Deleted");

        //Send ACK
        DATA_OUT.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        System.out.println("LogoutCMD End\n");

    }

    private void handleCreateNewUserCommand() throws IOException {
        int ack;

        System.out.println("\nCreateNewUserCMD Start");

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

        CreateNewUserResult createNewUserResult;
        synchronized (xulaATM) {
            createNewUserResult = xulaATM.createNewUser(
                    userName,
                    password
            );
        }

        //Send CreateNewUserResult Status
        DATA_OUT.writeInt(createNewUserResult.getStatus());
        System.out.println("\tSent CreateNewUserResult Status Length");

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

        boolean isNewUserCreated;
        isNewUserCreated = (createNewUserResult.getStatus() == Result.SUCCESS_CODE);

        if (!isNewUserCreated){

            //Send CreateNewUserResult Message Length
            DATA_OUT.writeInt(createNewUserResult.getMessage().length());
            System.out.println("\tSent CreateNewUserResult Message Length");

            //Read ACK
            ack = readIntWTimeout();
            printACKResult(ack);

            //Send CreateNewUserResult Message Bytes
            DATA_OUT.write(createNewUserResult.getMessage().getBytes());
            System.out.println("\tSent CreateNewUserResult Message Bytes");

            //Read ACK
            ack = readIntWTimeout();
            printACKResult(ack);

            //Send ACK
            DATA_OUT.writeInt(ACK_CODE);
            System.out.println("\tSent ACK");

            System.out.println("CreateNewUserCMD End\n");

            return;
        }

        //Send ACK
        DATA_OUT.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        System.out.println("CreateNewUserCMD End\n");

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

        LoginResult loginResult;
        synchronized (xulaATM) {
            loginResult = xulaATM.login(userName, password);
        }

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

        //Add user id to the session
        Session session = sessionList.getSession(sessionId);
        session.setUserId(loginResult.getUserId());

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
