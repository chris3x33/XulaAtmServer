package v20170926.xulaAtmModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class XulaATMTransactionList {

    private ArrayList<XulaATMTransaction> atmTransactions;

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
            readInTransactionsFrom(transactionFile);
        }

    }

    private void readInTransactionsFrom(File transactionFile) throws FileNotFoundException {
        Scanner scanner = new Scanner(transactionFile);

        while (scanner.hasNextLine()) {

            //read accountId
            long accountId = scanner.nextLong();
            scanner.next();// read " , "

            //read accountId
            long transactionId = scanner.nextLong();
            scanner.next();// read " , "

            //read amount
            double amount = scanner.nextDouble();
            scanner.next();// read " , "

            //read type
            int type = scanner.nextInt();
            scanner.next();// read " , "

            //read otherAccount
            String otherAccount = scanner.next();
            scanner.next();// read " , "

            //read prevAmount
            double prevAmount = scanner.nextDouble();
            scanner.next();// read " , "

            //read Date
            String date = scanner.next();
            scanner.nextLine();

            XulaATMTransaction xulaATMTransaction = new XulaATMTransaction(
                    accountId,
                    transactionId,
                    amount,
                    type,
                    otherAccount,
                    prevAmount,
                    date
            );

            atmTransactions.add(xulaATMTransaction);
        }

    }

}
