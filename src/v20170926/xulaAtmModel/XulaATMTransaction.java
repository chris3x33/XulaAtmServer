package v20170926.xulaAtmModel;

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

    public XulaATMTransaction(long accountId, long transactionId, double amount, int type, String otherAccount, double prevAmount, String dateTime) {

        this.accountId = accountId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.otherAccount = otherAccount;
        this.prevAmount = prevAmount;
        this.dateTime = dateTime;
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

            return null;

        } catch (InputMismatchException e){
            return null;
        }catch (NoSuchElementException e){
            return null;
        }
    }
}
