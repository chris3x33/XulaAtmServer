package v20170926.runTest;

import v20170926.xulaAtmModel.result.LoginResult;
import v20170926.xulaAtmModel.result.Result;
import v20170926.xulaAtmModel.XulaATM;

import java.io.IOException;
import java.util.Scanner;

public class ATMLoginTest {

    private final static String XULA_ATM_PATH="XulaATMFiles";
    private final static String USERLIST_PATH=XULA_ATM_PATH+"\\UserList";
    private final static String ACCOUNTLIST_PATH=XULA_ATM_PATH+"\\AccountList";
    private final static Scanner IN = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        XulaATM xulaATM = new XulaATM(USERLIST_PATH, ACCOUNTLIST_PATH);

        String userName;
        String password;

        boolean isUserLoggedIn;
        LoginResult loginResult;

        do{

            userName = getUserNameFromUser();
            password = getPasswordFromUser();

            loginResult = xulaATM.login(userName, password);

            isUserLoggedIn = (loginResult.getStatus() == Result.SUCCESS_CODE);

            if(!isUserLoggedIn){
                System.out.println(loginResult.getMessage() + "\n");
            }

        }while (!isUserLoggedIn);

        System.out.println("User LoggedIn!!");
        System.out.println("UserName: "+userName);
        System.out.println("UserId: "+loginResult.getUserId());
    }
    private static String getUserNameFromUser() {

        String userName;

        System.out.print("\tEnter Your UserName: ");

        userName = IN.nextLine();

        System.out.println();

        return userName;

    }

    private static String getPasswordFromUser() {

        String password;

        System.out.print("\tEnter Your Password: ");

        password = IN.nextLine();

        System.out.println();

        return password;

    }
}
