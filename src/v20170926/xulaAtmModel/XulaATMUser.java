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
        final int minLength = 8;
        final int maxLength = 40;

        if (password.length()<minLength || password.length()>maxLength){

        }

        //Must have a lower case character
        //Must have an upper case character
        //Must have a number
        boolean HasUpperCase = false;
        boolean HasLowerCase = false;
        boolean HasNumber = false;

        for(int i=0;i < password.length();i++) {
            char ch = password.charAt(i);
            if (Character.isDigit(ch)) {
                HasNumber = true;
            } else if (Character.isUpperCase(ch)) {
                HasUpperCase = true;
            } else if (Character.isLowerCase(ch)) {
                HasLowerCase = true;
            }
        }

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
