package v20170926.xulaAtmModel;

import v20170926.sha1Utilits.SHA1Utilits;

import java.security.NoSuchAlgorithmException;

public class XulaATMUser {

    private String userName;
    private String password;
    private long userId;
    private long[] atmAccountIds;

    public ValidatePasswordResult validatePassword( String passwordToValidate){

        //encrypt passwordToValidate
        String encryptedPassword = encrypt(password);

        //check against User password

        return null;
    }

    public Result isUsablePassword(String password){

        //Password needs to be at least 8 characters long
        //Can not be more than 40 characters long
        //Must have a lower case character
        //Must have an upper case character
        //Must have a number


        return null;
    }

    private String encrypt(String password){

        try {

            return new SHA1Utilits().encrypt(password);

        } catch (NoSuchAlgorithmException e) {

            return null;

        }

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
