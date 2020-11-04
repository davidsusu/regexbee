package hu.webarticum.jrb.number;

import java.math.BigInteger;
import java.util.regex.Pattern;

// TODO: make package-private -> numeric things to separate package
class UnsignedIntBetweenGenerator {
    
    private static final String DIGIT = "\\d";

    private static final String DIGITS = "\\d{%d}";

    private static final String DIGIT_RANGE = "[%d-%d]";

    private static final String DIGIT_RANGE_TWO = "[%d%d]";
    
    private static final Pattern ZEROS_PATTERN = Pattern.compile("0*");
    
    private static final Pattern NINES_PATTERN = Pattern.compile("9*");
    

    // from and to are non-negative, and from <= to
    public String generate(BigInteger from, BigInteger to) {
        String fromStr = from.toString();
        String toStr = to.toString();
        if (fromStr.length() == toStr.length()) {
            return generateSameLength(fromStr, toStr);
        } else {
            return generateDifferentLength(fromStr, toStr);
        }
    }

    private String generateSameLength(String fromStr, String toStr) {
        int length = fromStr.length();
        
        String commonPrefix = longestCommonPrefixOf(fromStr, toStr);
        int commonLength = commonPrefix.length();
        
        if (commonLength == length) {
            return fromStr;
        }
        
        StringBuilder resultBuilder = new StringBuilder(commonPrefix);
        
        int fromNextDigit = fromStr.charAt(commonLength) - '0';
        int toNextDigit = toStr.charAt(commonLength) - '0';

        // TODO: handle 0s and 9s
        if (toNextDigit - fromNextDigit > 2) {
            int minBetween = fromNextDigit + 1;
            int maxBetween = toNextDigit - 1;
            resultBuilder.append(digitBetween(minBetween, maxBetween));
            resultBuilder.append(anyDigitNTimes(length - commonLength - 1));
        }
        System.out.println("fromNextDigit: " + fromNextDigit);
        System.out.println("toNextDigit: " + toNextDigit);
        
        // TODO
        
        return resultBuilder.toString();
    }
    
    private String digitBetween(int min, int max) {
        if (min == max) {
            return Integer.toString(min);
        } else if (max - min == 1) {
            return String.format(DIGIT_RANGE_TWO, min, max);
        } else {
            return String.format(DIGIT_RANGE, min, max);
        }
    }
    
    private String anyDigitNTimes(int n) {
        if (n == 0) {
            return "";
        } else if (n == 1) {
            return DIGIT;
        } else {
            return String.format(DIGITS, n);
        }
    }
    
    private String generateDifferentLength(String fromStr, String toStr) {
        
        // TODO

        return "ddd";
    }
    
    private String longestCommonPrefixOf(String str1, String str2) {
        int commonLength = Math.min(str1.length(), str2.length());
        for (int i = 0; i < commonLength; i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                return str1.substring(0, i);
            }
        }
        return str1.substring(0, commonLength);
    }
    
}
