package hu.webarticum.regexbee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigInteger;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class BeeTest extends AbstractBeeTest {
    
    private static final String LOREM_IPSUM_TEXT =
            "Lorem ipsum dolor -3 sit amet, 12\tconsectetur \u0628 \u00E1dipiscing el_it.";
    

    @Test
    void testChar() {
        assertThat(matchAll(Bee.CHAR, "")).isEmpty();
        assertThat(match(Bee.CHAR, "x")).isTrue();
        assertThat(match(Bee.CHAR, "\n")).isFalse();
        assertThat(match(Bee.CHAR, Pattern.DOTALL, "\n")).isTrue();
        assertThat(matchAll(Bee.CHAR, LOREM_IPSUM_TEXT)).containsExactly(
                "L", "o", "r", "e", "m", " ", "i", "p", "s", "u", "m", " ",
                "d", "o", "l", "o", "r", " ", "-", "3", " ",
                "s", "i", "t", " ", "a", "m", "e", "t", ",", " ",
                "1", "2", "\t", "c", "o", "n", "s", "e", "c", "t", "e", "t", "u", "r", " ", "\u0628", " ",
                "\u00E1", "d", "i", "p", "i", "s", "c", "i", "n", "g", " ", "e", "l", "_", "i", "t", ".");
    }
    
    @Test
    void testCharThroughLines() {
        assertThat(matchAll(Bee.CHAR_THROUGH_LINES, "")).isEmpty();
        assertThat(match(Bee.CHAR_THROUGH_LINES, "x")).isTrue();
        assertThat(match(Bee.CHAR_THROUGH_LINES, "\n")).isTrue();
        assertThat(match(Bee.CHAR_THROUGH_LINES, Pattern.DOTALL, "\n")).isTrue();
        assertThat(matchAll(Bee.CHAR_THROUGH_LINES, LOREM_IPSUM_TEXT)).containsExactly(
                "L", "o", "r", "e", "m", " ", "i", "p", "s", "u", "m", " ",
                "d", "o", "l", "o", "r", " ", "-", "3", " ",
                "s", "i", "t", " ", "a", "m", "e", "t", ",", " ",
                "1", "2", "\t", "c", "o", "n", "s", "e", "c", "t", "e", "t", "u", "r", " ", "\u0628", " ",
                "\u00E1", "d", "i", "p", "i", "s", "c", "i", "n", "g", " ", "e", "l", "_", "i", "t", ".");
    }

    @Test
    void testAnything() {
        assertThat(match(Bee.ANYTHING, "")).isTrue();
        assertThat(match(Bee.ANYTHING, " ")).isTrue();
        assertThat(match(Bee.ANYTHING, LOREM_IPSUM_TEXT)).isTrue();
        assertThat(match(Bee.ANYTHING, "lorem\nipsum")).isFalse();
        assertThat(match(Bee.ANYTHING, Pattern.DOTALL, "lorem\nipsum")).isTrue();
    }

    @Test
    void testAnythingThroughLines() {
        assertThat(match(Bee.ANYTHING_THROUGH_LINES, "")).isTrue();
        assertThat(match(Bee.ANYTHING_THROUGH_LINES, " ")).isTrue();
        assertThat(match(Bee.ANYTHING_THROUGH_LINES, LOREM_IPSUM_TEXT)).isTrue();
        assertThat(match(Bee.ANYTHING_THROUGH_LINES, "lorem\nipsum")).isTrue();
        assertThat(match(Bee.ANYTHING_THROUGH_LINES, Pattern.DOTALL, "lorem\nipsum")).isTrue();
    }

    @Test
    void testSomething() {
        assertThat(match(Bee.SOMETHING, "")).isFalse();
        assertThat(match(Bee.SOMETHING, " ")).isTrue();
        assertThat(match(Bee.SOMETHING, LOREM_IPSUM_TEXT)).isTrue();
        assertThat(match(Bee.ANYTHING, "lorem\nipsum")).isFalse();
        assertThat(match(Bee.ANYTHING, Pattern.DOTALL, "lorem\nipsum")).isTrue();
    }

    @Test
    void testSomethingThroughLines() {
        assertThat(match(Bee.SOMETHING_THROUGH_LINES, "")).isFalse();
        assertThat(match(Bee.SOMETHING_THROUGH_LINES, " ")).isTrue();
        assertThat(match(Bee.SOMETHING_THROUGH_LINES, LOREM_IPSUM_TEXT)).isTrue();
        assertThat(match(Bee.SOMETHING_THROUGH_LINES, "lorem\nipsum")).isTrue();
        assertThat(match(Bee.SOMETHING_THROUGH_LINES, Pattern.DOTALL, "lorem\nipsum")).isTrue();
    }

    @Test
    void testNothing() {
        assertThat(match(Bee.NOTHING, "")).isTrue();
        assertThat(match(Bee.NOTHING, " ")).isFalse();
        assertThat(match(Bee.NOTHING, LOREM_IPSUM_TEXT)).isFalse();
    }

    @Test
    void testFail() {
        assertThat(match(Bee.FAIL, "")).isFalse();
        assertThat(match(Bee.FAIL, LOREM_IPSUM_TEXT)).isFalse();
    }

    @Test
    void testBegin() {
        assertThat(matcher(Bee.BEGIN, "").start()).isZero();
        assertThat(matcher(Bee.BEGIN, LOREM_IPSUM_TEXT).start()).isZero();
        assertThat(find(Bee.BEGIN.then(Bee.fixed("b")), "a\nb")).isFalse();
        assertThat(find(Bee.BEGIN.then(Bee.fixed("b")), Pattern.MULTILINE, "a\nb")).isTrue();
    }

    @Test
    void testGlobalBegin() {
        assertThat(matcher(Bee.GLOBAL_BEGIN, "").start()).isZero();
        assertThat(matcher(Bee.GLOBAL_BEGIN, LOREM_IPSUM_TEXT).start()).isZero();
        assertThat(find(Bee.GLOBAL_BEGIN.then(Bee.fixed("b")), "a\nb")).isFalse();
        assertThat(find(Bee.GLOBAL_BEGIN.then(Bee.fixed("b")), Pattern.MULTILINE, "a\nb")).isFalse();
    }

    @Test
    void testEnd() {
        assertThat(matcher(Bee.END, "").start()).isZero();
        assertThat(matcher(Bee.END, LOREM_IPSUM_TEXT).start()).isEqualTo(65);
        assertThat(find(Bee.fixed("a").then(Bee.END), "a\nb")).isFalse();
        assertThat(find(Bee.fixed("a").then(Bee.END), Pattern.MULTILINE, "a\nb")).isTrue();
    }

    @Test
    void testGlobalEnd() {
        assertThat(matcher(Bee.GLOBAL_END, "").start()).isZero();
        assertThat(matcher(Bee.GLOBAL_END, LOREM_IPSUM_TEXT).start()).isEqualTo(65);
        assertThat(find(Bee.fixed("a").then(Bee.GLOBAL_END), "a\nb")).isFalse();
        assertThat(find(Bee.fixed("a").then(Bee.GLOBAL_END), Pattern.MULTILINE, "a\nb")).isFalse();
    }

    @Test
    void testWhitespace() {
        assertThat(matchAll(Bee.WHITESPACE, "")).isEmpty();
        assertThat(match(Bee.WHITESPACE, " ")).isTrue();
        assertThat(match(Bee.WHITESPACE, "x")).isFalse();
        assertThat(matcher(Bee.WHITESPACE, LOREM_IPSUM_TEXT).start()).isEqualTo(5);
        assertThat(matcher(Bee.WHITESPACE, LOREM_IPSUM_TEXT).end()).isEqualTo(6);
        assertThat(matchAll(Bee.WHITESPACE, LOREM_IPSUM_TEXT)).containsExactly(
                " ", " ", " ", " ", " ", " ", "\t", " ", " ", " ");
    }
    
    @Test
    void testSpace() {
        assertThat(matchAll(Bee.SPACE, "")).isEmpty();
        assertThat(match(Bee.SPACE, " ")).isTrue();
        assertThat(match(Bee.SPACE, "x")).isFalse();
        assertThat(matcher(Bee.SPACE, LOREM_IPSUM_TEXT).start()).isEqualTo(5);
        assertThat(matcher(Bee.SPACE, LOREM_IPSUM_TEXT).end()).isEqualTo(6);
        assertThat(matchAll(Bee.SPACE, LOREM_IPSUM_TEXT)).containsExactly(
                " ", " ", " ", " ", " ", " ", " ", " ", " ");
    }

    @Test
    void testBackslash() {
        assertThat(matchAll(Bee.BACKSLASH, "")).isEmpty();
        assertThat(match(Bee.BACKSLASH, "\\")).isTrue();
        assertThat(match(Bee.BACKSLASH, "x")).isFalse();
        assertThat(matchAll(Bee.BACKSLASH, LOREM_IPSUM_TEXT)).isEmpty();
    }

    @Test
    void testTab() {
        assertThat(matchAll(Bee.TAB, "")).isEmpty();
        assertThat(match(Bee.TAB, "\t")).isTrue();
        assertThat(match(Bee.TAB, "x")).isFalse();
        assertThat(matcher(Bee.TAB, LOREM_IPSUM_TEXT).start()).isEqualTo(33);
        assertThat(matcher(Bee.TAB, LOREM_IPSUM_TEXT).end()).isEqualTo(34);
        assertThat(matchAll(Bee.TAB, LOREM_IPSUM_TEXT)).containsExactly("\t");
    }

    @Test
    void testNewline() {
        assertThat(matchAll(Bee.NEWLINE, "")).isEmpty();
        assertThat(match(Bee.NEWLINE, "\n")).isTrue();
        assertThat(match(Bee.NEWLINE, "x")).isFalse();
        assertThat(matchAll(Bee.NEWLINE, LOREM_IPSUM_TEXT)).isEmpty();
        assertThat(matchAllPositions(Bee.NEWLINE, "lorem\nipsum\ndolor")).hasSize(2);
    }

    @Test
    void testAsciiLetter() {
        assertThat(matchAll(Bee.ASCII_LETTER, "")).isEmpty();
        assertThat(match(Bee.ASCII_LETTER, "x")).isTrue();
        assertThat(match(Bee.ASCII_LETTER, "Y")).isTrue();
        assertThat(matchAll(Bee.ASCII_LETTER, LOREM_IPSUM_TEXT)).containsExactly(
                "L", "o", "r", "e", "m", "i", "p", "s", "u", "m",
                "d", "o", "l", "o", "r",
                "s", "i", "t", "a", "m", "e", "t",
                "c", "o", "n", "s", "e", "c", "t", "e", "t", "u", "r",
                "d", "i", "p", "i", "s", "c", "i", "n", "g", "e", "l", "i", "t");
    }

    @Test
    void testAsciiLowerCaseLetter() {
        assertThat(matchAll(Bee.ASCII_LOWERCASE_LETTER, "")).isEmpty();
        assertThat(match(Bee.ASCII_LOWERCASE_LETTER, "x")).isTrue();
        assertThat(match(Bee.ASCII_LOWERCASE_LETTER, "Y")).isFalse();
        assertThat(matchAll(Bee.ASCII_LOWERCASE_LETTER, LOREM_IPSUM_TEXT)).containsExactly(
                "o", "r", "e", "m", "i", "p", "s", "u", "m",
                "d", "o", "l", "o", "r",
                "s", "i", "t", "a", "m", "e", "t",
                "c", "o", "n", "s", "e", "c", "t", "e", "t", "u", "r",
                "d", "i", "p", "i", "s", "c", "i", "n", "g", "e", "l", "i", "t");
    }

    @Test
    void testAsciiUpperCaseLetter() {
        assertThat(matchAll(Bee.ASCII_UPPERCASE_LETTER, "")).isEmpty();
        assertThat(match(Bee.ASCII_UPPERCASE_LETTER, "x")).isFalse();
        assertThat(match(Bee.ASCII_UPPERCASE_LETTER, "Y")).isTrue();
        assertThat(matchAll(Bee.ASCII_UPPERCASE_LETTER, LOREM_IPSUM_TEXT)).containsExactly("L");
    }
    
    @Test
    void testAsciiDigit() {
        assertThat(matchAll(Bee.ASCII_DIGIT, "")).isEmpty();
        assertThat(match(Bee.ASCII_DIGIT, "a")).isFalse();
        assertThat(match(Bee.ASCII_DIGIT, "4")).isTrue();
        assertThat(match(Bee.ASCII_DIGIT, "\u0664")).isFalse();
        assertThat(matcher(Bee.ASCII_DIGIT, LOREM_IPSUM_TEXT).start()).isEqualTo(19);
        assertThat(matchAll(Bee.ASCII_DIGIT, LOREM_IPSUM_TEXT)).containsExactly("3", "1", "2");
    }
    
    @Test
    void testAsciiWordChar() {
        assertThat(matchAll(Bee.ASCII_WORD_CHAR, "")).isEmpty();
        assertThat(matchAll(Bee.ASCII_WORD_CHAR, LOREM_IPSUM_TEXT)).containsExactly(
                "L", "o", "r", "e", "m", "i", "p", "s", "u", "m",
                "d", "o", "l", "o", "r", "3",
                "s", "i", "t", "a", "m", "e", "t",
                "1", "2", "c", "o", "n", "s", "e", "c", "t", "e", "t", "u", "r",
                "d", "i", "p", "i", "s", "c", "i", "n", "g", "e", "l", "_", "i", "t");
    }
    
    @Test
    void testAsciiWordStart() {
        assertThat(matchAll(Bee.ASCII_WORD_START, "")).isEmpty();
        assertThat(matchAllPositions(Bee.ASCII_WORD_START, LOREM_IPSUM_TEXT)).containsExactly(
                0, 6, 12, 19, 21, 25, 31, 34, 49, 59);
    }

    @Test
    void testAsciiWordEnd() {
        assertThat(matchAll(Bee.ASCII_WORD_END, "")).isEmpty();
        assertThat(matchAllPositions(Bee.ASCII_WORD_END, LOREM_IPSUM_TEXT)).containsExactly(
                5, 11, 17, 20, 24, 29, 33, 45, 58, 64);
    }

    @Test
    void testAsciiWord() {
        assertThat(matchAll(Bee.ASCII_WORD, "")).isEmpty();
        assertThat(matchAll(Bee.ASCII_WORD, LOREM_IPSUM_TEXT)).containsExactly(
                "Lorem", "ipsum", "dolor", "3", "sit", "amet",
                "12", "consectetur", "dipiscing", "el_it");
    }

    @Test
    void testDefaultWordBoundary() {
        assertThat(matchAll(Bee.DEFAULT_WORD_BOUNDARY, "")).isEmpty();
        assertThat(matchAllPositions(Bee.DEFAULT_WORD_BOUNDARY, LOREM_IPSUM_TEXT)).containsExactly(
                0, 5, 6, 11, 12, 17, 19, 20, 21, 24, 25, 29, 31, 33, 34, 45, 46, 47, 48, 58, 59, 64);
    }

    @Test
    void testIdentifier() {
        assertThat(matchAll(Bee.IDENTIFIER, "")).isEmpty();
        assertThat(matchAll(Bee.IDENTIFIER, LOREM_IPSUM_TEXT)).containsExactly(
                "Lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "el_it");
    }
    
    @Test
    void testLetter() {
        assertThat(matchAll(Bee.LETTER, "")).isEmpty();
        assertThat(matchAll(Bee.LETTER, LOREM_IPSUM_TEXT)).containsExactly(
                "L", "o", "r", "e", "m", "i", "p", "s", "u", "m",
                "d", "o", "l", "o", "r",
                "s", "i", "t", "a", "m", "e", "t",
                "c", "o", "n", "s", "e", "c", "t", "e", "t", "u", "r", "\u0628",
                "\u00E1", "d", "i", "p", "i", "s", "c", "i", "n", "g", "e", "l", "i", "t");
    }
    
    @Test
    void testDigit() {
        assertThat(matchAll(Bee.DIGIT, "")).isEmpty();
        assertThat(match(Bee.DIGIT, "\u0661")).isTrue();
        assertThat(matchAll(Bee.DIGIT, LOREM_IPSUM_TEXT)).containsExactly("3", "1", "2");
    }

    @Test
    void testWord() {
        assertThat(matchAll(Bee.WORD, "")).isEmpty();
        assertThat(matchAll(Bee.WORD, LOREM_IPSUM_TEXT)).containsExactly(
                "Lorem", "ipsum", "dolor", "3", "sit", "amet",
                "12", "consectetur", "\u0628", "\u00E1dipiscing", "el", "it");
    }

    @Test
    void testUnsignedInt() {
        assertThat(match(Bee.UNSIGNED_INT, "")).isFalse();
        assertThat(match(Bee.UNSIGNED_INT, "-4")).isFalse();
        assertThat(match(Bee.UNSIGNED_INT, "-05")).isFalse();
        assertThat(match(Bee.UNSIGNED_INT, "-028")).isFalse();
        assertThat(match(Bee.UNSIGNED_INT, "-23")).isFalse();
        assertThat(match(Bee.UNSIGNED_INT, "+5")).isFalse();
        assertThat(match(Bee.UNSIGNED_INT, "+12")).isFalse();
        assertThat(match(Bee.UNSIGNED_INT, "+0912")).isFalse();
        assertThat(match(Bee.UNSIGNED_INT, "0")).isTrue();
        assertThat(match(Bee.UNSIGNED_INT, "9")).isTrue();
        assertThat(match(Bee.UNSIGNED_INT, "17")).isTrue();
        assertThat(match(Bee.UNSIGNED_INT, "017")).isFalse();
        assertThat(matchAll(Bee.UNSIGNED_INT, LOREM_IPSUM_TEXT)).containsExactly("3", "12");
    }

    @Test
    void testSignedInt() {
        assertThat(match(Bee.SIGNED_INT, "")).isFalse();
        assertThat(match(Bee.SIGNED_INT, "-4")).isTrue();
        assertThat(match(Bee.SIGNED_INT, "-05")).isFalse();
        assertThat(match(Bee.SIGNED_INT, "-028")).isFalse();
        assertThat(match(Bee.SIGNED_INT, "-23")).isTrue();
        assertThat(match(Bee.SIGNED_INT, "+5")).isTrue();
        assertThat(match(Bee.SIGNED_INT, "+12")).isTrue();
        assertThat(match(Bee.SIGNED_INT, "+0912")).isFalse();
        assertThat(match(Bee.SIGNED_INT, "0")).isTrue();
        assertThat(match(Bee.SIGNED_INT, "9")).isTrue();
        assertThat(match(Bee.SIGNED_INT, "17")).isTrue();
        assertThat(match(Bee.SIGNED_INT, "017")).isFalse();
        assertThat(matchAll(Bee.SIGNED_INT, LOREM_IPSUM_TEXT)).containsExactly("-3", "12");
    }

    @Test
    void testStrictlySignedInt() {
        assertThat(match(Bee.STRICTLY_SIGNED_INT, "")).isFalse();
        assertThat(match(Bee.STRICTLY_SIGNED_INT, "-4")).isTrue();
        assertThat(match(Bee.STRICTLY_SIGNED_INT, "-05")).isFalse();
        assertThat(match(Bee.STRICTLY_SIGNED_INT, "-028")).isFalse();
        assertThat(match(Bee.STRICTLY_SIGNED_INT, "-23")).isTrue();
        assertThat(match(Bee.STRICTLY_SIGNED_INT, "+5")).isTrue();
        assertThat(match(Bee.STRICTLY_SIGNED_INT, "+12")).isTrue();
        assertThat(match(Bee.STRICTLY_SIGNED_INT, "+0912")).isFalse();
        assertThat(match(Bee.STRICTLY_SIGNED_INT, "0")).isFalse();
        assertThat(match(Bee.STRICTLY_SIGNED_INT, "9")).isFalse();
        assertThat(match(Bee.STRICTLY_SIGNED_INT, "17")).isFalse();
        assertThat(match(Bee.STRICTLY_SIGNED_INT, "017")).isFalse();
        assertThat(matchAll(Bee.STRICTLY_SIGNED_INT, LOREM_IPSUM_TEXT)).containsExactly("-3");
    }

    @Test
    void testTimestamp() {
        assertThat(match(Bee.TIMESTAMP, "")).isFalse();
        assertThat(match(Bee.TIMESTAMP, "lorem")).isFalse();
        assertThat(match(Bee.TIMESTAMP, "2021.07.24")).isFalse();
        assertThat(match(Bee.TIMESTAMP, "2021-07-24T14:22:30Z")).isTrue();
        assertThat(matchAll(Bee.TIMESTAMP, LOREM_IPSUM_TEXT)).isEmpty();
    }

    @Test
    void testSimple() {
        assertThat(match(Bee.simple("lorem"), "lorem")).isTrue();
        assertThat(match(Bee.simple("ipsum"), "lorem")).isFalse();
        assertThat(matchAllPositions(Bee.simple("s.m"), "ipsum")).containsExactly(2);
        assertThat(matchAllPositions(Bee.simple("or"), "lorem")).containsExactly(1);
    }
    
    @Test
    void testChecked() {
        assertThat(match(Bee.checked("lorem"), "lorem")).isTrue();
        assertThat(match(Bee.checked("ipsum"), "lorem")).isFalse();
        assertThatThrownBy(() -> Bee.checked("x(")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testRange() {
        assertThat(match(Bee.range('a', 'a'), "a")).isTrue();
        assertThat(match(Bee.range('a', 'z'), "a")).isTrue();
        assertThat(match(Bee.range('b', 'z'), "a")).isFalse();
        assertThat(match(Bee.range('b', 'b'), "a")).isFalse();
        assertThatThrownBy(() -> Bee.range('z', 'x')).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testNegativeRange() {
        assertThat(match(Bee.range(false, 'a', 'z'), "a")).isFalse();
        assertThat(match(Bee.range(false, 'a', 'a'), "a")).isFalse();
        assertThat(match(Bee.range(false, 'b', 'z'), "a")).isTrue();
        assertThat(match(Bee.range(false, 'b', 'b'), "a")).isTrue();
    }
    
    @Test
    void testFixed() {
        assertThat(match(Bee.fixed("lorem"), "lorem")).isTrue();
        assertThat(match(Bee.fixed("ipsum"), "lorem")).isFalse();
        assertThat(match(Bee.fixed("s.m"), "ipsum")).isFalse();
        assertThat(matchAllPositions(Bee.fixed("or"), "lorem")).containsExactly(1);
        assertThat(match(Bee.fixed("(?:x)"), "(?:x)")).isTrue();
        assertThat(match(Bee.fixed("(?:x)"), "x")).isFalse();
    }

    @Test
    void testOneFixedOf() {
        assertThat(match(Bee.oneFixedOf(), "")).isTrue();
        assertThat(match(Bee.oneFixedOf(), "lorem")).isFalse();
        assertThat(match(Bee.oneFixedOf("lorem", "ipsum"), "lorem")).isTrue();
        assertThat(match(Bee.oneFixedOf("lorem", "ipsum"), "ipsum")).isTrue();
        assertThat(matchAll(Bee.oneFixedOf("lorem", "?"), "lorem? ipsum ??")).containsExactly(
                "lorem", "?", "?", "?");
    }

    @Test
    void testFixedChar() {
        assertThat(match(Bee.fixedChar('a'), "")).isFalse();
        assertThat(match(Bee.fixedChar('a'), "x")).isFalse();
        assertThat(match(Bee.fixedChar('a'), "a")).isTrue();
        assertThat(match(Bee.fixedChar('a'), "aa")).isFalse();
        assertThat(match(Bee.fixedChar('?'), "?")).isTrue();
        assertThat(matchAllPositions(Bee.fixedChar('('), "lo(rem) ip(sum) do(lor)")).hasSize(3);
    }

    @Test
    void testOneCharOf() {
        BeeFragment fragment = Bee.oneCharOf("afnop");
        assertThat(match(fragment, "")).isFalse();
        assertThat(match(fragment, "a")).isTrue();
        assertThat(match(fragment, "b")).isFalse();
        assertThat(match(fragment, "f")).isTrue();
        assertThat(match(fragment, "an")).isFalse();
        assertThat(match(fragment, "!")).isFalse();
        assertThat(match(fragment, "a!")).isFalse();
    }

    @Test
    void testRef() {
        assertThat(
                matchAll(
                        Bee
                                .then(Bee.simple("..").as("g"))
                                .then(Bee.CHAR.any(Greediness.LAZY))
                                .then(Bee.ref("g")),
                        LOREM_IPSUM_TEXT))
                .containsExactly(
                        "orem ipsum dolor",
                        "it amet, 12\tconsectetur \u0628 \u00E1dipiscing el_it");
    }

    @Test
    void testLookBehind() {
        assertThat(matchAll(Bee.lookBehind(Bee.CHAR), "")).isEmpty();
        assertThat(matchAll(Bee.lookBehind(Bee.simple("or")).then(Bee.CHAR), LOREM_IPSUM_TEXT))
                .containsExactly("e", " ");
    }
    
    @Test
    void testLookBehindNot() {
        assertThat(matchAll(Bee.lookBehindNot(Bee.CHAR), "")).containsExactly("");
        assertThat(matchAll(Bee.lookBehindNot(Bee.BEGIN), "")).isEmpty();
        assertThat(matchAll(Bee.lookBehindNot(Bee.fixed("l")).then(Bee.simple("or..")), LOREM_IPSUM_TEXT))
                .containsExactly("orem");
    }

    @Test
    void testLookAhead() {
        assertThat(matchAll(Bee.lookAhead(Bee.CHAR), "")).isEmpty();
        assertThat(matchAll(Bee.simple(".o").then(Bee.lookAhead(Bee.simple("n"))), LOREM_IPSUM_TEXT))
                .containsExactly("co");
    }

    @Test
    void testLookAheadNot() {
        assertThat(matchAll(Bee.lookAheadNot(Bee.CHAR), "")).containsExactly("");
        assertThat(matchAll(Bee.lookAheadNot(Bee.END), "")).isEmpty();
        assertThat(matchAll(Bee.simple("... ").then(Bee.lookAheadNot(Bee.LETTER)), LOREM_IPSUM_TEXT))
                .containsExactly("lor ", "et, ");
    }

    @Test
    void testAtomic() {
        assertThat(matchAll(Bee.atomic(Bee.simple("di?")).then(Bee.fixed("p")), LOREM_IPSUM_TEXT))
                .containsExactly("dip");
        assertThat(matchAll(Bee.atomic(Bee.simple("dip?")).then(Bee.fixed("p")), LOREM_IPSUM_TEXT))
                .containsExactly();
    }

    @Test
    void testIntBetween() {
        assertThat(matchAll(Bee.intBetween(4,  17), LOREM_IPSUM_TEXT)).containsExactly("12");
        assertThat(matchAll(Bee.intBetween(-5,  4), LOREM_IPSUM_TEXT)).containsExactly("-3");
        assertThat(matchAll(Bee.intBetween(-3,  50), LOREM_IPSUM_TEXT)).containsExactly("-3", "12");
    }

    @Test
    void testIntBetweenWithBigInteger() {
        BigInteger low = BigInteger.valueOf(-3);
        BigInteger high = BigInteger.valueOf(12);
        assertThat(matchAll(Bee.intBetween(low, false, high, false), LOREM_IPSUM_TEXT)).isEmpty();
        assertThat(matchAll(Bee.intBetween(low, false, high, true), LOREM_IPSUM_TEXT))
                .containsExactly("12");
        assertThat(matchAll(Bee.intBetween(low, true, high, false), LOREM_IPSUM_TEXT))
                .containsExactly("-3");
        assertThat(matchAll(Bee.intBetween(low, true, high, true), LOREM_IPSUM_TEXT))
                .containsExactly("-3", "12");
    }

    @Test
    void testWith() {
        assertThat(
                matchAll(
                        Bee.with(Pattern.CASE_INSENSITIVE, Bee.simple("L")).then(Bee.simple("or.")),
                        LOREM_IPSUM_TEXT))
                .containsExactly("Lore", "lor ");
    }

    @Test
    void testWithout() {
        BeeFragment fragment = Bee.with(
                Pattern.CASE_INSENSITIVE,
                Bee.simple("l")
                        .then(Bee.without(Pattern.CASE_INSENSITIVE, Bee.simple("or"))));
        assertThat(matchAll(fragment, LOREM_IPSUM_TEXT)).containsExactly("Lor", "lor");
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
        assertThat(matchAll(fragment, "aaa\nbbb\nccc")).containsExactly("aaa\nbbb");
    }
    
}
