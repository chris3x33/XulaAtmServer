package v20170926.xulaAtmModel;

import v20170926.xulaAtmModel.result.DepositResult;
import v20170926.xulaAtmModel.result.Result;
import v20170926.xulaAtmModel.result.WithdrawResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class XulaATMAccount {

    private long accountId;
    private long userId;
    private double balance;
    private int accountType;

    public XulaATMAccount(long accountId, long userId, int accountType, double balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
        this.accountType = accountType;
    }

    public XulaATMAccount(File accountFile) throws FileNotFoundException, NoSuchElementException {

        readAccountFrom(accountFile);

        //get FileName
        String accountFileName = accountFile.getName();
        accountFileName = accountFileName.substring(0,accountFileName.indexOf('.'));

        if(accountFileName.compareTo(Long.toString(getAccountId()))!=0){throw new NoSuchElementException();}


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

    public WithdrawResult withdraw(double withdrawAmount) {

        //check if withdrawAmount is negative
        if (withdrawAmount < 0){

            return new WithdrawResult(
                    Result.ERROR_CODE,
                    "Withdraw amount can not be negative!!"
            );

        }

        //check if balance - withdrawAmount is negative
        if ((balance - withdrawAmount) < 0){

            return new WithdrawResult(
                    Result.ERROR_CODE,
                    "Insufficient funds!!"
            );

        }

        double prevBalance = balance;

        setBalance(prevBalance - withdrawAmount);

        return new WithdrawResult(Result.SUCCESS_CODE);

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

        synchronized (this) {
            out.println(toString());
        }

        out.close();

        return true;
    }

    public void writeToAsync(String accountListFolderPath){

        class Writer implements Runnable{

            private String accountListFolderPath;

            public Writer(String transactionListPath){

                this.accountListFolderPath = transactionListPath;
            }

            @Override
            public void run() {
                try {
                    writeTo(accountListFolderPath);
                } catch (IOException e) {

                }
            }
        }

        Thread writerThread = new Thread(new Writer(accountListFolderPath));
        writerThread.start();

    }

    public void readAccountFrom(File accountFile) throws FileNotFoundException, NoSuchElementException {

        Scanner scanner = new Scanner(accountFile);

        String line = scanner.nextLine();

        XulaATMAccount atmAccount = parse(line);

        if (atmAccount==null){ throw new NoSuchElementException();}

        this.userId = atmAccount.userId;
        this.accountId = atmAccount.accountId;
        this.accountType = atmAccount.accountType;
        this.balance = atmAccount.balance;

        scanner.close();

    }
    public long getUserId() {
        return userId;
    }

    @Override
    public String toString() {

        return  userId + " , " +
                accountId + " , " +
                accountType + " , " +
                balance;
    }

    public static XulaATMAccount parse(String str) {

        try {

            Scanner parser = new Scanner(str);

            //Read UserId
            long userId = parser.nextLong();
            parser.next();

            //Read accountId
            long accountId = parser.nextLong();
            parser.next();

            //Read accountType
            int accountType = parser.nextInt();
            parser.next();

            //Read balance
            double balance = parser.nextDouble();

            return new XulaATMAccount(accountId,userId,accountType,balance);

        } catch (InputMismatchException e){
            return null;
        }catch (NoSuchElementException e){
            return null;
        }
    }
}
