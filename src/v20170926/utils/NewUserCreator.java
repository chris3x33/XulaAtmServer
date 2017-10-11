package v20170926.utils;

import v20170926.xulaAtmModel.CreateNewUserResult;
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

        String userName;
        String password;

        boolean isNewUserCreated;
        CreateNewUserResult newUserResult;

        do{

            userName = getUserNameFromUser();
            password = getPasswordFromUser();

            newUserResult = xulaATM.createNewUser(userName, password);

            isNewUserCreated = (newUserResult.getStatus() == Result.SUCCESS_CODE);
            if(!isNewUserCreated){
                System.out.println(newUserResult.getMessage() + "\n");
            }

        }while (!isNewUserCreated);

        System.out.println("New User Created!!");
        System.out.println("UserName: "+userName);
        System.out.println("UserId: "+newUserResult.getUserId());

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
