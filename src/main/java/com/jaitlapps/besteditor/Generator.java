package com.jaitlapps.besteditor;

import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

public class Generator {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(Generator.class);

    public static String generateRandomId() {
        byte[] dateBytes = null;

        try {
            dateBytes = new Date().toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("error", e);
        }

        byte[] randomBytes = null;

        try {
            randomBytes = Double.toHexString(Math.random()).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("error", e);
        }

        byte[] finalBytes = concat(dateBytes, randomBytes);

        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error("error", e);
        }

        byte[] randomId = messageDigest.digest(finalBytes);

        String hexId = byteArrayToHex(randomId);
        log.info("generated random id: " + hexId);

        return hexId;
    }

    private static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);

        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));

        return sb.toString();
    }

    private static byte[] concat(byte[] arr1, byte[] arr2) {
        byte[] result = Arrays.copyOf(arr1, arr1.length + arr2.length);
        System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
        return result;
    }
}
