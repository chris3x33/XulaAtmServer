package v20170926.xulaAtmServerController;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final int TIMEOUT = 3000;
    private final Socket SOCKET;

    public ClientHandler(Socket socket) throws IOException {
        SOCKET = socket;
    }

    @Override
    public void run() {

    }



}
