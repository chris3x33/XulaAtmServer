package v20170926.xulaAtmServerController;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final int TIMEOUT = 3000;
    private final Socket SOCKET;
    private final InputStream IN;

    public ClientHandler(Socket socket) throws IOException {
        SOCKET = socket;
        IN = socket.getInputStream();
    }

    @Override
    public void run() {

    }



}
