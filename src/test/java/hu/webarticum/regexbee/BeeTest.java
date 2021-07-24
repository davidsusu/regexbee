package hu.webarticum.regexbee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class BeeTest {
    
    private static final String LOREM_IPSUM_TEXT =
            "Lorem ipsum dolor -3 sit amet, 12\tconsectetur \u0628 \u00E1dipiscing el_it.";
    

    @Test
    void testAnything() {
        assertThat(match("", Bee.ANYTHING)).isTrue();
        assertThat(match(" ", Bee.ANYTHING)).isTrue();
        assertThat(match(LOREM_IPSUM_TEXT, Bee.ANYTHING)).isTrue();
    }

    @Test
    void testSomething() {
        assertThat(match("", Bee.SOMETHING)).isFalse();
        assertThat(match(" ", Bee.SOMETHING)).isTrue();
        assertThat(match(LOREM_IPSUM_TEXT, Bee.SOMETHING)).isTrue();
    }

    @Test
    void testNothing() {
        assertThat(match("", Bee.NOTHING)).isTrue();
        assertThat(match(" ", Bee.NOTHING)).isFalse();
        assertThat(match(LOREM_IPSUM_TEXT, Bee.NOTHING)).isFalse();
    }

    @Test
    void testFail() {
        assertThat(match("", Bee.FAIL)).isFalse();
        assertThat(match(LOREM_IPSUM_TEXT, Bee.FAIL)).isFalse();
    }

    @Test
    void testBegin() {
        assertThat(matcher("", Bee.BEGIN).start()).isZero();
        assertThat(matcher(LOREM_IPSUM_TEXT, Bee.BEGIN).start()).isZero();
    }

    @Test
    void testEnd() {
        assertThat(matcher("", Bee.END).start()).isZero();
        assertThat(matcher(LOREM_IPSUM_TEXT, Bee.END).start()).isEqualTo(65);
    }

    @Test
    void testChar() {
        assertThat(matchAll("", Bee.CHAR)).isEmpty();
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.CHAR)).containsExactly(
                "L", "o", "r", "e", "m", " ", "i", "p", "s", "u", "m", " ",
                "d", "o", "l", "o", "r", " ", "-", "3", " ",
                "s", "i", "t", " ", "a", "m", "e", "t", ",", " ",
                "1", "2", "\t", "c", "o", "n", "s", "e", "c", "t", "e", "t", "u", "r", " ", "\u0628", " ",
                "\u00E1", "d", "i", "p", "i", "s", "c", "i", "n", "g", " ", "e", "l", "_", "i", "t", ".");
    }

    @Test
    void testSpace() {
        assertThat(matchAll("", Bee.SPACE)).isEmpty();
        assertThat(matcher(LOREM_IPSUM_TEXT, Bee.SPACE).start()).isEqualTo(5);
        assertThat(matcher(LOREM_IPSUM_TEXT, Bee.SPACE).end()).isEqualTo(6);
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.SPACE)).containsExactly(
                " ", " ", " ", " ", " ", " ", " ", " ", " ");
    }

    @Test
    void testTab() {
        assertThat(matchAll("", Bee.TAB)).isEmpty();
        assertThat(matcher(LOREM_IPSUM_TEXT, Bee.TAB).start()).isEqualTo(33);
        assertThat(matcher(LOREM_IPSUM_TEXT, Bee.TAB).end()).isEqualTo(34);
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.TAB)).containsExactly("\t");
    }

    @Test
    void testWhitespace() {
        assertThat(matchAll("", Bee.WHITESPACE)).isEmpty();
        assertThat(matcher(LOREM_IPSUM_TEXT, Bee.WHITESPACE).start()).isEqualTo(5);
        assertThat(matcher(LOREM_IPSUM_TEXT, Bee.WHITESPACE).end()).isEqualTo(6);
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.WHITESPACE)).containsExactly(
                " ", " ", " ", " ", " ", " ", "\t", " ", " ", " ");
    }
    
    @Test
    void testAsciiLetter() {
        assertThat(matchAll("", Bee.ASCII_LETTER)).isEmpty();
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.ASCII_LETTER)).containsExactly(
                "L", "o", "r", "e", "m", "i", "p", "s", "u", "m",
                "d", "o", "l", "o", "r",
                "s", "i", "t", "a", "m", "e", "t",
                "c", "o", "n", "s", "e", "c", "t", "e", "t", "u", "r",
                "d", "i", "p", "i", "s", "c", "i", "n", "g", "e", "l", "i", "t");
    }
    
    @Test
    void testAsciiDigit() {
        assertThat(matchAll("", Bee.ASCII_DIGIT)).isEmpty();
        assertThat(matcher(LOREM_IPSUM_TEXT, Bee.ASCII_DIGIT).start()).isEqualTo(19);
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.ASCII_DIGIT)).containsExactly("3", "1", "2");
    }
    
    @Test
    void testAsciiWordChar() {
        assertThat(matchAll("", Bee.ASCII_WORD_CHAR)).isEmpty();
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.ASCII_WORD_CHAR)).containsExactly(
                "L", "o", "r", "e", "m", "i", "p", "s", "u", "m",
                "d", "o", "l", "o", "r", "3",
                "s", "i", "t", "a", "m", "e", "t",
                "1", "2", "c", "o", "n", "s", "e", "c", "t", "e", "t", "u", "r",
                "d", "i", "p", "i", "s", "c", "i", "n", "g", "e", "l", "_", "i", "t");
    }
    
    @Test
    void testAsciiWordStart() {
        assertThat(matchAll("", Bee.ASCII_WORD_START)).isEmpty();
        assertThat(matchAllPositions(LOREM_IPSUM_TEXT, Bee.ASCII_WORD_START)).containsExactly(
                0, 6, 12, 19, 21, 25, 31, 34, 49, 59);
    }

    @Test
    void testAsciiWordEnd() {
        assertThat(matchAll("", Bee.ASCII_WORD_END)).isEmpty();
        assertThat(matchAllPositions(LOREM_IPSUM_TEXT, Bee.ASCII_WORD_END)).containsExactly(
                5, 11, 17, 20, 24, 29, 33, 45, 58, 64);
    }

    @Test
    void testAsciiWord() {
        assertThat(matchAll("", Bee.ASCII_WORD)).isEmpty();
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.ASCII_WORD)).containsExactly(
                "Lorem", "ipsum", "dolor", "3", "sit", "amet",
                "12", "consectetur", "dipiscing", "el_it");
    }

    @Test
    void testDefaultWordBoundary() {
        assertThat(matchAll("", Bee.DEFAULT_WORD_BOUNDARY)).isEmpty();
        assertThat(matchAllPositions(LOREM_IPSUM_TEXT, Bee.DEFAULT_WORD_BOUNDARY)).containsExactly(
                0, 5, 6, 11, 12, 17, 19, 20, 21, 24, 25, 29, 31, 33, 34, 45, 46, 47, 48, 58, 59, 64);
    }

    @Test
    void testIdentifier() {
        assertThat(matchAll("", Bee.IDENTIFIER)).isEmpty();
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.IDENTIFIER)).containsExactly(
                "Lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "el_it");
    }
    
    @Test
    void testLetter() {
        assertThat(matchAll("", Bee.LETTER)).isEmpty();
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.LETTER)).containsExactly(
                "L", "o", "r", "e", "m", "i", "p", "s", "u", "m",
                "d", "o", "l", "o", "r",
                "s", "i", "t", "a", "m", "e", "t",
                "c", "o", "n", "s", "e", "c", "t", "e", "t", "u", "r", "\u0628",
                "\u00E1", "d", "i", "p", "i", "s", "c", "i", "n", "g", "e", "l", "i", "t");
    }
    
    @Test
    void testDigit() {
        assertThat(matchAll("", Bee.DIGIT)).isEmpty();
        assertThat(match("\u0661", Bee.DIGIT)).isTrue();
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.DIGIT)).containsExactly("3", "1", "2");
    }

    @Test
    void testWord() {
        assertThat(matchAll("", Bee.WORD)).isEmpty();
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.WORD)).containsExactly(
                "Lorem", "ipsum", "dolor", "3", "sit", "amet",
                "12", "consectetur", "\u0628", "\u00E1dipiscing", "el", "it");
    }

    @Test
    void testUnsignedInt() {
        assertThat(match("", Bee.UNSIGNED_INT)).isFalse();
        assertThat(match("-4", Bee.UNSIGNED_INT)).isFalse();
        assertThat(match("-05", Bee.UNSIGNED_INT)).isFalse();
        assertThat(match("-028", Bee.UNSIGNED_INT)).isFalse();
        assertThat(match("-23", Bee.UNSIGNED_INT)).isFalse();
        assertThat(match("+5", Bee.UNSIGNED_INT)).isFalse();
        assertThat(match("+12", Bee.UNSIGNED_INT)).isFalse();
        assertThat(match("+0912", Bee.UNSIGNED_INT)).isFalse();
        assertThat(match("0", Bee.UNSIGNED_INT)).isTrue();
        assertThat(match("9", Bee.UNSIGNED_INT)).isTrue();
        assertThat(match("17", Bee.UNSIGNED_INT)).isTrue();
        assertThat(match("017", Bee.UNSIGNED_INT)).isFalse();
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.UNSIGNED_INT)).containsExactly("3", "12");
    }

    @Test
    void testSignedInt() {
        assertThat(match("", Bee.SIGNED_INT)).isFalse();
        assertThat(match("-4", Bee.SIGNED_INT)).isTrue();
        assertThat(match("-05", Bee.SIGNED_INT)).isFalse();
        assertThat(match("-028", Bee.SIGNED_INT)).isFalse();
        assertThat(match("-23", Bee.SIGNED_INT)).isTrue();
        assertThat(match("+5", Bee.SIGNED_INT)).isTrue();
        assertThat(match("+12", Bee.SIGNED_INT)).isTrue();
        assertThat(match("+0912", Bee.SIGNED_INT)).isFalse();
        assertThat(match("0", Bee.SIGNED_INT)).isTrue();
        assertThat(match("9", Bee.SIGNED_INT)).isTrue();
        assertThat(match("17", Bee.SIGNED_INT)).isTrue();
        assertThat(match("017", Bee.SIGNED_INT)).isFalse();
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.SIGNED_INT)).containsExactly("-3", "12");
    }

    @Test
    void testStrictlySignedInt() {
        assertThat(match("", Bee.STRICTLY_SIGNED_INT)).isFalse();
        assertThat(match("-4", Bee.STRICTLY_SIGNED_INT)).isTrue();
        assertThat(match("-05", Bee.STRICTLY_SIGNED_INT)).isFalse();
        assertThat(match("-028", Bee.STRICTLY_SIGNED_INT)).isFalse();
        assertThat(match("-23", Bee.STRICTLY_SIGNED_INT)).isTrue();
        assertThat(match("+5", Bee.STRICTLY_SIGNED_INT)).isTrue();
        assertThat(match("+12", Bee.STRICTLY_SIGNED_INT)).isTrue();
        assertThat(match("+0912", Bee.STRICTLY_SIGNED_INT)).isFalse();
        assertThat(match("0", Bee.STRICTLY_SIGNED_INT)).isFalse();
        assertThat(match("9", Bee.STRICTLY_SIGNED_INT)).isFalse();
        assertThat(match("17", Bee.STRICTLY_SIGNED_INT)).isFalse();
        assertThat(match("017", Bee.STRICTLY_SIGNED_INT)).isFalse();
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.STRICTLY_SIGNED_INT)).containsExactly("-3");
    }

    @Test
    void testTimestamp() {
        assertThat(match("", Bee.TIMESTAMP)).isFalse();
        assertThat(match("lorem", Bee.TIMESTAMP)).isFalse();
        assertThat(match("2021.07.24", Bee.TIMESTAMP)).isFalse();
        assertThat(match("2021-07-24T14:22:30Z", Bee.TIMESTAMP)).isTrue();
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.TIMESTAMP)).isEmpty();
    }

    @Test
    void testSimple() {
        assertThat(match("lorem", Bee.simple("lorem"))).isTrue();
        assertThat(match("lorem", Bee.simple("ipsum"))).isFalse();
        assertThat(matchAllPositions("ipsum", Bee.simple("s.m"))).containsExactly(2);
        assertThat(matchAllPositions("lorem", Bee.simple("or"))).containsExactly(1);
    }
    
    @Test
    void testChecked() {
        assertThat(match("lorem", Bee.checked("lorem"))).isTrue();
        assertThat(match("lorem", Bee.checked("ipsum"))).isFalse();
        assertThatThrownBy(() -> Bee.checked("x(")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testFixed() {
        assertThat(match("lorem", Bee.fixed("lorem"))).isTrue();
        assertThat(match("lorem", Bee.fixed("ipsum"))).isFalse();
        assertThat(match("ipsum", Bee.fixed("s.m"))).isFalse();
        assertThat(matchAllPositions("lorem", Bee.fixed("or"))).containsExactly(1);
        assertThat(match("(?:x)", Bee.fixed("(?:x)"))).isTrue();
        assertThat(match("x", Bee.fixed("(?:x)"))).isFalse();
    }

    @Test
    void testOneFixedOf() {
        assertThat(match("", Bee.oneFixedOf())).isTrue();
        assertThat(match("lorem", Bee.oneFixedOf())).isFalse();
        assertThat(match("lorem", Bee.oneFixedOf("lorem", "ipsum"))).isTrue();
        assertThat(match("ipsum", Bee.oneFixedOf("lorem", "ipsum"))).isTrue();
        assertThat(matchAll("lorem? ipsum ??", Bee.oneFixedOf("lorem", "?"))).containsExactly(
                "lorem", "?", "?", "?");
    }

    @Test
    void testRef() {
        assertThat(
                matchAll(LOREM_IPSUM_TEXT,
                Bee
                        .then(Bee.simple("..").as("g"))
                        .then(Bee.CHAR.any(Greediness.LAZY))
                        .then(Bee.ref("g"))))
                .containsExactly(
                        "orem ipsum dolor",
                        "it amet, 12\tconsectetur \u0628 \u00E1dipiscing el_it");
    }

    @Test
    void testLookBehind() {
        assertThat(matchAll("", Bee.lookBehind(Bee.CHAR))).isEmpty();
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.lookBehind(Bee.simple("or")).then(Bee.CHAR)))
                .containsExactly("e", " ");
    }
    
    @Test
    void testLookBehindNot() {
        assertThat(matchAll("", Bee.lookBehindNot(Bee.CHAR))).containsExactly("");
        assertThat(matchAll("", Bee.lookBehindNot(Bee.BEGIN))).isEmpty();
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.lookBehindNot(Bee.fixed("l")).then(Bee.simple("or.."))))
                .containsExactly("orem");
    }

    @Test
    void testLookAhead() {
        assertThat(matchAll("", Bee.lookAhead(Bee.CHAR))).isEmpty();
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.simple(".o").then(Bee.lookAhead(Bee.simple("n")))))
                .containsExactly("co");
    }

    @Test
    void testLookAheadNot() {
        assertThat(matchAll("", Bee.lookAheadNot(Bee.CHAR))).containsExactly("");
        assertThat(matchAll("", Bee.lookAheadNot(Bee.END))).isEmpty();
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.simple("... ").then(Bee.lookAheadNot(Bee.LETTER))))
                .containsExactly("lor ", "et, ");
    }

    @Test
    void testAtomic() {
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.atomic(Bee.simple("di?")).then(Bee.fixed("p"))))
                .containsExactly("dip");
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.atomic(Bee.simple("dip?")).then(Bee.fixed("p"))))
                .containsExactly();
    }

    @Test
    void testIntBetween() {
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.intBetween(4,  17))).containsExactly("12");
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.intBetween(-5,  4))).containsExactly("-3");
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.intBetween(-3,  50))).containsExactly("-3", "12");
    }

    @Test
    void testIntBetweenWithBigInteger() {
        BigInteger low = BigInteger.valueOf(-3);
        BigInteger high = BigInteger.valueOf(12);
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.intBetween(low, false, high, false))).isEmpty();
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.intBetween(low, false, high, true)))
                .containsExactly("12");
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.intBetween(low, true, high, false)))
                .containsExactly("-3");
        assertThat(matchAll(LOREM_IPSUM_TEXT, Bee.intBetween(low, true, high, true)))
                .containsExactly("-3", "12");
    }

    @Test
    void testWith() {
        assertThat(matchAll(
                LOREM_IPSUM_TEXT,
                Bee.with(Pattern.CASE_INSENSITIVE, Bee.simple("L")).then(Bee.simple("or."))))
                .containsExactly("Lore", "lor ");
    }

    @Test
    void testWithout() {
        BeeFragment fragment = Bee.with(
                Pattern.CASE_INSENSITIVE,
                Bee.simple("l")
                        .then(Bee.without(Pattern.CASE_INSENSITIVE, Bee.simple("or"))));
        assertThat(matchAll(LOREM_IPSUM_TEXT, fragment)).containsExactly("Lor", "lor");
    }

    @Test
    void testWithWithout() {
        BeeFragment fragment = Bee.with(
                Pattern.DOTALL | Pattern.UNIX_LINES,
                Bee.simple("aaa.bb")
                        .then(Bee.with(
                                Pattern.CASE_INSENSITIVE,
                                Pattern.DOTALL,
                                Bee.simple("B(?:....)?"))));
        assertThat(matchAll("aaa\nbbb\nccc", fragment)).containsExactly("aaa\nbbb");
    }
    

    private static Matcher matcher(String input, BeeFragment fragment) {
        Matcher matcher = fragment.toPattern().matcher(input);
        if (!matcher.find()) {
            throw new IllegalArgumentException("No match found");
        }
        return matcher;
    }
    
    private static boolean match(String input, BeeFragment fragment) {
        return fragment.toPattern().matcher(input).matches();
    }
    
    private static List<String> matchAll(String input, BeeFragment fragment) {
        List<String> result = new ArrayList<>();
        Matcher matcher = fragment.toPattern().matcher(input);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }
    
    private static List<Integer> matchAllPositions(String input, BeeFragment fragment) {
        List<Integer> result = new ArrayList<>();
        Matcher matcher = fragment.toPattern().matcher(input);
        while (matcher.find()) {
            result.add(matcher.start());
        }
        return result;
    }
    
}
