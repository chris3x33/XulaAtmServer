package v20170926.xulaAtmModel;

import v20170926.sha1Utilits.SHA1Utilits;
import v20170926.xulaAtmModel.result.Result;
import v20170926.xulaAtmModel.result.ValidatePasswordResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class XulaATMUser {

    private String userName;
    private String password;
    private long userId;
    private boolean isActivated;
    private ArrayList<Long> atmAccountIds;

    public XulaATMUser(String userName, String password, long userId) {

        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.isActivated=true;
    }

    public XulaATMUser(
            String userName, String password, long userId,
            boolean isActivated) {

        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.isActivated = isActivated;

    }

    public XulaATMUser(File userFile) throws FileNotFoundException {

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

        //Read isActivated
        isActivated = scanner.nextBoolean();


    }

    public ValidatePasswordResult validatePassword(String passwordToValidate){

        //encrypt passwordToValidate
        String encryptedPassword = encrypt(passwordToValidate);

        //check against User password
        if(!encryptedPassword.equals(password)){
            return new ValidatePasswordResult(Result.ERROR_CODE, "Incorrect Password!!");
        }

        return new ValidatePasswordResult(Result.SUCCESS_CODE);
    }

    private String encrypt(String password){

        try {

            return new SHA1Utilits().encrypt(password);

        } catch (NoSuchAlgorithmException e) {

            return null;

        }

    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
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

    public boolean writeTo(String userListFolderPath) throws IOException {
        File userListFolder = new File(userListFolderPath);

        if (!userListFolder.isDirectory()){return false;}

        File userFile = new File(userListFolderPath+"\\"+userId+".txt");
        userFile.createNewFile();

        PrintWriter out = new PrintWriter(userFile);

        out.println(userId);
        out.println(userName);
        out.println(password);
        out.println(isActivated);

        out.close();

        return true;
    }

    @Override
    public String toString() {

        return  userId + " , " +
                userName + " , " +
                password + " , " +
                isActivated;

    }

    public static XulaATMUser parse(String str) {

        try {

            Scanner parser = new Scanner(str);

            //Read UserId
            long userId = parser.nextLong();
            parser.next();

            //Read userName
            String userName = parser.next();
            parser.next();

            //Read password
            String password = parser.next();
            parser.next();

            //Read isActivated
            boolean isActivated = parser.nextBoolean();

            return new XulaATMUser(

                    userName,
                    password,
                    userId,
                    isActivated
            );

        } catch (InputMismatchException e){
            return null;
        }catch (NoSuchElementException e){
            return null;
        }

    }
}
