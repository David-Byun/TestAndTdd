package com.programming.techie.springredditclone.tdd.chap02;

public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {

        if(s == null || s.isEmpty()) return PasswordStrength.INVALID;
        int metCounts = getMetCounts(s);

        if(metCounts <= 1) return PasswordStrength.WEAK;
        if(metCounts == 2) return PasswordStrength.NORMAL;

        return PasswordStrength.STRONG;
    }

    private static int getMetCounts(String s) {
        int metCounts = 0;
        if(s.length() >= 8) metCounts++;
        if(meetsContainingNumberCreiteria(s)) metCounts++;
        if(meetsContainingUppercaseCriteria(s)) metCounts++;
        return metCounts;
    }

    private static boolean meetsContainingUppercaseCriteria(String s) {
        boolean containsUpp = false;
        for (char ch : s.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                containsUpp = true;
                break;
            }
        }
        return containsUpp;
    }

    private static boolean meetsContainingNumberCreiteria(String s) {
        boolean containsNum = false;
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                containsNum = true;
                break;
            }
        }
        return containsNum;
    }
}
