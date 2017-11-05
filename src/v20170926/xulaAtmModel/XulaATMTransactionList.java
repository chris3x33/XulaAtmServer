package v20170926.xulaAtmModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class XulaATMTransactionList {

    private ArrayList<XulaATMTransaction> atmTransactions;
    private SecureRandom random = new SecureRandom();

    public XulaATMTransactionList(String transactionListFolder) throws FileNotFoundException {
        atmTransactions = new ArrayList<XulaATMTransaction>();
        readInTransactionsFrom(transactionListFolder);
    }

    public void recordTransaction(long accountId, long transactionId, double amount, int type, String otherAccount, double prevAmount, String dateTime) {

        atmTransactions.add(
                new XulaATMTransaction(
                        accountId,
                        transactionId,
                        amount,
                        type,
                        otherAccount,
                        prevAmount,
                        dateTime
                )
        );

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

            try {

                XulaATMTransaction atmTransaction = new XulaATMTransaction(transactionFile);
                atmTransactions.add(atmTransaction);

            }catch (IOException | NoSuchElementException e){

            }

        }

    }

    public void writeTo(String transactionListPath) throws IOException {

        for (XulaATMTransaction atmTransaction: atmTransactions){

            atmTransaction.writeTo(transactionListPath);

        }

    }

    public ArrayList<Long> getTransactionIds(long accountId){

        ArrayList<Long> transactionIds = new ArrayList<Long>();

        for (XulaATMTransaction atmTransaction : atmTransactions){

            if(atmTransaction.getAccountId() == accountId){

                transactionIds.add(
                        atmTransaction.getTransactionId()
                );

            }
        }

        return transactionIds;

    }

    public long getUnusedTransactionId(long accountId) {

        long unusedTransactionId;
        ArrayList<Long> transactionIds = getTransactionIds(accountId);
        do {
            unusedTransactionId = Math.abs(random.nextLong());

        } while (transactionIds.contains(unusedTransactionId));

        return unusedTransactionId;

    }

}
