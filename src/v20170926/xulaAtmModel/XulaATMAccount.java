package v20170926.xulaAtmModel;

import v20170926.xulaAtmModel.result.DepositResult;
import v20170926.xulaAtmModel.result.Result;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Scanner;

public class XulaATMAccount {

    private SecureRandom random = new SecureRandom();

    private long accountId;
    private long userId;
    private double balance;
    private int accountType;
    private ArrayList<Long> transactionIds;

    public XulaATMAccount(long accountId, long userId, int accountType, double balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
        this.accountType = accountType;
    }

    public XulaATMAccount(File accountFile) throws FileNotFoundException {

        transactionIds = new ArrayList<Long>();
        readAccountFrom(accountFile);

    }

    public long getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public DepositResult deposit(double depositAmount){

        //check if depositAmount is negative
        if (depositAmount < 0){

            return new DepositResult(
                    Result.ERROR_CODE,
                    "Deposit amount can not be negative!!"
            );

        }

        double prevBalance = balance;

        setBalance(prevBalance + depositAmount);

        return new DepositResult(Result.SUCCESS_CODE);
        
    }

    public int getAccountType() {
        return accountType;
    }


    public boolean writeTo(String accountListFolderPath) throws IOException {
        File accountListFolder = new File(accountListFolderPath);

        if (!accountListFolder.isDirectory()){return false;}

        File accountFile = new File(accountListFolderPath+"\\"+accountId+".txt");
        accountFile.createNewFile();

        PrintWriter out = new PrintWriter(accountFile);

        out.println(accountId);
        out.println(userId);
        out.println(accountType);
        out.println(balance);

        for (long transactionId : transactionIds){
            out.println(transactionId);
        }

        out.close();

        return true;
    }

    public void readAccountFrom(File accountFile) throws FileNotFoundException {

        Scanner scanner = new Scanner(accountFile);

        //Read accountId
        accountId = scanner.nextLong();
        scanner.nextLine();

        //Read userId
        userId = scanner.nextLong();
        scanner.nextLine();

        //Read accountType
        accountType = scanner.nextInt();
        scanner.nextLine();

        //Read balance
        balance = scanner.nextDouble();
        scanner.nextLine();

        //Read transactionIds
        while (scanner.hasNextLine()){
            transactionIds.add(scanner.nextLong());
            scanner.nextLine();
        }

        scanner.close();

    }
    public long getUserId() {
        return userId;
    }

    public ArrayList<Long> getTransactionIds() {
        return transactionIds;
    }

    public boolean hasTransaction(long transactionId){

        for (long curTransactionId: transactionIds){
            if (curTransactionId == transactionId){
                return true;
            }
        }

        return false;

    }

    public long getUnusedTransactionId() {

        long unusedTransactionId;
        do {
            unusedTransactionId = Math.abs(random.nextLong());

        } while (hasTransaction(unusedTransactionId));

        return unusedTransactionId;

    }

    @Override
    public String toString() {

        return  userId + " , " +
                accountId + " , " +
                accountType + " , " +
                balance;
    }

}
