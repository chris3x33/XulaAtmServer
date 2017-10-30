package v20170926.xulaAtmModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class XulaATMTransactionList {

    private ArrayList<XulaATMTransaction> atmTransactions;

    public XulaATMTransactionList(String transactionListFolder) throws FileNotFoundException {
        atmTransactions = new ArrayList<XulaATMTransaction>();
        readInTransactionsFrom(transactionListFolder);
    }

    public void recordTransaction(double amount, int type, String otherAccount, double prevAmount, String dateTime) {

        atmTransactions.add(new XulaATMTransaction(amount, type, otherAccount, prevAmount, dateTime));

    }

    private void readInTransactionsFrom(String transactionListPath) throws FileNotFoundException {

        File transactionListFolder = new File(transactionListPath);
        if (!transactionListFolder.isDirectory()) {
            return;
        }

        File[] transactionFiles = transactionListFolder.listFiles();
        if (transactionFiles == null) {
            return;
        }

        for (File transactionFile : transactionFiles) {

        }

    }

}
