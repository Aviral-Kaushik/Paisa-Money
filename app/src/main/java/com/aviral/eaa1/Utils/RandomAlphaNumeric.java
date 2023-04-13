package com.aviral.eaa1.Utils;

import java.util.Random;

public class RandomAlphaNumeric {
    private final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private final char[] ALPHANUMERIC = (LETTERS + LETTERS.toUpperCase() + "0123456789").toCharArray();
    public String generateAlphaNumeric(int length){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i =0; i<length; i++){
            stringBuilder.append(ALPHANUMERIC[new Random().nextInt(ALPHANUMERIC.length)]);
        }
        return stringBuilder.toString();
    }
}
