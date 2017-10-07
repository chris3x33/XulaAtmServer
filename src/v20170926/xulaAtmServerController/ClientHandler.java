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
            int sessionId = readIntWTimeout();

            //check for new session
            if(sessionId <= -1){

                handleNewSession();

                SOCKET.close();

                return;
            }


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

}
