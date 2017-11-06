package v20170926.xulaAtmModel;

import v20170926.xulaAtmModel.result.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class XulaATMAccountList {

    private SecureRandom random = new SecureRandom();
    private ArrayList<XulaATMAccount> atmAccounts;
    private String accountListFolderPath;

    public XulaATMAccountList(String accountListFolderPath) throws FileNotFoundException {
        atmAccounts = readAccountsFrom(accountListFolderPath);
        this.accountListFolderPath = accountListFolderPath;
    }

    public String getAccountListFolderPath() {
        return accountListFolderPath;
    }

    public void setAccountListFolderPath(String accountListFolderPath) {
        this.accountListFolderPath = accountListFolderPath;
    }

    private ArrayList<XulaATMAccount> readAccountsFrom(String accountListFolderPath) throws FileNotFoundException {
        File userListFolder = new File(accountListFolderPath);
        if(!userListFolder.isDirectory()){
            return new ArrayList<XulaATMAccount>();
        }

        File[] accountFiles = userListFolder.listFiles();
        if(accountFiles == null){
            return new ArrayList<XulaATMAccount>();
        }

        ArrayList<XulaATMAccount> atmAccounts = new ArrayList<XulaATMAccount>();

        for (File accountFile :accountFiles){
            try {

                atmAccounts.add(new XulaATMAccount(accountFile));

            }catch (FileNotFoundException | NoSuchElementException e){

            }
        }

        return atmAccounts;
    }

    public XulaATMAccountList() {
        atmAccounts = new ArrayList<XulaATMAccount>();
    }


    public boolean accountExists(long accountID){
        for (XulaATMAccount atmAccount : atmAccounts){
            if (atmAccount.getAccountId() == accountID){
                return true;
            }
        }

        return false;
    }

    public long getUnusedAccountId() {

        long unusedUserId;
        do {
            unusedUserId = Math.abs(random.nextLong());

        } while (accountExists(unusedUserId));

        return unusedUserId;
    }



    public WithdrawResult withdraw(long fromAccountId, double
            withdrawAmount) {

        return null;
    }

    public DepositResult deposit(long toAccountId, double depositAmount) {

        //check if the Account Exists
        if (!accountExists(toAccountId)){

            return new DepositResult(
                    Result.ERROR_CODE,
                    "Account Doesn't Exists!!"
            );

        }

        return getAccount(toAccountId).deposit(depositAmount);

    }

    public TransferResult transfer(long fromAccountId, long toAccount, double transferAmount) {

        return null;
    }

    public XulaATMAccount getAccount(long accountId){

        for (XulaATMAccount atmAccount : atmAccounts){
            if (atmAccount.getAccountId() == accountId){
                return atmAccount;
            }
        }

        return null;

    }

    public boolean createNewAccount(long accountId, long userId, int accountType, double balance) {

        return atmAccounts.add(new XulaATMAccount(accountId,userId,accountType,balance));

    }

    public void writeTo(String accountListFolderPath) throws IOException {
        for (XulaATMAccount atmAccount :atmAccounts){
            atmAccount.writeTo(accountListFolderPath);
        }
    }

    public ArrayList<Long> getAccountIds(long userId) {

        ArrayList<Long> accountIds = new ArrayList<Long>();

        for (XulaATMAccount atmAccount :atmAccounts){

            if (atmAccount.getUserId()== userId){
                accountIds.add(atmAccount.getAccountId());
            }

        }

        return accountIds;

    }
}
