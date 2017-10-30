package v20170926.xulaAtmModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import static v20170926.utils.DateTime.getCurrentDateTime;

public class XulaATMTransactionList {

    private ArrayList<XulaATMTransaction> atmTransactions;
    private String dateTimeFormatPattern = "yyyy_dd_MM_HH_mm_ss";

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

    public void writeTo(String transactionListPath) throws IOException {
        File accountListFolder = new File(transactionListPath);

        if (!accountListFolder.isDirectory()){return;}

        String currentDateTime = getCurrentDateTime(dateTimeFormatPattern);

        File accountFile = new File(transactionListPath+"\\"+currentDateTime+".txt");
        accountFile.createNewFile();

        PrintWriter out = new PrintWriter(accountFile);
        String spacer = " , ";
        for (XulaATMTransaction atmTransaction: atmTransactions){

            //write accountId
            out.print(atmTransaction.getAccountId());
            out.print(spacer);

            //write accountId
            out.print(atmTransaction.getTransactionId());
            out.print(spacer);

            //write amount
            out.print(atmTransaction.getAmount());
            out.print(spacer);

            //write type
            out.print(atmTransaction.getType());
            out.print(spacer);

            //read otherAccount
            out.print(atmTransaction.getOtherAccount());
            out.print(spacer);

            //read prevAmount
            out.print(atmTransaction.getPrevAmount());
            out.print(spacer);

            //read Date
            out.print(atmTransaction.getDateTime());
            out.print(spacer);

            out.println();

        }

        out.close();

    }

}
