package hu.webarticum.regexbee;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import hu.webarticum.regexbee.character.CharacterClassFragment;
import hu.webarticum.regexbee.character.CharacterFragment;
import hu.webarticum.regexbee.common.AlternationFragment;
import hu.webarticum.regexbee.common.AtomicGroupFragment;
import hu.webarticum.regexbee.common.LookAroundFragment;
import hu.webarticum.regexbee.common.ModifierGroupFragment;
import hu.webarticum.regexbee.common.NamedBackreferenceFragment;
import hu.webarticum.regexbee.number.IntRangeBuilder;
import hu.webarticum.regexbee.util.PatternUtil;


public final class Bee {

    public static final BeeFragment ANYTHING = simple(".*");

    public static final BeeFragment SOMETHING = simple(".+");

    public static final BeeFragment NOTHING = simple("");

    public static final BeeFragment FAIL = simple("(?!)");

    public static final BeeFragment BEGIN = simple("^");

    public static final BeeFragment END = simple("$");

    public static final BeeFragment CHAR = simple(".");

    public static final BeeFragment SPACE = simple(" ");

    public static final BeeFragment TAB = simple("\\t");

    public static final BeeFragment WHITESPACE = simple("\\s");
    
    public static final BeeFragment ASCII_LETTER = simple("[a-zA-Z]");

    public static final BeeFragment ASCII_DIGIT = simple("\\d");

    public static final BeeFragment ASCII_WORD_CHAR = simple("\\w");

    public static final BeeFragment ASCII_WORD_START = simple("(?<!\\w)(?=\\w)");

    public static final BeeFragment ASCII_WORD_END = simple("(?<=\\w)(?!\\w)");

    public static final BeeFragment ASCII_WORD = simple("(?<!\\w)\\w+(?!\\w)");

    public static final BeeFragment DEFAULT_WORD_BOUNDARY = simple("\\b");

    public static final BeeFragment IDENTIFIER = simple("\\b[a-zA-Z_]\\w+\\b");

    public static final BeeFragment LETTER = simple("\\p{L}");

    public static final BeeFragment DIGIT = simple("\\p{N}");

    public static final BeeFragment WORD =
            simple("(?<=[^\\p{L}\\p{N}]|^)[\\p{L}\\p{N}]+(?=[^\\p{L}\\p{N}]|$)");

    public static final BeeFragment UNSIGNED_INT = simple("(?:0|[1-9]\\d*)");

    public static final BeeFragment SIGNED_INT = simple("[\\+\\-]?(?:0|[1-9]\\d*)");

    public static final BeeFragment STRICTLY_SIGNED_INT = simple("[\\+\\-](?:0|[1-9]\\d*)");

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

    public static BeeFragment checked(String pattern) {
        return new SimpleFragment(pattern, true);
    }

    public static CharacterFragment range(char from, char to) {
        return new CharacterClassFragment(from, to);
    }

    public static CharacterFragment range(char from, char to, boolean positive) {
        return new CharacterClassFragment(from, to, positive);
    }

    public static BeeFragment fixed(String content) {
        return simple(PatternUtil.fixedOf(content));
    }

    public static BeeFragment oneFixedOf(String... contents) {
        return oneFixedOf(Arrays.asList(contents));
    }

    public static BeeFragment oneFixedOf(Collection<String> contents) {
        return new AlternationFragment(
                contents.stream().map(Bee::fixed).collect(Collectors.toList()));
    }
    
    public static BeeFragment ref(String groupName) {
        return new NamedBackreferenceFragment(groupName);
    }
    
    public static BeeFragment lookBehind(BeeFragment fragment) {
        return new LookAroundFragment(fragment, LookAroundFragment.Type.BEHIND_POSITIVE);
    }

    public static BeeFragment lookBehindNot(BeeFragment fragment) {
        return new LookAroundFragment(fragment, LookAroundFragment.Type.BEHIND_NEGATIVE);
    }

    public static BeeFragment lookAhead(BeeFragment fragment) {
        return new LookAroundFragment(fragment, LookAroundFragment.Type.AHEAD_POSITIVE);
    }

    public static BeeFragment lookAheadNot(BeeFragment fragment) {
        return new LookAroundFragment(fragment, LookAroundFragment.Type.AHEAD_NEGATIVE);
    }
    
    public static BeeFragment atomic(BeeFragment fragment) {
        return new AtomicGroupFragment(fragment);
    }

    public static BeeFragment with(int switchOn, BeeFragment fragment) {
        return with(switchOn, 0, fragment);
    }

    public static BeeFragment without(int switchOff, BeeFragment fragment) {
        return new ModifierGroupFragment(fragment, 0, switchOff);
    }

    public static BeeFragment with(int switchOn, int switchOff, BeeFragment fragment) {
        return new ModifierGroupFragment(fragment, switchOn, switchOff);
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
