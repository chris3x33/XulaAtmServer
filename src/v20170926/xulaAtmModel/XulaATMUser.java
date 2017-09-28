package v20170926.xulaAtmModel;

public class XulaATMUser {

    private String userName;
    private String password;
    private long userId;
    private long[] atmAccountIds;

    public ValidatePasswordResult validatePassword( String passwordTovalidate){

        return null;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public long getUserId() {
        return userId;
    }

    public long[] getAtmAccountIds() {
        return atmAccountIds;
    }
}
