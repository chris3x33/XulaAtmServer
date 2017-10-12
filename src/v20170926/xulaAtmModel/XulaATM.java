package v20170926.xulaAtmModel;


import v20170926.sha1Utilits.SHA1Utilits;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class XulaATM {

    private XulaATMAccountList atmAccountList;
    private XulaATMUserList atmUserList;
    private final String WELCOME_MSG = "Welcome to XULA ATM";

    public XulaATM(){

        atmAccountList = new XulaATMAccountList();
        atmUserList = new XulaATMUserList();

    }

    public XulaATM(String UserListFolder, String AccountListFolder) throws FileNotFoundException {

        atmAccountList = new XulaATMAccountList(AccountListFolder);
        atmUserList = new XulaATMUserList(UserListFolder);

    }

    public String getWelcomeMsg() {
        return WELCOME_MSG;
    }

    public double getAccountBalance(long accountId) {

        return atmAccountList.getAccountBalance(accountId);

    }

    public ArrayList<Long> getAccountIDs(long userId) {

        return atmUserList.getAccountIDs(userId);

    }

    public boolean userExists(long userId) {

        return atmUserList.userExists(userId);

    }
    public void writeTo(String userListFolderPath, String accountListFolderPath) throws IOException {
        atmUserList.writeTo(userListFolderPath);
        atmAccountList.writeTo(accountListFolderPath);
    }

    public boolean userExists(String userName) {
        return atmUserList.userExists(userName);
    }

    public WithdrawResult withdraw(long fromAccountId, double
            withdrawAmount) {

        return atmAccountList.withdraw(fromAccountId, withdrawAmount);

    }

    public DepositResult deposit(long toAccountId, double depositAmount) {

        return atmAccountList.deposit(toAccountId, depositAmount);
    }

    public TransferResult transfer(long fromAccountId, long toAccountId, double transferAmount) {

        return atmAccountList.transfer(fromAccountId, toAccountId, transferAmount);
    }

    public CreateNewUserResult createNewUser(String username, String password) {

        //Check if username is valid
        Result validUserNameResult = atmUserList.isValidUserName(username);
        boolean isValidUserName = (validUserNameResult.getStatus() == Result.SUCCESS_CODE);
        if (!isValidUserName) {
            return new CreateNewUserResult( validUserNameResult );
        }

        //Check if username is taken
        if (userExists(username)) {
            return new CreateNewUserResult(Result.ERROR_CODE,"UserName already exists");
        }

        //Check if password is usable
        Result isUsablePasswordResult = atmUserList.isUsablePassword(password);
        boolean isUsablePassword = (isUsablePasswordResult.getStatus() == Result.SUCCESS_CODE);

        if (!isUsablePassword) {
            return new CreateNewUserResult( isUsablePasswordResult );
        }

        //Encrypt password
        String encryptedPassword = encrypt(password);

        //Create UserId
        long newUserId = atmUserList.getUnusedUserId();

        //Create Checking Account
        long newCheckingAccountId = atmAccountList.getUnusedAccountId();
        atmAccountList.createNewAccount(newCheckingAccountId,XulaATMAccountType.CHECKING,0);

        //Create Savings Account
        long newSavingsAccountId = atmAccountList.getUnusedAccountId();
        atmAccountList.createNewAccount(newSavingsAccountId,XulaATMAccountType.SAVINGS,0);

        //Create User Account id list
        ArrayList<Long> atmAccountIds = new ArrayList<Long>();
        atmAccountIds.add(newCheckingAccountId);
        atmAccountIds.add(newSavingsAccountId);

        //Create User
        atmUserList.createNewUser(username,encryptedPassword,newUserId,atmAccountIds);

        //Write to Filesystem async
        XulaATMAccount newCheckingAccount = atmAccountList.getAccount(newCheckingAccountId);

        XulaATMAccount newSavingsAccount = atmAccountList.getAccount(newSavingsAccountId);

        XulaATMUser atmUser = atmUserList.getATMUser(newUserId);

        return new CreateNewUserResult(Result.SUCCESS_CODE, newUserId);

    }

    private String encrypt(String password){

        try {

            return new SHA1Utilits().encrypt(password);

        } catch (NoSuchAlgorithmException e) {

            return null;

        }

    }

    public OpenAccountResult openNewAccount(long userId) {

        return null;
    }

    public LoginResult login(String userName, String password){

        //Check if User Exists
        if(!userExists(userName)){

        }

        //Get user
        XulaATMUser atmUser = atmUserList.getATMUser(userName);

        //Check password
        ValidatePasswordResult validatePasswordResult = atmUser.validatePassword(password);

        //Setup LoginResult

        return null;
    }

    public Result changePassword(long userId, String currPass, String newPass){

        return atmUserList.changePassword(userId, currPass, newPass);

    }

}
