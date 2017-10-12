package v20170926.xulaAtmModel;

import v20170926.sha1Utilits.SHA1Utilits;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

public class XulaATMUser {

    private String userName;
    private String password;
    private long userId;
    private ArrayList<Long> atmAccountIds;

    public XulaATMUser(String userName, String password, long userId, ArrayList<Long> atmAccountIds) {

        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.atmAccountIds = atmAccountIds;

    }

    public XulaATMUser(File userFile) throws FileNotFoundException {

        atmAccountIds = new ArrayList<Long>();

        readUserFrom(userFile);

    }

    public void readUserFrom(File userFile) throws FileNotFoundException {

        Scanner scanner = new Scanner(userFile);

        //Read UserId
        userId = scanner.nextLong();
        scanner.nextLine();

        //Read userName
        userName = scanner.nextLine();

        //Read password
        password = scanner.nextLine();

        //Read Accounts
        while (scanner.hasNextLine()){
            scanner.nextLong();
            scanner.nextLine();
        }

    }

    public ValidatePasswordResult validatePassword( String passwordToValidate){

        //encrypt passwordToValidate
        String encryptedPassword = encrypt(password);

        //check against User password

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

    public ArrayList<Long> getAtmAccountIds() {
        return atmAccountIds;
    }
}
