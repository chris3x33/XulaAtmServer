package v20170926.xulaAtmServerController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientHandler implements Runnable {

    private final int TIMEOUT = 3000;
    private final Socket SOCKET;
    private final InputStream IN;
    private final DataInputStream DATA_IN;
    private final DataOutputStream DATA_OUT;
    private static SessionList sessionList = new SessionList();
    private final int ACK_CODE = 10101010;


    public ClientHandler(Socket socket) throws IOException {
        SOCKET = socket;
        IN = socket.getInputStream();
        DATA_IN = new DataInputStream(IN);
        DATA_OUT = new DataOutputStream(socket.getOutputStream());
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

                System.out.println("NewSessionCMD Start");
                handleNewSession();
                System.out.println("NewSessionCMD End\n");

                SOCKET.close();

                return;
            }

            SOCKET.close();

        } catch (SocketTimeoutException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }


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

    private void printACKResult(int ack) {

        if (ack == ACK_CODE) {
            System.out.println("\tRead ACK");
        } else {
            System.out.println("\tACK Read Error");
        }

    }

}
