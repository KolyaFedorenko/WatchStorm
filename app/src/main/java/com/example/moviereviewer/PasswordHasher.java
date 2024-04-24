package com.example.moviereviewer;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHasher {

    public static String generatePasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec keySpec = new PBEKeySpec(chars, salt, 10, 64*8);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); // lgtm [java/weak-cryptographic-algorithm]

        byte[] hash = keyFactory.generateSecret(keySpec).getEncoded();
        return toHex(salt) + toHex(hash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException{
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException{
        BigInteger bigInteger = new BigInteger(1, array);
        String hex = bigInteger.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0){
            return String.format("%0" + paddingLength + "d", 0) + hex;
        }
        else{
            return hex;
        }
    }

    public static boolean validatePassword(String passwordToCheck, String passwordFromDB) throws NoSuchAlgorithmException, InvalidKeySpecException{

        byte[] salt = fromHex(passwordFromDB.substring(0,32));
        byte[] hash = fromHex(passwordFromDB.substring(32, 156));

        PBEKeySpec keySpec = new PBEKeySpec(passwordToCheck.toCharArray(), salt, 10, hash.length * 8);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); // lgtm [java/weak-cryptographic-algorithm]
        byte[] testHash = keyFactory.generateSecret(keySpec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}