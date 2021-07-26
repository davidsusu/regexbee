package hu.webarticum.regexbee.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import hu.webarticum.regexbee.BeeFragment;
import hu.webarticum.regexbee.SimpleFragment;

class StringLiteralFragmentTest {

    @Test
    void testUnescapeable() {
        BeeFragment fragment = StringLiteralFragment.builder()
                .withDelimiter("'", true)
                .withSelfEscapingDisabled()
                .withNormalEscapingDisabled()
                .build();
        assertThat(match("", fragment)).isFalse();
        assertThat(match("'", fragment)).isFalse();
        assertThat(match("''", fragment)).isTrue();
        assertThat(match("'''", fragment)).isFalse();
        assertThat(match("''''", fragment)).isFalse();
        assertThat(match("'abc'", fragment)).isTrue();
        assertThat(match("'ab'c'", fragment)).isFalse();
        assertThat(match("'ab\\'c'", fragment)).isFalse();
        assertThat(match("'abc\\'", fragment)).isTrue();
        assertThat(match("'ab''c'", fragment)).isFalse();
        assertThat(match("'a''b''c'", fragment)).isFalse();
        assertThat(match("'abc'''", fragment)).isFalse();
        assertThat(match("x'abc'", fragment)).isFalse();
        assertThat(match("'abc'y", fragment)).isFalse();
        assertThat(match("abc", fragment)).isFalse();
        assertThat(match("a'bc", fragment)).isFalse();
        assertThat(match("a'b'c", fragment)).isFalse();
        assertThat(match("a''bc", fragment)).isFalse();
    }

    @Test
    void testSelfEscapeable() {
        BeeFragment fragment = StringLiteralFragment.builder()
                .withDelimiter("'", true)
                .withSelfEscapingEnabled()
                .withNormalEscapingDisabled()
                .build();
        assertThat(match("", fragment)).isFalse();
        assertThat(match("'", fragment)).isFalse();
        assertThat(match("''", fragment)).isTrue();
        assertThat(match("'''", fragment)).isFalse();
        assertThat(match("''''", fragment)).isTrue();
        assertThat(match("'abc'", fragment)).isTrue();
        assertThat(match("'ab'c'", fragment)).isFalse();
        assertThat(match("'ab\\'c'", fragment)).isFalse();
        assertThat(match("'abc\\'", fragment)).isTrue();
        assertThat(match("'ab''c'", fragment)).isTrue();
        assertThat(match("'a''b''c'", fragment)).isTrue();
        assertThat(match("'abc'''", fragment)).isTrue();
        assertThat(match("x'abc'", fragment)).isFalse();
        assertThat(match("'abc'y", fragment)).isFalse();
        assertThat(match("abc", fragment)).isFalse();
        assertThat(match("a'bc", fragment)).isFalse();
        assertThat(match("a'b'c", fragment)).isFalse();
        assertThat(match("a''bc", fragment)).isFalse();
    }

    @Test
    void testNormalEscapeable() {
        BeeFragment fragment = StringLiteralFragment.builder()
                .withDelimiter("'", true)
                .withSelfEscapingDisabled()
                .withNormalEscapingEnabled()
                .build();
        assertThat(match("", fragment)).isFalse();
        assertThat(match("'", fragment)).isFalse();
        assertThat(match("''", fragment)).isTrue();
        assertThat(match("'''", fragment)).isFalse();
        assertThat(match("''''", fragment)).isFalse();
        assertThat(match("'abc'", fragment)).isTrue();
        assertThat(match("'ab'c'", fragment)).isFalse();
        assertThat(match("'ab\\'c'", fragment)).isTrue();
        assertThat(match("'abc\\'", fragment)).isFalse();
        assertThat(match("'ab''c'", fragment)).isFalse();
        assertThat(match("'abc\\''", fragment)).isTrue();
        assertThat(match("x'abc'", fragment)).isFalse();
        assertThat(match("'abc'y", fragment)).isFalse();
        assertThat(match("ab'c", fragment)).isFalse();
        assertThat(match("ab'c", fragment)).isFalse();
        assertThat(match("a''bc", fragment)).isFalse();
        assertThat(match("a\\'bc", fragment)).isFalse();
    }

    @Test
    void testBothEscapeable() {
        BeeFragment fragment = StringLiteralFragment.builder()
                .withDelimiter("'", true)
                .withSelfEscapingEnabled()
                .withNormalEscapingEnabled()
                .build();
        assertThat(match("", fragment)).isFalse();
        assertThat(match("'", fragment)).isFalse();
        assertThat(match("''", fragment)).isTrue();
        assertThat(match("'''", fragment)).isFalse();
        assertThat(match("''''", fragment)).isTrue();
        assertThat(match("'abc'", fragment)).isTrue();
        assertThat(match("'ab'c'", fragment)).isFalse();
        assertThat(match("'ab\\'c'", fragment)).isTrue();
        assertThat(match("'abc\\'", fragment)).isFalse();
        assertThat(match("'ab''c'", fragment)).isTrue();
        assertThat(match("'abc\\''", fragment)).isTrue();
        assertThat(match("'a''b\\'''c\\''", fragment)).isTrue();
        assertThat(match("x'abc'", fragment)).isFalse();
        assertThat(match("'abc'y", fragment)).isFalse();
        assertThat(match("ab'c", fragment)).isFalse();
        assertThat(match("ab'c", fragment)).isFalse();
        assertThat(match("a''bc", fragment)).isFalse();
        assertThat(match("a\\'bc", fragment)).isFalse();
    }

    @Test
    void testBothEscapeableWithoutDelimiters() {
        BeeFragment fragment = StringLiteralFragment.builder()
                .withDelimiter("'", false)
                .withSelfEscapingEnabled()
                .withNormalEscapingEnabled()
                .build();
        assertThat(match("", fragment)).isTrue();
        assertThat(match("'", fragment)).isFalse();
        assertThat(match("''", fragment)).isTrue();
        assertThat(match("''''", fragment)).isTrue();
        assertThat(match("'abc'", fragment)).isFalse();
        assertThat(match("'ab'c'", fragment)).isFalse();
        assertThat(match("'ab\\'c'", fragment)).isFalse();
        assertThat(match("'abc\\'", fragment)).isFalse();
        assertThat(match("'ab''c'", fragment)).isFalse();
        assertThat(match("'abc\\''", fragment)).isFalse();
        assertThat(match("'a''b\\'''c\\''", fragment)).isFalse();
        assertThat(match("x'abc'", fragment)).isFalse();
        assertThat(match("'abc'y", fragment)).isFalse();
        assertThat(match("ab'c", fragment)).isFalse();
        assertThat(match("ab'c", fragment)).isFalse();
        assertThat(match("a''bc", fragment)).isTrue();
        assertThat(match("a\\'bc", fragment)).isTrue();
        assertThat(match("a\\'b\\'''''c", fragment)).isTrue();
        assertThat(match("abc\\", fragment)).isFalse();
    }
    
    @Test
    void testDifferentDelimiters() {
        BeeFragment fragment = StringLiteralFragment.builder()
                .withLeftDelimiter("<", true)
                .withRightDelimiter(">", true)
                .withSelfEscapingEnabled()
                .withNormalEscapingEnabled()
                .build();
        assertThat(match("", fragment)).isFalse();
        assertThat(match("''", fragment)).isFalse();
        assertThat(match("<>", fragment)).isTrue();
        assertThat(match("<abc", fragment)).isFalse();
        assertThat(match("abc>", fragment)).isFalse();
        assertThat(match("<abc>", fragment)).isTrue();
        assertThat(match("<abc>>", fragment)).isFalse();
        assertThat(match("<ab<c>", fragment)).isTrue();
        assertThat(match("<ab\\<c>", fragment)).isTrue();
        assertThat(match("<ab>c>", fragment)).isFalse();
        assertThat(match("<ab\\>c>", fragment)).isTrue();
        assertThat(match("<ab>>c>", fragment)).isTrue();
        assertThat(match("<a\\>>>b>>>>c\\>>", fragment)).isTrue();
    }
    
    @Test
    void testTwoDollarsDelimiter() {
        BeeFragment fragment = StringLiteralFragment.builder()
                .withDelimiter("$$", true)
                .withSelfEscapingDisabled()
                .withNormalEscapingDisabled()
                .build();
        assertThat(match("", fragment)).isFalse();
        assertThat(match("''", fragment)).isFalse();
        assertThat(match("$", fragment)).isFalse();
        assertThat(match("$$", fragment)).isFalse();
        assertThat(match("$$$", fragment)).isFalse();
        assertThat(match("$$$$", fragment)).isTrue();
        assertThat(match("$$$$$", fragment)).isFalse();
        assertThat(match("$$\\$$$", fragment)).isFalse();
        assertThat(match("$$$x$$", fragment)).isTrue();
        assertThat(match("$$abc$$", fragment)).isTrue();
        assertThat(match("$$ab$$c$$", fragment)).isFalse();
        assertThat(match("$$ab\\$$c$$", fragment)).isFalse();
        assertThat(match("a$$bc$$", fragment)).isFalse();
        assertThat(match("$$ab$$c", fragment)).isFalse();
        assertThat(match("$$a$b$c$$", fragment)).isTrue();
        assertThat(match("$$abc\\$$", fragment)).isTrue();
    }
    
    @Test
    void testTwoDollarsDelimiterWithCustomNormalEscaping() {
        BeeFragment fragment = StringLiteralFragment.builder()
                .withDelimiter("$$", true)
                .withSelfEscapingDisabled()
                .withNormalEscapingEnabled()
                .withEscaper('-')
                .build();
        assertThat(match("", fragment)).isFalse();
        assertThat(match("''", fragment)).isFalse();
        assertThat(match("$", fragment)).isFalse();
        assertThat(match("$$", fragment)).isFalse();
        assertThat(match("$$$", fragment)).isFalse();
        assertThat(match("$$$$", fragment)).isTrue();
        assertThat(match("$$$$$", fragment)).isFalse();
        assertThat(match("$$-$$$", fragment)).isTrue();
        assertThat(match("$$$x$$", fragment)).isTrue();
        assertThat(match("$$abc$$", fragment)).isTrue();
        assertThat(match("$$ab$$c$$", fragment)).isFalse();
        assertThat(match("$$ab-$$c$$", fragment)).isTrue();
        assertThat(match("a$$bc$$", fragment)).isFalse();
        assertThat(match("$$ab$$c", fragment)).isFalse();
        assertThat(match("$$a$b$c$$", fragment)).isTrue();
        assertThat(match("$$abc-$$", fragment)).isFalse();
    }
    
    @Test
    void testComplexDelimiters() {
        BeeFragment fragment = StringLiteralFragment.builder()
                .withLeftDelimiter("(lorem)", true)
                .withRightDelimiter("(ipsum)", true)
                .withSelfEscapingDisabled()
                .withNormalEscapingEnabled()
                .build();
        assertThat(match("", fragment)).isFalse();
        assertThat(match("''", fragment)).isFalse();
        assertThat(match("(lorem)(ipsum)", fragment)).isTrue();
        assertThat(match("(lorem)\\(ipsum)", fragment)).isFalse();
        assertThat(match("(lorem)abcdef(ipsum)", fragment)).isTrue();
        assertThat(match("abc(lorem)def(ipsum)", fragment)).isFalse();
        assertThat(match("(lorem)abc(ipsum)def", fragment)).isFalse();
        assertThat(match("abc(lorem)(ipsum)def", fragment)).isFalse();
        assertThat(match("(lorem)abc(ipsum)def(ipsum)", fragment)).isFalse();
        assertThat(match("(lorem)abc\\(ipsum)def(ipsum)", fragment)).isTrue();
        assertThat(match("(lorem)abcdef(ipsum(ipsum)", fragment)).isTrue();
        assertThat(match("(lorem)abcdef(ippsum)", fragment)).isFalse();
        assertThat(match("(lorem)ab\\\\cdef(ipsum)", fragment)).isTrue();
    }
    
    @Test
    void testEmbeddableFragment() {
        BeeFragment fragment = StringLiteralFragment.builder()
                .withDelimiter("'", true)
                .withSelfEscapingDisabled()
                .withNormalEscapingEnabled()
                .withEmbeddableFragment(new SimpleFragment("\\$\\{[^\\}]*\\}"))
                .build();
        assertThat(match("'${lorem}'", fragment)).isTrue();
        assertThat(match("'${l'orem}'", fragment)).isTrue();
        assertThat(match("'lorem${ipsum}dolor${sit}amet'", fragment)).isTrue();
        assertThat(match("'lorem${ipsum}dolor${sit'", fragment)).isTrue();
        assertThat(match("'lorem${ipsum}dolor${sit'}", fragment)).isFalse();
        assertThat(match("'lorem${ip'sum}dolor${s'i'''t\\}amet'", fragment)).isTrue();
    }
    
    private static boolean match(String input, BeeFragment fragment) {
        return fragment.toPattern().matcher(input).matches();
    }
    
}
