package com.jaitlapps.besteditor;

import org.slf4j.LoggerFactory;

import java.util.UUID;

public class Generator {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(Generator.class);

    public static String generateRandomId() {
        return String.valueOf(UUID.randomUUID());
    }
}
