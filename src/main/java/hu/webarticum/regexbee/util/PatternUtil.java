package hu.webarticum.regexbee.util;

import java.util.regex.Pattern;

public final class PatternUtil {
    
    private static final Pattern ATOMIC_METAPATTERN = Pattern.compile(
            "[^\\|]|\\\\.|\\([^\\)]*\\)");
    
    private static final Pattern SAFE_METAPATTERN = Pattern.compile(
            "(?:\\\\.|[^\\|])*");
    
    
    private PatternUtil() {
        // utility class
    }
    

    public static boolean isSafe(String pattern) {
        return (isAtomic(pattern) || isNonAtomicSafe(pattern));
    }

    public static boolean isNonAtomicSafe(String nonAtomicpattern) {
        return SAFE_METAPATTERN.matcher(nonAtomicpattern).matches();
    }

    public static boolean isAtomic(String pattern) {
        return ATOMIC_METAPATTERN.matcher(pattern).matches();
    }

    public static String wrap(String pattern) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("(?:");
        resultBuilder.append(pattern);
        resultBuilder.append(")");
        return resultBuilder.toString();
    }
    
}
