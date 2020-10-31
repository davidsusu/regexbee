package hu.webarticum.jrb;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Pattern;

// TODO:
//
// EMAIL
// DATE ([??], format?)
// URL ([??], http[s] only? -> UrlFragmentBuilder)
// etc.
//
// literal (-> escape)
// optional (...?, optionally use QuantifierType)
// anyOccur ([??], ...*, optionally use QuantifierType)
// positiveOccur ([??], ...+, optionally use QuantifierType)
// boundedOccur
// intBetween
// etc.

public class Fragments {
    
    public enum QuantifierType {
        GREEDY, RELUCTANT, POSSESSIVE
    }
    
    
    private static final Pattern GROUP_NAME_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
    

    public static final Fragment BEGIN = simple("^");

    public static final Fragment END = simple("$");
    
    public static final Fragment SIGNED_INT = simple("[\\+\\-]?(?:0|[1-9]\\d*)");
    
    public static final Fragment UNSIGNED_INT = simple("(?:0|[1-9]\\d*)");
    
    public static final Fragment WORD = simple("\\b\\w+\\b");
    
    
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
    
}
