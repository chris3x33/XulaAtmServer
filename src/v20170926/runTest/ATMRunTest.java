package v20170926.runTest;

import v20170926.xulaAtmModel.XulaATM;

import java.util.Scanner;

public class ATMRunTest {

    private final static Scanner IN = new Scanner(System.in);

    public static void main(String[] args) {

        XulaATM xulaATM = new XulaATM();

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

        System.out.print("Login: ");

        //Get userName
        String userName = getUserNameFromUser();

        //Get password

        //Try to Login

    }

    private static String getUserNameFromUser() {

        String userName;

        System.out.print("\tEnter Your UserName: ");

        userName = IN.next();

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
