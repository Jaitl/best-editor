package com.jaitlapps.besteditor;

import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class Generator {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(Generator.class);

    public static String generateRandomId() {
        return String.valueOf(UUID.randomUUID());
    }
}
