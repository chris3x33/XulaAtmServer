package v20170926.utils;

import v20170926.xulaAtmModel.result.CreateNewUserResult;
import v20170926.xulaAtmModel.result.Result;
import v20170926.xulaAtmModel.XulaATM;
import java.io.IOException;
import java.util.Scanner;

public class NewUserCreator {

    private final static String XULA_ATM_PATH="XulaATMFiles";
    private final static String USERLIST_PATH=XULA_ATM_PATH+"\\UserList";
    private final static String ACCOUNTLIST_PATH=XULA_ATM_PATH+"\\AccountList";
    private final static String TRANSACTIONLIST_PATH = XULA_ATM_PATH+"\\TransactionList";

    private final static Scanner IN = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        XulaATM xulaATM = new XulaATM( USERLIST_PATH, ACCOUNTLIST_PATH, TRANSACTIONLIST_PATH);

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
        System.out.println("User Accounts: "+ xulaATM.getAccountIDs(newUserResult.getUserId()));
        xulaATM.writeTo(USERLIST_PATH,ACCOUNTLIST_PATH);

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
