package hu.webarticum.regexbee;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import hu.webarticum.regexbee.character.CharacterRangeFragment;
import hu.webarticum.regexbee.character.CompoundCharacterFragment;
import hu.webarticum.regexbee.character.CharacterFragment;
import hu.webarticum.regexbee.character.FixedCharacterFragment;
import hu.webarticum.regexbee.character.PredefinedCharacterFragment;
import hu.webarticum.regexbee.common.AlternationFragment;
import hu.webarticum.regexbee.common.AtomicGroupFragment;
import hu.webarticum.regexbee.common.LookAroundFragment;
import hu.webarticum.regexbee.common.ModifierGroupFragment;
import hu.webarticum.regexbee.common.NamedBackreferenceFragment;
import hu.webarticum.regexbee.number.IntRangeBuilder;
import hu.webarticum.regexbee.template.BeeTemplateParameter;
import hu.webarticum.regexbee.util.PatternUtil;


public final class Bee {

    /** Any character (except newline if {@link Pattern#DOTALL} is off) */
    public static final CharacterFragment CHAR = PredefinedCharacterFragment.ANY;

    /** Any character, including newline */
    public static final CharacterFragment CHAR_THROUGH_LINES = new CompoundCharacterFragment(
            PredefinedCharacterFragment.WHITESPACE,
            PredefinedCharacterFragment.NON_WHITESPACE);

    /** Zero or more characters, see {@link Bee#CHAR} */
    public static final BeeFragment ANYTHING = CHAR.any();

    /** Zero or more characters, including newline, see {@link Bee#CHAR_THROUGH_LINES} */
    public static final BeeFragment ANYTHING_THROUGH_LINES = CHAR_THROUGH_LINES.any();

    /** One or more characters, see {@link Bee#CHAR} */
    public static final BeeFragment SOMETHING = CHAR.more();

    /** One or more characters, including newline, see {@link Bee#CHAR_THROUGH_LINES} */
    public static final BeeFragment SOMETHING_THROUGH_LINES = CHAR_THROUGH_LINES.more();
    
    /** Nothing (empty string without restrictions) */
    public static final BeeFragment NOTHING = simple("");

    /** Fail, causes backtrack (potentially global fail) immediately */
    public static final BeeFragment FAIL = simple("(?!)");

    /**
     * Matches at begin of the input
     * (or begin of the current line, if {@link Pattern#MULTILINE} is on)
     */
    public static final BeeFragment BEGIN = simple("^");

    /**
     * Matches at end of the input
     * (or end of the current line, if {@link Pattern#MULTILINE} is on)
     */
    public static final BeeFragment END = simple("$");

    /** Matches at begin of the entire input, even if {@link Pattern#MULTILINE} is on */
    public static final BeeFragment GLOBAL_BEGIN = simple("\\A"); // TODO: test

    /** Matches at end of the entire input, even if {@link Pattern#MULTILINE} is on */
    public static final BeeFragment GLOBAL_END = simple("\\Z"); // TODO: test

    /** Any whitespace character, e. g. space, tab and newline */
    public static final CharacterFragment WHITESPACE = PredefinedCharacterFragment.WHITESPACE;

    /** The space character */
    public static final CharacterFragment SPACE = fixedChar(' ');

    /** The backslash character */
    public static final CharacterFragment BACKSLASH = fixedChar('\\');

    /** The tab character */
    public static final CharacterFragment TAB = fixedChar('\t');

    /** The newline character */
    public static final CharacterFragment NEWLINE = fixedChar('\n');

    /** Any ASCII letter: [a-zA-Z] */
    public static final CharacterFragment ASCII_LETTER = CharacterRangeFragment.builder()
            .withPositiveMatching()
            .addRange('a', 'z')
            .addRange('A', 'Z')
            .build();

    /** Any lower-case ASCII letter: [a-z] */
    public static final CharacterFragment ASCII_LOWERCASE_LETTER = range('a', 'z');

    /** Any upper-case ASCII letter: [A-Z] */
    public static final CharacterFragment ASCII_UPPERCASE_LETTER = range('A', 'Z');

    /** Any ASCII digit: [0-9] */
    public static final CharacterFragment ASCII_DIGIT = PredefinedCharacterFragment.ASCII_DIGIT;

    /** Any ASCII word character: [_a-zA-Z0-9] */
    public static final CharacterFragment ASCII_WORD_CHAR = PredefinedCharacterFragment.ASCII_WORD_CHAR;

    /** Any letter according to the Unicode standard */
    public static final CharacterFragment LETTER = PredefinedCharacterFragment.UNICODE_LETTER;

    /** Any numeric character according to the Unicode standard */
    public static final CharacterFragment DIGIT = PredefinedCharacterFragment.UNICODE_DIGIT;

    /** Left boundary of a sequence of {@link Bee#ASCII_WORD_CHAR}s */
    public static final BeeFragment ASCII_WORD_START = simple("(?<!\\w)(?=\\w)");

    /** Right boundary of a sequence of {@link Bee#ASCII_WORD_CHAR}s */
    public static final BeeFragment ASCII_WORD_END = simple("(?<=\\w)(?!\\w)");

    /** Boundary of a word (default) */
    public static final BeeFragment DEFAULT_WORD_BOUNDARY = simple("\\b");

    /** Full sequence of {@link Bee#ASCII_WORD_CHAR}s */
    public static final BeeFragment ASCII_WORD = simple("(?<!\\w)\\w+(?!\\w)");

    /** Full sequence of Unicode letters and numbers */
    public static final BeeFragment WORD =
            simple("(?<=[^\\p{L}\\p{N}]|^)[\\p{L}\\p{N}]+(?=[^\\p{L}\\p{N}]|$)");

    /** An ASCII-only Unicode word that does not start with a digit */
    public static final BeeFragment IDENTIFIER = simple("(?<=[^\\p{L}\\p{N}]|^)\\b[a-zA-Z_]\\w+(?=[^\\p{L}\\p{N}]|$)");

    /** Unsigned integer, e. g. <code>12</code> or <code>594</code> (no boundary included) */
    public static final BeeFragment UNSIGNED_INT = simple("(?:0|[1-9]\\d*)");

    /**
     * Optionally signed integer,
     * e. g. <code>7</code>, <code>+37</code> or <code>-55</code> (no boundary included)
     */
    public static final BeeFragment SIGNED_INT = simple("[\\+\\-]?(?:0|[1-9]\\d*)");

    /**
     * Strictly signed integer (sign is required),
     * e. g. <code>+8</code> or <code>-12</code> (no boundary included)
     */
    public static final BeeFragment STRICTLY_SIGNED_INT = simple("[\\+\\-](?:0|[1-9]\\d*)");

    /** An ISO 8601 UTC timestamp */
    public static final BeeFragment TIMESTAMP =
            simple("\\d{4}\\-\\d{2}\\-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z");


    private Bee() {
        // utility class
    }
    
    
    // to be visually consistent
    public static BeeFragment then(BeeFragment fragment) {
        return fragment;
    }


    public static BeeTemplateParameter placeholder() {
        return new BeeTemplateParameter();
    }

    public static BeeTemplateParameter placeholder(String name) {
        return new BeeTemplateParameter(name);
    }
    
    public static BeeFragment simple(String pattern) {
        return new SimpleFragment(pattern);
    }

    public static BeeFragment checked(String pattern) {
        return new SimpleFragment(pattern, true);
    }

    public static CharacterFragment range(char from, char to) {
        return new CharacterRangeFragment(from, to);
    }

    public static CharacterFragment range(boolean positive, char from, char to) {
        return new CharacterRangeFragment(positive, from, to);
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

    public static CharacterFragment fixedChar(char c) {
        return new FixedCharacterFragment(c);
    }

    public static CharacterFragment oneCharOf(String chars) {
        return new CharacterRangeFragment(chars);
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

    public static BeeFragment escaped(char delimiter, char escaper) {
        return fixedChar(delimiter).then(quoted(delimiter, escaper)).then(fixedChar(delimiter));
    }

    public static BeeFragment quoted(char delimiter, char escaper) {
        return new CharacterRangeFragment(false, "" + delimiter + escaper)
                .or(fixedChar(escaper).then(CHAR))
                .any();
    }

}
