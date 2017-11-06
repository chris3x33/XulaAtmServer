package v20170926.runTest;

import v20170926.xulaAtmModel.*;
import v20170926.xulaAtmModel.result.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ATMDepositWithdrawTest {

    private final static String XULA_ATM_PATH="XulaATMFiles";
    private final static String USERLIST_PATH=XULA_ATM_PATH+"\\UserList";
    private final static String ACCOUNTLIST_PATH=XULA_ATM_PATH+"\\AccountList";
    private final static String TRANSACTIONLIST_PATH = XULA_ATM_PATH+"\\TransactionList";

    private final static Scanner IN = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException {

        XulaATM xulaATM = new XulaATM(USERLIST_PATH, ACCOUNTLIST_PATH, TRANSACTIONLIST_PATH);

        String userName;
        String password;

        boolean isUserLoggedIn;
        LoginResult loginResult;

        do{

            System.out.println("Login:");

            userName = "u123456";
            password = "Pw123456";
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

        long userId = loginResult.getUserId();


        while(true) {

            //Get user Option
            char userOption = getUserOption();

            //run Option
            switch (userOption) {
                case 'W':

                    break;
                case 'D':
                    runDepositOption(xulaATM, userId);
                    break;

                case 'Q':
                    System.exit(0);
                    break;

                default:
                    break;
            }

        }

    }

    private static void runDepositOption(XulaATM xulaATM,long userId) {

        System.out.println("Deposit: ");

        //Get User Accounts
        GetAccountIdsResult getAccountIdsResult = xulaATM.getAccountIDs(userId);
        ArrayList<Long> accountIDs = getAccountIdsResult.getAccountIDs();

        //Get User Account Balances
        ArrayList<Double> accountBalances = new ArrayList<Double>();
        for (int i = 0; i < accountIDs.size(); i++){

            long accountId = accountIDs.get(i);
            GetAccountBalanceResult getAccountBalanceResult = xulaATM.getAccountBalance(
                    userId, accountId);
            accountBalances.add(getAccountBalanceResult.getAccountBalance());

        }

        //Get Account Selection
        int accountSelection;
        boolean hasViladAccountSelection;
        do {

            System.out.println("\tSelect an account to Deposit into: ");

            //List Accounts
            for (int i = 0; i < accountIDs.size(); i++) {

                long accountId = accountIDs.get(i);
                Double accountBalance = accountBalances.get(i);

                System.out.println("\t\tEnter \""+(i+1)+"\" for\n"+
                            "\t\t\taccount: "+accountId+"\n"+
                            "\t\t\tbalance: "+accountBalance+"\n"
                    );



            }

            accountSelection = IN.nextInt() - 1;
            IN.hasNextLine();
            System.out.println("accountSelection: "+accountSelection);
            hasViladAccountSelection = (accountSelection  > -1 && accountSelection < accountIDs.size());

            if(!hasViladAccountSelection){
                System.out.println("\tInvalid Account Selection!!\n");
            }


        }while (!hasViladAccountSelection);

        //output Selected Account
        long accountId = accountIDs.get(accountSelection);
        double accountBalance = accountBalances.get(accountSelection);
        System.out.println("Selected Account: "+accountId);
        System.out.println("Balance: "+accountBalance);

        //Get Amount
        double amount;
        System.out.println("\tEnter the Deposit amount: ");
        amount = IN.nextDouble();
        IN.hasNextLine();

        DepositResult depositResult = xulaATM.deposit(userId, accountId, amount);

        if (depositResult.getStatus() == Result.ERROR_CODE){

            System.out.println(depositResult.getMessage());

            return;

        }

        //Get New Balance
        double accountNewBalance = xulaATM.getAccountBalance(userId,accountId).getAccountBalance();

        //Output New Balance
        System.out.println("New Balance: "+accountNewBalance);

    }

    private static char getUserOption() {

        boolean hasOption;
        char userOption;
        do {

            System.out.println("\nEnter a choice");
            System.out.println("\tEnter \"D\" for Deposit");
            System.out.println("\tEnter \"W\" for Withdraw");
            System.out.println("\tEnter \"Q\" for Quit");

            userOption = IN.next().toUpperCase().charAt(0);

            switch (userOption){
                case 'W':
                case 'D':
                case 'Q':

                    hasOption = true;

                    break;
                default:

                    hasOption = false;

                    System.out.println("\nError not a valid choice");

                    break;
            }

        }while(!hasOption);

        return userOption;

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
