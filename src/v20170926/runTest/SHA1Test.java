package v20170926.runTest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class SHA1Test {

    private final static Scanner IN = new Scanner(System.in);

    public static void main(String[] args) throws NoSuchAlgorithmException {

        //Output Instruction
        System.out.print("Enter password to encrypt: ");

        //Get Password from user
        String password = IN.nextLine();

        //Encrypt password
        String encryptedPassword = encrypt(password);

        //Output Encrypted password
        System.out.println("Encrypted password: "+encryptedPassword);

    }

    private static String encrypt(String password) throws NoSuchAlgorithmException {

        //Set encrypt type
        final String encryptType = "SHA-1";

        //initialize encryption
        MessageDigest encryptor = MessageDigest.getInstance(encryptType);
        encryptor.reset();

        //get password bytes
        byte[] passwordBytes = password.getBytes();

        //encrypt password bytes
        byte[] encryptedBytes = encryptor.digest(passwordBytes);

        //convert bytes to hex
        return toHexString(encryptedBytes);
    }

    private static String toHexString(byte[] bytes){

        StringBuilder hexStringBuilder = new StringBuilder();

        for (byte b : bytes){
            hexStringBuilder.append(
                    String.format("%02x", b)
            );
        }

        return hexStringBuilder.toString();

    }

}
