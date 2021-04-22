package hu.webarticum.regexbee;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import hu.webarticum.regexbee.common.AlternationFragment;
import hu.webarticum.regexbee.common.NamedBackreferenceFragment;
import hu.webarticum.regexbee.number.IntRangeBuilder;


// TODO:
//
// helper classes by categories (common, number, text, format etc.)
//
// lookBehind
// lookAhead
// atomic
// backReference ([??])
// recursion ([??])
// dates
// etc.

public final class Bee {

    public static final BeeFragment ANYTHING = simple(".*");

    public static final BeeFragment FAIL = simple("(?!)");

    public static final BeeFragment JUST_FAIL = simple("^(?!)");

    public static final BeeFragment BEGIN = simple("^");

    public static final BeeFragment END = simple("$");

    public static final BeeFragment CHARACTER = simple(".");

    public static final BeeFragment WHITESPACE = simple("\\s");
    
    public static final BeeFragment IDENTIFIER = simple("\\b[a-zA-Z_]\\w+\\b");

    public static final BeeFragment ASCII_WORD = simple("\\b\\w+\\b");

    public static final BeeFragment WORD =
            simple("(?<=[^\\p{L}\\p{N}]|^)[\\p{L}\\p{N}]+(?=[^\\p{L}\\p{N}]|$)");

    public static final BeeFragment SIGNED_INT = simple("[\\+\\-]?(?:0|[1-9]\\d*)");

    public static final BeeFragment UNSIGNED_INT = simple("(?:0|[1-9]\\d*)");

    public static final BeeFragment TIMESTAMP =
            simple("\\d{4}\\-\\d{2}\\-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z");


    private Bee() {
        // utility class
    }
    
    
    // to be visually consistent
    public static BeeFragment then(BeeFragment fragment) {
        return fragment;
    }


    public static BeeFragment simple(String pattern) {
        return new SimpleFragment(pattern);
    }

    public static BeeFragment fixed(String content) {
        return simple(Pattern.quote(content));
    }

    public static BeeFragment oneFixedOf(String... contents) {
        return oneFixedOf(Arrays.asList(contents));
    }

    public static BeeFragment oneFixedOf(Collection<String> contents) {
        return new AlternationFragment(
                contents.stream().map(Bee::fixed).collect(Collectors.toList()));
    }
    
    public static BeeFragment checked(String pattern) {
        Pattern.compile(pattern);
        return simple(pattern);
    }
    
    public static BeeFragment ref(String groupName) {
        return new NamedBackreferenceFragment(groupName);
    }
    

    public static BeeFragment intBetween(long min, long max) {
        return intBetween(BigInteger.valueOf(min), true, BigInteger.valueOf(max), true);
    }

    public static BeeFragment intBetween(
            BigInteger low, boolean lowInclusive, BigInteger high, boolean highInclusive) {

        return new IntRangeBuilder()
                .low(low, lowInclusive)
                .high(high, highInclusive)
                .build();
    }

}
