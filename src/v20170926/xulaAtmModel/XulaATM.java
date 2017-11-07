package v20170926.xulaAtmModel;


import v20170926.sha1Utilits.SHA1Utilits;
import v20170926.xulaAtmModel.result.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static v20170926.utils.DateTime.getCurrentDate;

public class XulaATM {

    private XulaATMAccountList atmAccountList;
    private XulaATMUserList atmUserList;
    private XulaATMTransactionList atmTransactionList;

    private final String WELCOME_MSG = "Welcome to XULA ATM";

    public XulaATM(
            String userListFolder, String accountListFolder,
            String transactionListFolder) throws FileNotFoundException {


        atmAccountList = new XulaATMAccountList(accountListFolder);
        atmUserList = new XulaATMUserList(userListFolder);
        atmTransactionList = new XulaATMTransactionList(transactionListFolder);
    }

    public String getUserListFolder() {
        return atmUserList.getUserListFolder();
    }

    public String getAccountListFolder() {
        return atmAccountList.getAccountListFolderPath();
    }
    public String getTransactionListFolder() {

        return atmTransactionList.getTransactionListFolder();
    }

    public String getWelcomeMsg() {
        return WELCOME_MSG;
    }

    public GetAccountBalanceResult getAccountBalance(long userId ,long accountId) {

        //Check if User Exists
        if(!userExists(userId)){
            return new GetAccountBalanceResult(
                    Result.ERROR_CODE,
                    "User Doesn't Exists!!"
            );
        }

        //Check if Account Exists
        if(!atmAccountList.accountExists(accountId)){
            return new GetAccountBalanceResult(
                    Result.ERROR_CODE,
                    "Account Doesn't Exists!!"
            );
        }

        //Get atmAccount
        XulaATMAccount atmAccount = atmAccountList.getAccount(accountId);

        //check if the Account belongs to the user
        if (atmAccount.getUserId() != userId){

            return new GetAccountBalanceResult(
                    Result.ERROR_CODE,
                    "Account Access Denied!!"
            );

        }
        return new GetAccountBalanceResult(Result.SUCCESS_CODE, atmAccount.getBalance());

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
        ArrayList<Long> accountIDs = atmAccountList.getAccountIds(userId);

        GetAccountIdsResult getAccountIdsResult = new GetAccountIdsResult(
                Result.SUCCESS_CODE,
                accountIDs
        );

        return getAccountIdsResult;

    }

    public GetTransactionIdsResult getTransactionIds(long userId, long accountId) {

        //Check if User Exists
        if(!userExists(userId)){
            return new GetTransactionIdsResult(
                    Result.ERROR_CODE,
                    "User Doesn't Exists!!"
            );
        }

        //Get User
        XulaATMUser atmUser = atmUserList.getATMUser(userId);

        //Check if Account Exists
        if(!atmAccountList.accountExists(accountId)){
            return new GetTransactionIdsResult(
                    Result.ERROR_CODE,
                    "Account Doesn't Exists!!"
            );
        }

        //Get atmAccount
        XulaATMAccount atmAccount = atmAccountList.getAccount(accountId);

        //check if the Account belongs to the user
        if (atmAccount.getUserId() != userId){

            return new GetTransactionIdsResult(
                    Result.ERROR_CODE,
                    "Account Access Denied!!"
            );

        }

        //Get transactionIds
        ArrayList<Long> transactionIds = atmTransactionList.getTransactionIds(accountId);

        GetTransactionIdsResult getTransactionIdsResult = new GetTransactionIdsResult(
                Result.SUCCESS_CODE,
                transactionIds
        );

        return getTransactionIdsResult;

    }

    public GetTransactionResult getTransaction(
            long userId, long accountId, long transactionId) {

        //Check if User Exists
        if(!userExists(userId)){
            return new GetTransactionResult(
                    Result.ERROR_CODE,
                    "User Doesn't Exists!!"
            );
        }

        //Check if Account Exists
        if(!atmAccountList.accountExists(accountId)){
            return new GetTransactionResult(
                    Result.ERROR_CODE,
                    "Account: "+accountId+" Doesn't Exists!!"
            );
        }

        //Get atmAccount
        XulaATMAccount atmAccount = atmAccountList.getAccount(accountId);

        //check if the Account belongs to the user
        if (atmAccount.getUserId() != userId){

            return new GetTransactionResult(
                    Result.ERROR_CODE,
                    "Account Access Denied!!"
            );

        }

        //Check if Transaction Exists
        if(!atmTransactionList.transactionExists(accountId, transactionId)){
            return new GetTransactionResult(
                    Result.ERROR_CODE,
                    "Transaction: "+transactionId+" Doesn't Exists!!"
            );
        }

        //Get Transaction
        XulaATMTransaction atmTransaction =
                atmTransactionList.getTransaction(accountId, transactionId);

        return new GetTransactionResult(
                Result.SUCCESS_CODE,
                atmTransaction
        );
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

    public WithdrawResult withdraw(
            long userId, long fromAccountId, double withdrawAmount) {

        //Check if User Exists
        if(!userExists(userId)){
            return new WithdrawResult(
                    Result.ERROR_CODE,
                    "User Doesn't Exists!!"
            );
        }

        //Get User
        XulaATMUser atmUser = atmUserList.getATMUser(userId);

        //Check if Account Exists
        if(!atmAccountList.accountExists(fromAccountId)){
            return new WithdrawResult(
                    Result.ERROR_CODE,
                    "Account Doesn't Exists!!"
            );
        }

        //Get atmAccount
        XulaATMAccount atmAccount = atmAccountList.getAccount(fromAccountId);

        //check if the Account belongs to the user
        if (atmAccount.getUserId() != userId){

            return new WithdrawResult(
                    Result.ERROR_CODE,
                    "Account Access Denied!!"
            );

        }

        //Get balance before withdraw
        double balanceBeforeWithdraw = atmAccount.getBalance();

        WithdrawResult withdrawResult
                = atmAccount.withdraw(withdrawAmount);

        if (withdrawResult.getStatus() == Result.ERROR_CODE){
            return withdrawResult;
        }

        //Save transaction
        long newTransactionId = atmTransactionList.getUnusedTransactionId(fromAccountId);
        atmTransactionList.recordTransaction(
                fromAccountId,
                newTransactionId,
                withdrawAmount,
                XulaATMTransactionType.WITHDRAW,
                "CASH",
                balanceBeforeWithdraw,
                getCurrentDate()
        );

        //Update Account in filesystem
        atmAccount.writeToAsync(atmAccountList.getAccountListFolderPath());

        String withdrawMsg = String.format(
                "Successfully Withdrawn $%.2f from Account: %d",
                withdrawAmount,
                fromAccountId
        );

        withdrawResult.setWithdrawMsg(withdrawMsg);

        return withdrawResult;

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

        //Check if Account Exists
        if(!atmAccountList.accountExists(toAccountId)){
            return new DepositResult(
                    Result.ERROR_CODE,
                    "Account Doesn't Exists!!"
            );
        }

        //Get atmAccount
        XulaATMAccount atmAccount = atmAccountList.getAccount(toAccountId);

        //check if the Account belongs to the user
        if (atmAccount.getUserId() != userId){

            return new DepositResult(
                    Result.ERROR_CODE,
                    "Account Access Denied!!"
            );

        }

        //Get balance before deposit
        double balanceBeforeDeposit = atmAccount.getBalance();

        DepositResult depositResult = atmAccount.deposit(depositAmount);

        if (depositResult.getStatus() == Result.ERROR_CODE){
            return depositResult;
        }

        //Save transaction
        long newTransactionId = atmTransactionList.getUnusedTransactionId(toAccountId);
        atmTransactionList.recordTransaction(
                toAccountId,
                newTransactionId,
                depositAmount,
                XulaATMTransactionType.DEPOSIT,
                "CASH",
                balanceBeforeDeposit,
                getCurrentDate()
        );

        //Update Account in filesystem
        atmAccount.writeToAsync(atmAccountList.getAccountListFolderPath());

        String depositMsg = String.format(
                "Successfully Deposited $%.2f into Account: %d",
                depositAmount,
                toAccountId
        );

        depositResult.setDepositMsg(depositMsg);

        return depositResult;
    }

    public TransferResult transfer(long fromAccountId, long toAccountId, double transferAmount) {

        return null;
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

        //Create User
        atmUserList.createNewUser(username,encryptedPassword,newUserId);

        //Write to Filesystem async
        XulaATMAccount newCheckingAccount = atmAccountList.getAccount(newCheckingAccountId);
        newCheckingAccount.writeToAsync(atmAccountList.getAccountListFolderPath());

        XulaATMAccount newSavingsAccount = atmAccountList.getAccount(newSavingsAccountId);
        newSavingsAccount.writeToAsync(atmAccountList.getAccountListFolderPath());

        XulaATMUser atmUser = atmUserList.getATMUser(newUserId);
        atmUser.writeToAsync(atmUserList.getUserListFolder());

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

