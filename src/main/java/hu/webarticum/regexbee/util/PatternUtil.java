package hu.webarticum.regexbee.util;

import java.util.regex.Pattern;

public final class PatternUtil {
    
    private static final Pattern GROUP_NAME_PATTERN = Pattern.compile(
            "[a-zA-Z][a-zA-Z0-9]*");
    
    private static final Pattern ATOMIC_METAPATTERN = Pattern.compile(
            "[^\\|]|\\\\.|\\([^\\)]*\\)");
    
    private static final Pattern SAFE_METAPATTERN = Pattern.compile(
            "(?:\\\\.|[^\\|])*");
    
    
    private PatternUtil() {
        // utility class
    }
    

    public static String requireValidGroupName(String groupName) {
        if (!isValidGroupName(groupName)) {
            throw new IllegalArgumentException("Invalid group name: " + groupName);
        }
        
        return groupName;
    }
    
    public static boolean isValidGroupName(String groupName) {
        return GROUP_NAME_PATTERN.matcher(groupName).matches();
    }
    
    public static boolean isSafePattern(String pattern) {
        return (isAtomicPattern(pattern) || isSafeNonAtomicPattern(pattern));
    }

    public static boolean isSafeNonAtomicPattern(String nonAtomicpattern) {
        return SAFE_METAPATTERN.matcher(nonAtomicpattern).matches();
    }

    public static boolean isAtomicPattern(String pattern) {
        return ATOMIC_METAPATTERN.matcher(pattern).matches();
    }

    public static String wrapPattern(String pattern) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("(?:");
        resultBuilder.append(pattern);
        resultBuilder.append(")");
        return resultBuilder.toString();
    }
    
}
