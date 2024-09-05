package com.portnum.number.global.utils;

import java.security.SecureRandom;

public class RandomUtils {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
    private static final int LENGTH = 12;
    private static SecureRandom random = new SecureRandom();

    public static String generateRandomCode() {
        StringBuilder builder = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            builder.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return builder.toString();
    }
}
