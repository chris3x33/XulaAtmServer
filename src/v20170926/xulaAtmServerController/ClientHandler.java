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
            System.out.println("Read sessionId = " + sessionId);

            //Send ACK
            DATA_OUT.writeInt(ACK_CODE);
            System.out.println("Sent ACK");


            //check for new session
            if(sessionId <= -1){

                handleNewSession();

                SOCKET.close();

                return;
            }

            SOCKET.close();

        } catch (SocketTimeoutException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void handleNewSession() {



    }

    private int readIntWTimeout() throws IOException {

        final int BYTE_SIZE_OF_INT = Integer.SIZE/ Byte.SIZE;

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

        final long BYTE_SIZE_OF_LONG = Long.SIZE/ Byte.SIZE;

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


}
