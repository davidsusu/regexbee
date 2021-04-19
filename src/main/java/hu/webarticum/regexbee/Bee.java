package hu.webarticum.regexbee;

import java.math.BigInteger;
import java.util.regex.Pattern;

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

    public static final BeeFragment BEGIN = simple("^");

    public static final BeeFragment END = simple("$");

    public static final BeeFragment SIGNED_INT = simple("[\\+\\-]?(?:0|[1-9]\\d*)");

    public static final BeeFragment UNSIGNED_INT = simple("(?:0|[1-9]\\d*)");

    public static final BeeFragment WORD = simple("\\b\\w+\\b");

    public static final BeeFragment FAIL = simple("(?!)");

    public static final BeeFragment JUST_FAIL = BEGIN.then(FAIL);


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
    
    public static BeeFragment checked(String pattern) {
        Pattern.compile(pattern);
        return simple(pattern);
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
