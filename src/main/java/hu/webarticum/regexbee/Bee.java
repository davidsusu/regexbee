package hu.webarticum.regexbee;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Pattern;

import hu.webarticum.regexbee.common.LazyFragment;
import hu.webarticum.regexbee.number.IntRangeBuilder;


// TODO:
//
// helper classes by categories (common, number, text, format etc.)
//
// anyOccur ([??], ...*, optionally use QuantifierType)
// positiveOccur ([??], ...+, optionally use QuantifierType)
// boundedOccur
// lookBehind
// lookAhead
// atomic
// backReference ([??])
// recursion ([??])
// dates
// etc.

public final class Bee {

    private static final Pattern GROUP_NAME_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");


    public static final BeeFragment BEGIN = simple("^");

    public static final BeeFragment END = simple("$");

    public static final BeeFragment SIGNED_INT = simple("[\\+\\-]?(?:0|[1-9]\\d*)");

    public static final BeeFragment UNSIGNED_INT = simple("(?:0|[1-9]\\d*)");

    public static final BeeFragment WORD = simple("\\b\\w+\\b");

    public static final BeeFragment FAIL = simple("(?!)");

    public static final BeeFragment JUST_FAIL = concat(BEGIN, FAIL);


    private Bee() {
        // utility class
    }


    public static BeeFragment checked(String pattern) {
        Pattern.compile(pattern);
        return simple(pattern);
    }

    public static BeeFragment checked(String name, String pattern) {
        Pattern.compile(pattern);
        return simple(name, pattern);
    }

    public static BeeFragment simple(String name, String pattern) {
        return named(name, simple(pattern));
    }

    public static BeeFragment simple(String pattern) {
        return new SimpleFragment(pattern);
    }


    public static BeeFragment named(String name, BeeFragment fragment) {
        checkName(name);
        return new LazyFragment(() -> Bee.generateNamed(name, fragment));
    }

    private static void checkName(String name) {
        Objects.requireNonNull(name, "Group name can not be null");
        if (!GROUP_NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(String.format("Invalid group name: '%s'", name));
        }
    }

    private static String generateNamed(String name, BeeFragment fragment) {
        StringBuilder resultBuilder = new StringBuilder("(?<");
        resultBuilder.append(name);
        resultBuilder.append(">");
        resultBuilder.append(fragment.get());
        resultBuilder.append(")");
        return resultBuilder.toString();
    }


    public static BeeFragment concat(BeeFragment... fragments) {
        return concat(Arrays.asList(fragments));
    }

    public static BeeFragment concat(String name, BeeFragment... fragments) {
        return concat(name, Arrays.asList(fragments));
    }

    public static BeeFragment concat(String name, Collection<BeeFragment> fragments) {
        checkName(name);
        return new LazyFragment(() -> Bee.generateConcat(name, fragments));
    }

    public static BeeFragment concat(Collection<BeeFragment> fragments) {
        return new LazyFragment(() -> Bee.generateConcat(null, fragments));
    }

    private static String generateConcat(String name, Collection<BeeFragment> fragments) {
        StringBuilder resultBuilder = new StringBuilder();

        if (name != null) {
            resultBuilder.append("(?<");
            resultBuilder.append(name);
            resultBuilder.append(">");
        }

        for (BeeFragment fragment : fragments) {
            resultBuilder.append(fragment.get());
        }

        if (name != null) {
            resultBuilder.append(")");
        }

        return resultBuilder.toString();
    }


    public static BeeFragment alter(String name, BeeFragment... fragments) {
        return alter(name, Arrays.asList(fragments));
    }

    public static BeeFragment alter(BeeFragment... fragments) {
        return alter(Arrays.asList(fragments));
    }

    public static BeeFragment alter(String name, Collection<BeeFragment> fragments) {
        checkName(name);
        return new LazyFragment(() -> Bee.generateAlter(name, fragments));
    }

    public static BeeFragment alter(Collection<BeeFragment> fragments) {
        return new LazyFragment(() -> Bee.generateAlter(null, fragments));
    }

    private static String generateAlter(String name, Collection<BeeFragment> fragments) {
        StringJoiner joiner = new StringJoiner("|");
        for (BeeFragment fragment : fragments) {
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


    public static BeeFragment fixed(String content) {
        return simple(Pattern.quote(content));
    }


    public static BeeFragment optional(BeeFragment fragment) {
        return optional(fragment, QuantifierType.GREEDY);
    }

    public static BeeFragment optional(String name, BeeFragment fragment) {
        return optional(name, fragment, QuantifierType.GREEDY);
    }

    public static BeeFragment optional(BeeFragment fragment, QuantifierType type) {
        return new LazyFragment(() -> Bee.generateOptional(null, fragment, type));
    }

    public static BeeFragment optional(String name, BeeFragment fragment, QuantifierType type) {
        checkName(name);
        return new LazyFragment(() -> Bee.generateOptional(name, fragment, type));
    }

    private static String generateOptional(String name, BeeFragment fragment, QuantifierType type) {
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


    public static BeeFragment intRange(long min, long until) {
        return intRangeClosed(min, until + 1);
    }

    public static BeeFragment intRangeClosed(long min, long max) {
        return intRange(BigInteger.valueOf(min), true, BigInteger.valueOf(max), true);
    }

    public static BeeFragment intRange(
            BigInteger low, boolean lowInclusive, BigInteger high, boolean highInclusive) {

        return new IntRangeBuilder()
                .low(low, lowInclusive)
                .high(high, highInclusive)
                .build();
    }

}
