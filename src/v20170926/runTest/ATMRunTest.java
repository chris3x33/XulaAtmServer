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

        //Login, Open a New Account, and Quit Options are shown

        //repeat options until the program quits

        //if invalid option, show error and repeat valid options


    }

}
