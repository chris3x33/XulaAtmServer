package v20170926.xulaAtmServerController;

import v20170926.xulaAtmModel.XulaATM;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class XulaAtmServer {

    private static final int PORT = 55555;
    private static final String IP_ADDRESS = "127.0.0.1" /*"192.168.1.74"*/;
    private static final int TIMEOUT = 100;

    private final static String XULA_ATM_PATH="XulaATMFiles";
    private final static String USERLIST_PATH=XULA_ATM_PATH+"\\UserList";
    private final static String ACCOUNTLIST_PATH=XULA_ATM_PATH+"\\AccountList";
    private final static String TRANSACTIONLIST_PATH = XULA_ATM_PATH+"\\TransactionList";

    private static XulaATM xulaATM;

    public static void main(String[] args) {

        try {

            ServerSocket server = setupServer();

            xulaATM = new XulaATM(USERLIST_PATH, ACCOUNTLIST_PATH, TRANSACTIONLIST_PATH);

            runServer(server);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void runServer(ServerSocket server) throws IOException {
        System.out.println("Server Running");
        while (true) {

            try {

                Socket socket = server.accept();
                System.out.println("Accepted Connection From "+socket.getRemoteSocketAddress());
                Thread clientHandlerThread = new Thread(new ClientHandler(socket, xulaATM));
                clientHandlerThread.start();

            } catch (SocketTimeoutException e) {
            }
        }

    }

    private static ServerSocket setupServer() throws IOException {

        System.out.println("Setting up Server");

        ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(IP_ADDRESS));
        server.setSoTimeout(TIMEOUT);

        return server;

    }

}
