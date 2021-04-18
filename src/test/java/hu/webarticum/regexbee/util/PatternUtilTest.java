package hu.webarticum.regexbee.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PatternUtilTest {

    @Test
    void testSimpleAtomicPatterns() {
        assertThat(PatternUtil.isAtomic("a")).isTrue();
        assertThat(PatternUtil.isAtomic(".")).isTrue();
        assertThat(PatternUtil.isAtomic("\\w")).isTrue();
        assertThat(PatternUtil.isAtomic("(abc)")).isTrue();
        assertThat(PatternUtil.isAtomic("(ab|c)")).isTrue();
        assertThat(PatternUtil.isAtomic("(?:abc)")).isTrue();
        assertThat(PatternUtil.isAtomic("(?:a|b|c)")).isTrue();
    }

    @Test
    void testNonAtomicPatterns() {
        assertThat(PatternUtil.isAtomic("")).isFalse();
        assertThat(PatternUtil.isAtomic("|")).isFalse();
        assertThat(PatternUtil.isAtomic("ab")).isFalse();
        assertThat(PatternUtil.isAtomic("(a)bc")).isFalse();
        assertThat(PatternUtil.isAtomic("ab(c)")).isFalse();
    }

    @Test
    void testSimpleSafePatterns() {
        assertThat(PatternUtil.isSafe("")).isTrue();
        assertThat(PatternUtil.isSafe("a")).isTrue();
        assertThat(PatternUtil.isSafe("abc")).isTrue();
        assertThat(PatternUtil.isSafe("ab(c)")).isTrue();
        assertThat(PatternUtil.isSafe("ab(c)")).isTrue();
        assertThat(PatternUtil.isSafe("ab(c)\\w*")).isTrue();
    }

    @Test
    void testAtomicSafePatterns() {
        assertThat(PatternUtil.isSafe("a")).isTrue();
        assertThat(PatternUtil.isSafe(".")).isTrue();
        assertThat(PatternUtil.isSafe("\\w")).isTrue();
        assertThat(PatternUtil.isSafe("(abc)")).isTrue();
        assertThat(PatternUtil.isSafe("(ab|c)")).isTrue();
        assertThat(PatternUtil.isSafe("(?:abc)")).isTrue();
        assertThat(PatternUtil.isSafe("(?:a|b|c)")).isTrue();
    }
    
    @Test
    void testNonSafePatterns() {
        assertThat(PatternUtil.isSafe("|")).isFalse();
        assertThat(PatternUtil.isSafe("a|")).isFalse();
        assertThat(PatternUtil.isSafe("|b")).isFalse();
        assertThat(PatternUtil.isSafe("a|b")).isFalse();
        assertThat(PatternUtil.isSafe("a|b|.")).isFalse();
    }

    @Test
    void testWrap() {
        assertThat(PatternUtil.wrap("")).isEqualTo("(?:)");
        assertThat(PatternUtil.wrap("a")).isEqualTo("(?:a)");
        assertThat(PatternUtil.wrap("(?:a)")).isEqualTo("(?:(?:a))");
        assertThat(PatternUtil.wrap("\\w|xy")).isEqualTo("(?:\\w|xy)");
        
    }
    
}
