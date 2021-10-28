package me.zeepic.cardgame.util;

public class Util {

    public static String titleCase(String string) {
        String[] split = string.split(" ");
        StringBuilder finalString = new StringBuilder();
        for (String s : split) {
            finalString
                    .append(s.toUpperCase().charAt(0)) // the first letter of every word is capitalized
                    // (the USSR when it realizes its letters are capitalized)
                    .append(s.toLowerCase().substring(1)) // the rest of the word
                    .append(" ");
        }
        return finalString.toString();
    }

}
