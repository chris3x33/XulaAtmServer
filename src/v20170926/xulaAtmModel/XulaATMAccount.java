package v20170926.xulaAtmModel;

public class XulaATMAccount {

    private long accountId;
    private double balance;
    private int accountType;
    private XulaATMTransactionList atmTransactionList;

    public long getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
    }

    public int getAccountType() {
        return accountType;
    }


}
