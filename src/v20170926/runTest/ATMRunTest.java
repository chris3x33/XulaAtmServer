package v20170926.runTest;

import v20170926.xulaAtmModel.result.LoginResult;
import v20170926.xulaAtmModel.XulaATM;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class ATMRunTest {

    private final static Scanner IN = new Scanner(System.in);

    private final static String XULA_ATM_PATH="XulaATMFiles";
    private final static String USERLIST_PATH=XULA_ATM_PATH+"\\UserList";
    private final static String ACCOUNTLIST_PATH=XULA_ATM_PATH+"\\AccountList";
    private final static String TRANSACTIONLIST_PATH = XULA_ATM_PATH+"\\TransactionList";

    public static void main(String[] args) throws FileNotFoundException {

        XulaATM xulaATM = new XulaATM(USERLIST_PATH,ACCOUNTLIST_PATH, TRANSACTIONLIST_PATH);

        runATM(xulaATM);

    }

    private static void runATM(XulaATM xulaATM) {

        //XulaAtm starts with a welcome msg
        System.out.println(xulaATM.getWelcomeMsg());

        //repeat options until the program quits
        //Login, Open a New Account, and Quit Options are shown
        // if invalid option, show error and repeat
        while (true) {

            outputOptions();

            //Get User Option
            char userOption = IN.next().toUpperCase().charAt(0);

            handleUserOption(xulaATM, userOption);

        }

    }

    private static void handleUserOption(XulaATM xulaATM, char userOption) {

        if (userOption == 'L') {

            //Login
            runLoginOption(xulaATM);

        } else if (userOption == 'O') {
            //Open a New Account

        } else if (userOption == 'Q') {
            //Quit
            System.exit(0);
        } else {
            //Error
        }

    }

    private static void runLoginOption(XulaATM xulaATM) {

        System.out.println("Login: ");

        //Get userName
        String userName = getUserNameFromUser();

        //Get password
        String password = getPasswordFromUser();

        //Try to Login
        LoginResult loginResult = xulaATM.login(userName, password);

    }

    private static String getPasswordFromUser() {

        String password;

        System.out.print("\tEnter Your Password: ");

        password = IN.nextLine();

        System.out.println();

        return password;

    }

    private static String getUserNameFromUser() {

        String userName;

        System.out.print("\tEnter Your UserName: ");

        userName = IN.nextLine();

        System.out.println();

        return userName;

    }

    private static void outputOptions() {

        System.out.println(
                "Options: \n" +
                        "\t Enter \'L\' to Login\n" +
                        "\t Enter \'O\' to Open a New Account\n"+
                        "\t Enter \'Q\' to Quit\n"
        );

    }

}
