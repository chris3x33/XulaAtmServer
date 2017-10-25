package v20170926.xulaAtmServerController;

import v20170926.xulaAtmModel.*;

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

            case XulaAtmServerCommands.GET_USERNAME_CMD:

                handleGetUsernameCommand();

                break;

            case XulaAtmServerCommands.GET_USER_ACCOUNTIDS_CMD:

                handleGetUserAccountIdsCommand();

                break;

            default://Invalid Command

                handleInvalidCommand();

                break;
        }

    }

    private void handleGetUserAccountIdsCommand() throws IOException {
        int ack;

        System.out.println("\nGetUserAccountIdsCMD Start");

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

        //Get userId
        Session session = sessionList.getSession(sessionId);
        long userId = session.getUserId();

        //Get getAccountIdsResult
        GetAccountIdsResult getAccountIdsResult;
        synchronized (xulaATM) {
            getAccountIdsResult = xulaATM.getAccountIDs(userId);
        }

        //Send getAccountIdsResult
        sendResult(getAccountIdsResult);

        //if ERROR, Do not send AccountIds.
        if (getAccountIdsResult.getStatus() == Result.ERROR_CODE) {

            System.out.println("GetUserAccountIdsCMD End\n");

            return;

        }

        //Send numOfAccountIDs
        writeLongs(getAccountIdsResult.getAccountIDs());
        System.out.println("\tSent AccountIds: "+getAccountIdsResult.getAccountIDs());

        System.out.println("GetUserAccountIdsCMD End\n");

    }

    private void handleGetUsernameCommand() throws IOException {

        int ack;

        System.out.println("\nGetUsernameCMD Start");

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

        //Get userId
        Session session = sessionList.getSession(sessionId);
        long userId = session.getUserId();

        //Get userNameResult
        GetUserNameResult userNameResult;
        synchronized (xulaATM) {
            userNameResult = xulaATM.getUserName(userId);
        }

        //Send userNameResult
        sendResult(userNameResult);

        //if ERROR, Do not send userName.
        if (userNameResult.getStatus() == Result.ERROR_CODE) {

            System.out.println("GetUsernameCMD End\n");

            return;

        }

        //Get userName
        String userName = userNameResult.getUserName();

        //Send userName
        sendString(userName);

        System.out.println("GetUsernameCMD End\n");

    }


    private void handleLogoutCommand() throws IOException {

        int ack;

        System.out.println("\nLogoutCMD Start");

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

        sessionList.deleteSession(sessionId);
        System.out.println("\tSession Id: "+sessionId+" Deleted");

        //Send ACK
        DATA_OUT.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        System.out.println("LogoutCMD End\n");

    }

    private void handleCreateNewUserCommand() throws IOException {
        int ack;

        System.out.println("\nCreateNewUserCMD Start");

        String userName = readString();
        String password = readString();

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

        sendResult(createNewUserResult);

        System.out.println("CreateNewUserCMD End\n");

    }

    private void handleLoginCommand() throws IOException {
        int ack;

        System.out.println("\nLoginCMD Start");

        String userName = readString();
        String password = readString();

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

        LoginResult loginResult;
        synchronized (xulaATM) {
            loginResult = xulaATM.login(userName, password);
        }

        sendResult(loginResult);

        //Add user id to the session
        Session session = sessionList.getSession(sessionId);
        session.setUserId(loginResult.getUserId());

        System.out.println("LoginCMD End\n");

    }

    private String readString() throws IOException {

        //read Str Len
        int readStrLen = readIntWTimeout();
        System.out.println("\tRead readStr Len");

        //Send Ack
        DATA_OUT.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        //Read Str
        String readStr = new String(readBytesWTimeout(readStrLen));
        System.out.println("\tRead readStr: "+readStr);

        //Send Ack
        DATA_OUT.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        return readStr;

    }

    private void sendString(String sendStr) throws IOException {

        int ack;

        //Send sendStr Length
        int sendStrLen = sendStr.length();
        DATA_OUT.writeInt(sendStrLen);
        System.out.println("\tSent Str Len");

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

        DATA_OUT.write(sendStr.getBytes());
        System.out.println("\tSent Str: "+sendStr);

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

    }

    private void sendResult(Result result) throws IOException {

        int ack;

        //Send Result Status
        DATA_OUT.writeInt(result.getStatus());
        System.out.println("\tSent Result Status: "+result.getStatus());

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

        //handle Results Msg
        if (result.getStatus() == Result.ERROR_CODE){

            //Send Result Message Length
            DATA_OUT.writeInt(result.getMessage().length());
            System.out.println("\tSent Result Message Length");

            //Read ACK
            ack = readIntWTimeout();
            printACKResult(ack);

            //Send Result Message Bytes
            DATA_OUT.write(result.getMessage().getBytes());
            System.out.println("\tSent Result Message Bytes: "+result.getMessage());

            //Read ACK
            ack = readIntWTimeout();
            printACKResult(ack);

            //Send ACK
            DATA_OUT.writeInt(ACK_CODE);
            System.out.println("\tSent ACK");

            return;
        }

        //Send ACK
        DATA_OUT.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");
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
        sendResult(new Result(Result.ERROR_CODE, "Invalid Session!!"));

    }

    private void writeLongs(ArrayList<Long> longs) throws IOException{

        int ack;

        //Send numOfLongs
        DATA_OUT.writeInt(longs.size());
        System.out.println("\tSend numOfLongs");

        //Read ACK
        ack = readIntWTimeout();
        printACKResult(ack);

        //Send longs
        for (long curLong :longs) {

            //Send long
            DATA_OUT.writeLong(curLong);

            //Read ACK
            ack = readIntWTimeout();
            printACKResult(ack);

        }

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
