package v20170926.utils;

import v20170926.xulaAtmModel.Result;
import v20170926.xulaAtmModel.XulaATM;

import java.util.Scanner;

public class NewUserCreator {

    private final static String XULA_ATM_PATH="XulaATMFiles";
    private final static String USERLIST_PATH=XULA_ATM_PATH+"\\UserList";
    private final static String ACCOUNTLIST_PATH=XULA_ATM_PATH+"\\AccountList";
    private final static Scanner IN = new Scanner(System.in);

    public static void main(String[] args) {

        XulaATM xulaATM = new XulaATM();

        //Get UserName
        String userName;

        boolean userExists;
        boolean isValidUserName;
        do{
            userName = getUserNameFromUser();

            Result validUserNameResult = xulaATM.isValidUserName(userName);

            isValidUserName = (validUserNameResult.getStatus() == Result.SUCCESS_CODE);
            if(!isValidUserName){
                System.out.println(validUserNameResult.getMessage() + "\n");
            }

            userExists = xulaATM.userExists(userName);
            if (isValidUserName && userExists){
                System.out.println("UserName already exists\n");
            }

        }while (!isValidUserName||userExists);


        //Get Password

        //Create UserId

        //Create Checking Account

        //Create Savings Account

        //Write to Filesystem

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
