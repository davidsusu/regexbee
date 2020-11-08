package hu.webarticum.jrb.core;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Pattern;

import hu.webarticum.jrb.number.IntBetweenBuilder;


// TODO:
//
// anyOccur ([??], ...*, optionally use QuantifierType)
// positiveOccur ([??], ...+, optionally use QuantifierType)
// boundedOccur
// lookBehind
// lookAhead
// atomic
// backReference ([??])
// recursion ([??])
// etc.

public class Fragments {
    
    private static final Pattern GROUP_NAME_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
    

    public static final Fragment BEGIN = simple("^");

    public static final Fragment END = simple("$");
    
    public static final Fragment SIGNED_INT = simple("[\\+\\-]?(?:0|[1-9]\\d*)");
    
    public static final Fragment UNSIGNED_INT = simple("(?:0|[1-9]\\d*)");
    
    public static final Fragment WORD = simple("\\b\\w+\\b");

    public static final Fragment FAIL = simple("(?!)");
    
    public static final Fragment JUST_FAIL = concat(BEGIN, FAIL);
    
    
    private Fragments() {
        // utility class
    }

    
    public static Fragment checked(String pattern) {
        Pattern.compile(pattern);
        return simple(pattern);
    }

    public static Fragment checked(String name, String pattern) {
        Pattern.compile(pattern);
        return simple(name, pattern);
    }

    public static Fragment simple(String name, String pattern) {
        return named(name, simple(pattern));
    }

    public static Fragment simple(String pattern) {
        return new SimpleFragment(pattern);
    }

    
    public static Fragment named(String name, Fragment fragment) {
        checkName(name);
        return new LazyFragment(() -> Fragments.generateNamed(name, fragment));
    }
    
    private static void checkName(String name) {
        Objects.requireNonNull(name, "Group name can not be null");
        if (!GROUP_NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(String.format("Invalid group name: '%s'", name));
        }
    }

    private static String generateNamed(String name, Fragment fragment) {
        StringBuilder resultBuilder = new StringBuilder("(?<");
        resultBuilder.append(name);
        resultBuilder.append(">");
        resultBuilder.append(fragment.get());
        resultBuilder.append(")");
        return resultBuilder.toString();
    }


    public static Fragment concat(Fragment... fragments) {
        return concat(Arrays.asList(fragments));
    }

    public static Fragment concat(String name, Fragment... fragments) {
        return concat(name, Arrays.asList(fragments));
    }

    public static Fragment concat(String name, Collection<Fragment> fragments) {
        checkName(name);
        return new LazyFragment(() -> Fragments.generateConcat(name, fragments));
    }
    
    public static Fragment concat(Collection<Fragment> fragments) {
        return new LazyFragment(() -> Fragments.generateConcat(null, fragments));
    }
    
    private static String generateConcat(String name, Collection<Fragment> fragments) {
        StringBuilder resultBuilder = new StringBuilder();

        if (name != null) {
            resultBuilder.append("(?<");
            resultBuilder.append(name);
            resultBuilder.append(">");
        }
        
        for (Fragment fragment : fragments) {
            resultBuilder.append(fragment.get());
        }

        if (name != null) {
            resultBuilder.append(")");
        }
        
        return resultBuilder.toString();
    }


    public static Fragment alter(String name, Fragment... fragments) {
        return alter(name, Arrays.asList(fragments));
    }

    public static Fragment alter(Fragment... fragments) {
        return alter(Arrays.asList(fragments));
    }

    public static Fragment alter(String name, Collection<Fragment> fragments) {
        checkName(name);
        return new LazyFragment(() -> Fragments.generateAlter(name, fragments));
    }

    public static Fragment alter(Collection<Fragment> fragments) {
        return new LazyFragment(() -> Fragments.generateAlter(null, fragments));
    }
    
    private static String generateAlter(String name, Collection<Fragment> fragments) {
        StringJoiner joiner = new StringJoiner("|");
        for (Fragment fragment : fragments) {
            joiner.add(fragment.get());
        }

        StringBuilder resultBuilder = new StringBuilder();
        if (name != null) {
            resultBuilder.append("(?<");
            resultBuilder.append(name);
            resultBuilder.append(">");
        } else {
            resultBuilder.append("(?:");
        }
        resultBuilder.append(joiner);
        resultBuilder.append(")");
        
        return resultBuilder.toString();
    }
    
    
    public static Fragment fixed(String content) {
        return simple(Pattern.quote(content));
    }

    
    public static Fragment optional(Fragment fragment) {
        return optional(fragment, QuantifierType.GREEDY);
    }

    public static Fragment optional(String name, Fragment fragment) {
        return optional(name, fragment, QuantifierType.GREEDY);
    }

    public static Fragment optional(Fragment fragment, QuantifierType type) {
        return new LazyFragment(() -> Fragments.generateOptional(null, fragment, type));
    }

    public static Fragment optional(String name, Fragment fragment, QuantifierType type) {
        checkName(name);
        return new LazyFragment(() -> Fragments.generateOptional(name, fragment, type));
    }

    private static String generateOptional(String name, Fragment fragment, QuantifierType type) {
        StringBuilder resultBuilder = new StringBuilder();
        
        if (name != null) {
            resultBuilder.append("(?<");
            resultBuilder.append(name);
            resultBuilder.append(">");
        } else {
            resultBuilder.append("(?:");
        }
        
        resultBuilder.append(fragment.get());
        resultBuilder.append(")?");
        resultBuilder.append(type.modifier());
        
        return resultBuilder.toString();
    }
    

    public static Fragment intBetween(long min, long until) {
        return intBetweenClosed(min, until + 1);
    }
    
    public static Fragment intBetweenClosed(long min, long max) {
        return intBetween(BigInteger.valueOf(min), true, BigInteger.valueOf(max), true);
    }
    
    public static Fragment intBetween(
            BigInteger low, boolean lowInclusive, BigInteger high, boolean highInclusive) {

        return new IntBetweenBuilder()
                .low(low, lowInclusive)
                .high(high, highInclusive)
                .build();
    }
    
}
