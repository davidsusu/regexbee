package hu.webarticum.regexbee.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import hu.webarticum.regexbee.BeeFragment;

class StringLiteralFragmentTest {

    @Test
    void testUnescapeable() {
        BeeFragment fragment = StringLiteralFragment.builder()
                .withDelimiter("'", true)
                .withSelfEscapingDisabled()
                .withNormalEscapingDisabled()
                .build();
        assertThat(match("''", fragment)).isTrue();
        assertThat(match("''''", fragment)).isFalse();
        assertThat(match("'abc'", fragment)).isTrue();
        assertThat(match("'ab'c'", fragment)).isFalse();
        assertThat(match("'ab\\'c'", fragment)).isFalse();
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
        assertThat(match("''", fragment)).isTrue();
        assertThat(match("''''", fragment)).isTrue();
        assertThat(match("'abc'", fragment)).isTrue();
        assertThat(match("'ab'c'", fragment)).isFalse();
        assertThat(match("'ab\\'c'", fragment)).isFalse();
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
        assertThat(match("''", fragment)).isTrue();
        assertThat(match("''''", fragment)).isFalse();
        assertThat(match("'abc'", fragment)).isTrue();
        assertThat(match("'ab'c'", fragment)).isFalse();
        assertThat(match("'ab\\'c'", fragment)).isTrue();
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
        assertThat(match("''", fragment)).isTrue();
        assertThat(match("''''", fragment)).isTrue();
        assertThat(match("'abc'", fragment)).isTrue();
        assertThat(match("'ab'c'", fragment)).isFalse();
        assertThat(match("'ab\\'c'", fragment)).isTrue();
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

    // TODO
    
    private static boolean match(String input, BeeFragment fragment) {
        return fragment.toPattern().matcher(input).matches();
    }
    
}
