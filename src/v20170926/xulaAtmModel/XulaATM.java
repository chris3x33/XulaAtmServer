package v20170926.xulaAtmModel;


import v20170926.sha1Utilits.SHA1Utilits;
import v20170926.xulaAtmModel.result.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class XulaATM {

    private XulaATMAccountList atmAccountList;
    private XulaATMUserList atmUserList;
    private XulaATMTransactionList atmTransactionList;

    private final String WELCOME_MSG = "Welcome to XULA ATM";

    private final String USERLIST_FOLDER;
    private final String ACCOUNTLIST_FOLDER;
    private final String TRANSACTIONLIST_FOLDER;

    public XulaATM(
            String userListFolder, String accountListFolder,
            String transactionListFolder) throws FileNotFoundException {

        this.USERLIST_FOLDER = userListFolder;
        this.ACCOUNTLIST_FOLDER = accountListFolder;
        this.TRANSACTIONLIST_FOLDER = transactionListFolder;
        atmAccountList = new XulaATMAccountList(accountListFolder);
        atmUserList = new XulaATMUserList(userListFolder);

    }

    public String getUserListFolder() {
        return USERLIST_FOLDER;
    }

    public String getAccountListFolder() {
        return ACCOUNTLIST_FOLDER;
    }

    public String getWelcomeMsg() {
        return WELCOME_MSG;
    }

    public GetAccountBalanceResult getAccountBalance(long accountId) {

        return atmAccountList.getAccountBalance(accountId);

    }

    public GetAccountIdsResult getAccountIDs(long userId) {

        //Check if User Exists
        if(!userExists(userId)){
            return new GetAccountIdsResult(
                    Result.ERROR_CODE,
                    "User Doesn't Exists!!"
            );
        }

        //Get User
        XulaATMUser atmUser = atmUserList.getATMUser(userId);

        //Get AtmAccountIds
        ArrayList<Long> accountIDs = atmUserList.getAccountIDs(userId);

        GetAccountIdsResult getAccountIdsResult = new GetAccountIdsResult(
                Result.SUCCESS_CODE,
                accountIDs
        );

        return getAccountIdsResult;

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

    public DepositResult deposit(long userId, long toAccountId, double depositAmount) {

        //Check if User Exists
        if(!userExists(userId)){
            return new DepositResult(
                    Result.ERROR_CODE,
                    "User Doesn't Exists!!"
            );
        }

        //Get User
        XulaATMUser atmUser = atmUserList.getATMUser(userId);

        //Get AtmAccountIds
        ArrayList<Long> atmAccountIds = atmUser.getAtmAccountIds();

        //check if the Account belongs to the user
        if (!atmAccountIds.contains(toAccountId)){
            return new DepositResult(
                    Result.ERROR_CODE,
                    "Account Access Denied!!"
            );
        }

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
        atmAccountList.createNewAccount(
                newCheckingAccountId,
                newUserId,
                XulaATMAccountType.CHECKING,
                0
        );

        //Create Savings Account
        long newSavingsAccountId = atmAccountList.getUnusedAccountId();
        atmAccountList.createNewAccount(
                newSavingsAccountId,
                newUserId,
                XulaATMAccountType.SAVINGS,
                0
        );

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
            return new LoginResult(
                    Result.ERROR_CODE,
                    "User Doesn't Exists!!"
            );
        }

        //Get user
        XulaATMUser atmUser = atmUserList.getATMUser(userName);

        //Check if User is Activated
        if(!atmUser.isActivated()){
            return new LoginResult(
                    Result.ERROR_CODE,
                    "User Deactivated!!"
            );
        }

        //Check password
        ValidatePasswordResult validatePasswordResult = atmUser.validatePassword(password);
        boolean isPasswordValid = (validatePasswordResult.getStatus() == Result.SUCCESS_CODE);
        if(!isPasswordValid){
            return new LoginResult(
                    validatePasswordResult.getStatus(),
                    validatePasswordResult.getMessage()
            );
        }

        return new LoginResult(Result.SUCCESS_CODE,atmUser.getUserId());
    }

    public Result changePassword(long userId, String currPass, String newPass){

        return atmUserList.changePassword(userId, currPass, newPass);

    }

    public GetUserNameResult getUserName(long userId) {

        String userName;

        boolean userExists = atmUserList.userExists(userId);
        if (!userExists){
            return new GetUserNameResult(
                    Result.ERROR_CODE,
                    "User Doesn't Exists!!"
            );
        }

        XulaATMUser atmUser = atmUserList.getATMUser(userId);
        if (atmUser == null){
            return new GetUserNameResult(
                    Result.ERROR_CODE,
                    "Error finding User!!"
            );
        }

        userName = atmUser.getUserName();
        if(userName==null || userName.isEmpty()){
            return new GetUserNameResult(
                    Result.ERROR_CODE,
                    "Error userName missing!!"
            );
        }


        GetUserNameResult getUserNameResult = new GetUserNameResult(Result.SUCCESS_CODE);
        getUserNameResult.setUserName(userName);

        return getUserNameResult;

    }

}

