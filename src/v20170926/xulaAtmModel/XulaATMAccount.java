package v20170926.xulaAtmModel;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

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


    public long getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
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

        return true;
    }
}
