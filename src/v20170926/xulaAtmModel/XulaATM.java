package v20170926.xulaAtmModel;


import java.io.FileNotFoundException;

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

    public long[] getAccountIDs(long userId) {

        return atmUserList.getAccountIDs(userId);

    }

    public boolean userExists(long userId) {

        return atmUserList.userExists(userId);

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

        return atmUserList.createNewUser(username, password);

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
