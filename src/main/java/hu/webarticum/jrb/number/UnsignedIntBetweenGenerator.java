package hu.webarticum.jrb.number;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// TODO: develop a non-backtracking version

class UnsignedIntBetweenGenerator {
    
    private static final String DIGIT = "\\d";

    private static final String DIGITS_N = "\\d{%d}";

    private static final String DIGITS_N_M = "\\d{%d,%d}";

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

    private String generateSameLength(String from, String to) {
        int length = from.length();
        
        String commonPrefix = longestCommonPrefixOf(from, to);
        int commonLength = commonPrefix.length();
        
        if (commonLength == length) {
            return from;
        }
        
        
        StringBuilder resultBuilder = new StringBuilder(commonPrefix);
        
        int fromNextDigit = from.charAt(commonLength) - '0';
        int toNextDigit = to.charAt(commonLength) - '0';

        String fromAfterPart = from.substring(commonLength + 1);
        boolean fromZeros = ZEROS_PATTERN.matcher(fromAfterPart).matches();

        String toAfterPart = to.substring(commonLength + 1);
        boolean toNines = NINES_PATTERN.matcher(toAfterPart).matches();

        int minBetween = fromZeros ? fromNextDigit : fromNextDigit + 1;
        int maxBetween = toNines ? toNextDigit : toNextDigit - 1;

        if (!fromZeros || !toNines) {
            resultBuilder.append("(?:");
        }
        
        if (maxBetween >= minBetween) {
            resultBuilder.append(digitBetween(minBetween, maxBetween));
            resultBuilder.append(anyDigitNTimes(length - commonLength - 1));
            if (!fromZeros || !toNines) {
                resultBuilder.append('|');
            }
        }

        if (!fromZeros) {
            resultBuilder.append(fromNextDigit);
            anyFromToNines(fromAfterPart, resultBuilder);
            if (!toNines) {
                resultBuilder.append('|');
            }
        }

        if (!toNines) {
            resultBuilder.append(toNextDigit);
            anyUpToWithLeadingZeros(toAfterPart, resultBuilder);
        }

        if (!fromZeros || !toNines) {
            resultBuilder.append(')');
        }
        
        return resultBuilder.toString();
    }
    
    private String generateDifferentLength(String from, String to) {
        StringBuilder resultBuilder = new StringBuilder();

        int fromLength = from.length();
        int toLength = to.length();

        String toAfterPart = to.substring(1);
        boolean toNines = NINES_PATTERN.matcher(toAfterPart).matches();
        
        int toFirstDigit = to.charAt(0) - '0';
        int toMaxFullDigit = toNines ? toFirstDigit : toFirstDigit - 1;
        
        List<String> branches = new ArrayList<>();

        if (!toNines) {
            StringBuilder mainBranchBuilder = new StringBuilder();
            mainBranchBuilder.append(toFirstDigit);
            anyUpToWithLeadingZeros(toAfterPart, mainBranchBuilder);
            branches.add(mainBranchBuilder.toString());
        }

        if (toMaxFullDigit > 0) {
            StringBuilder trickyVariableLengthBranchBuilder = new StringBuilder();
            trickyVariableLengthBranchBuilder.append(digitBetween(1, toMaxFullDigit));
            trickyVariableLengthBranchBuilder.append(anyDigitNMTimes(fromLength, toLength - 1));
            branches.add(trickyVariableLengthBranchBuilder.toString());
        }

        if (toMaxFullDigit < 9 && toLength > fromLength + 1) {
            StringBuilder normalVariableLengthBranchBuilder = new StringBuilder();
            normalVariableLengthBranchBuilder.append(digitBetween(toMaxFullDigit + 1, 9));
            normalVariableLengthBranchBuilder.append(anyDigitNMTimes(fromLength, toLength - 2));
            branches.add(normalVariableLengthBranchBuilder.toString());
        }

        StringBuilder smallNumberBranchBuilder = new StringBuilder();
        anyFromToNines(from, smallNumberBranchBuilder);
        branches.add(smallNumberBranchBuilder.toString());

        resultBuilder.append(branches.size() > 1 ? String.format("(?:%s)", String.join("|", branches)) : branches.get(0));
        
        return resultBuilder.toString();
    }
    
    private void anyUpToWithLeadingZeros(String to, StringBuilder resultBuilder) {
        int length = to.length();
        int firstDigit = to.charAt(0) - '0';
        
        if (length == 1) {
            resultBuilder.append(digitBetween(0, firstDigit));
            return;
        }
        
        if (firstDigit > 0) {
            resultBuilder.append("(?:");
            resultBuilder.append(digitBetween(0, firstDigit - 1));
            resultBuilder.append(anyDigitNTimes(length - 1));
            resultBuilder.append('|');
        }
        resultBuilder.append(firstDigit);
        anyUpToWithLeadingZeros(to.substring(1), resultBuilder);
        if (firstDigit > 0) {
            resultBuilder.append(')');
        }
    }
    
    private void anyFromToNines(String from, StringBuilder resultBuilder) {
        int length = from.length();
        int firstDigit = from.charAt(0) - '0';

        if (length == 1) {
            resultBuilder.append(digitBetween(firstDigit, 9));
            return;
        }

        if (firstDigit < 9) {
            resultBuilder.append("(?:");
            resultBuilder.append(digitBetween(firstDigit + 1, 9));
            resultBuilder.append(anyDigitNTimes(length - 1));
            resultBuilder.append('|');
        }
        resultBuilder.append(firstDigit);
        anyFromToNines(from.substring(1), resultBuilder);
        if (firstDigit < 9) {
            resultBuilder.append(')');
        }
    }
    
    private String digitBetween(int min, int max) {
        if (min == max) {
            return Integer.toString(min);
        } else if (min == 0 && max == 9) {
            return DIGIT;
        } else if (max - min == 1) {
            return String.format(DIGIT_RANGE_TWO, min, max);
        } else {
            return String.format(DIGIT_RANGE, min, max);
        }
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

    private String anyDigitNMTimes(int n, int m) {
        if (n == m) {
            return anyDigitNTimes(n);
        } else {
            return String.format(DIGITS_N_M, n, m);
        }
    }

    private String anyDigitNTimes(int n) {
        if (n == 0) {
            return "";
        } else if (n == 1) {
            return DIGIT;
        } else {
            return String.format(DIGITS_N, n);
        }
    }
    
}
