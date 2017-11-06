package v20170926.xulaAtmModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class XulaATMTransaction {
    private long accountId;
    private long transactionId;
    private double amount;
    private int type;// W , D
    private String otherAccount;
    private String dateTime;
    private double prevAmount;

    public XulaATMTransaction(
            long accountId, long transactionId, double amount, int type,
            String otherAccount, double prevAmount, String dateTime) {

        this.accountId = accountId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.otherAccount = otherAccount;
        this.prevAmount = prevAmount;
        this.dateTime = dateTime;
    }

    public XulaATMTransaction(File transactionFile) throws FileNotFoundException, NoSuchElementException{
        readTransactionFrom(transactionFile);

        //get FileName
        String transactionFileName = transactionFile.getName();
        transactionFileName = transactionFileName.substring(0,transactionFileName.indexOf('.'));

        String validFileName = Long.toString(getAccountId())+"-"+Long.toString(getTransactionId());

        if(transactionFileName.compareTo(validFileName)!=0){throw new NoSuchElementException();}
    }

    private void readTransactionFrom(File transactionFile) throws FileNotFoundException, NoSuchElementException{

        Scanner scanner = new Scanner(transactionFile);

        String line = scanner.nextLine();

        XulaATMTransaction atmTransaction = parse(line);

        if (atmTransaction==null){ throw new NoSuchElementException();}

        this.accountId = atmTransaction.accountId;
        this.transactionId = atmTransaction.transactionId;
        this.amount = atmTransaction.amount;
        this.type = atmTransaction.type;
        this.otherAccount = atmTransaction.otherAccount;
        this.dateTime = atmTransaction.dateTime;
        this.prevAmount = atmTransaction.prevAmount;

    }

    public double getAmount() {
        return amount;
    }

    public int getType() {
        return type;
    }

    public String getOtherAccount() {
        return otherAccount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public double getPrevAmount() {
        return prevAmount;
    }

    public long getAccountId() {
        return accountId;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public synchronized void writeTo(String transactionListPath) throws IOException {

        File accountListFolder = new File(transactionListPath);

        if (!accountListFolder.isDirectory()){return;}

        File accountFile = new File(transactionListPath+"\\"+accountId+"-"+transactionId+".txt");
        accountFile.createNewFile();

        PrintWriter out = new PrintWriter(accountFile);

        synchronized (this) {
            out.println(toString());
        }

        out.close();

    }

    public void writeToAsyn(String transactionListPath){

        class Writer implements Runnable{

            private String transactionListPath;

            public Writer(String transactionListPath){
                this.transactionListPath = transactionListPath;
            }

            @Override
            public void run() {
                try {
                    writeTo(transactionListPath);
                } catch (IOException e) {

                }
            }
        }

        Thread writerThread = new Thread(new Writer(transactionListPath));
        writerThread.start();

    }

    @Override
    public String toString() {
        return accountId + " , " +
               transactionId + " , " +
               amount + " , " +
               type + " , " +
               otherAccount +  " , " +
               dateTime +  " , " +
               prevAmount;
    }

    public static XulaATMTransaction parse(String str) {

        try {

            Scanner parser = new Scanner(str);

            //Read accountId
            long accountId = parser.nextLong();
            parser.next();

            //Read transactionId
            long transactionId = parser.nextLong();
            parser.next();

            //Read amount
            double amount = parser.nextDouble();
            parser.next();

            //Read type
            int type = parser.nextInt();
            parser.next();

            //Read otherAccount
            String otherAccount = parser.next();
            parser.next();

            //Read dateTime
            String dateTime = parser.next();
            parser.next();

            //Read amount
            double prevAmount = parser.nextDouble();

            return new XulaATMTransaction(
                    accountId,  transactionId,  amount,  type,
                    otherAccount,  prevAmount,  dateTime
            );

        } catch (InputMismatchException e){
            return null;
        }catch (NoSuchElementException e){
            return null;
        }
    }
}
