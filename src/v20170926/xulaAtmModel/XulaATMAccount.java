package v20170926.xulaAtmModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class XulaATMAccount {

    private long accountId;
    private double balance;
    private int accountType;
    private XulaATMTransactionList atmTransactionList;

    public XulaATMAccount(long accountId, int accountType, double balance) {
        this.accountId = accountId;
        this.balance = balance;
        this.accountType = accountType;
    }

    public XulaATMAccount(File accountFile) throws FileNotFoundException {

        atmTransactionList = new XulaATMTransactionList();
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

        atmTransactionList.recordTransaction(
                depositAmount,XulaATMTransactionType.DEPOSIT,"CASH",prevBalance,"");

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
        out.println(accountType);
        out.println(balance);

        out.close();

        return true;
    }

    public void readAccountFrom(File accountFile) throws FileNotFoundException {

        Scanner scanner = new Scanner(accountFile);

        //Read accountId
        accountId = scanner.nextLong();
        scanner.nextLine();

        //Read accountType
        accountType = scanner.nextInt();
        scanner.nextLine();

        //Read balance
        balance = scanner.nextDouble();
        scanner.hasNextLine();

        scanner.close();

    }
}
