package org.group_b.Database;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncodeMD5 {

    String hashedText;

    public EncodeMD5(String password) throws NoSuchAlgorithmException {
        this.hashedText = MD5(password);
    }

    private String MD5(String password) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing MD5
        MessageDigest md = MessageDigest.getInstance("MD5");

        // Calculate message digest of an input digest() return array of byte
        byte[] messageDigest = md.digest(password.getBytes());

        // Convert byte array into "signum" representation
        BigInteger no = new BigInteger(1, messageDigest);

        // Convert message digest into hex value
        StringBuilder hashText = new StringBuilder(no.toString(16));

        while (hashText.length() < 32) {
            hashText.insert(0, "0");
        }

        return hashText.toString();
    }

}

