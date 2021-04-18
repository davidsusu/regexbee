package hu.webarticum.regexbee.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PatternUtilTest {

    @Test
    void testIsValidGroupName() {
        assertThat(PatternUtil.isValidGroupName("")).isFalse();
        assertThat(PatternUtil.isValidGroupName("abc")).isTrue();
        assertThat(PatternUtil.isValidGroupName("ab(c")).isFalse();
        assertThat(PatternUtil.isValidGroupName("ab>c")).isFalse();
    }

    @Test
    void testSimpleAtomicPatterns() {
        assertThat(PatternUtil.isAtomicPattern("a")).isTrue();
        assertThat(PatternUtil.isAtomicPattern(".")).isTrue();
        assertThat(PatternUtil.isAtomicPattern("\\w")).isTrue();
        assertThat(PatternUtil.isAtomicPattern("(abc)")).isTrue();
        assertThat(PatternUtil.isAtomicPattern("(ab|c)")).isTrue();
        assertThat(PatternUtil.isAtomicPattern("(?:abc)")).isTrue();
        assertThat(PatternUtil.isAtomicPattern("(?:a|b|c)")).isTrue();
    }

    @Test
    void testNonAtomicPatterns() {
        assertThat(PatternUtil.isAtomicPattern("")).isFalse();
        assertThat(PatternUtil.isAtomicPattern("|")).isFalse();
        assertThat(PatternUtil.isAtomicPattern("ab")).isFalse();
        assertThat(PatternUtil.isAtomicPattern("(a)bc")).isFalse();
        assertThat(PatternUtil.isAtomicPattern("ab(c)")).isFalse();
    }

    @Test
    void testSimpleSafePatterns() {
        assertThat(PatternUtil.isSafePattern("")).isTrue();
        assertThat(PatternUtil.isSafePattern("a")).isTrue();
        assertThat(PatternUtil.isSafePattern("abc")).isTrue();
        assertThat(PatternUtil.isSafePattern("ab(c)")).isTrue();
        assertThat(PatternUtil.isSafePattern("ab(c)")).isTrue();
        assertThat(PatternUtil.isSafePattern("ab(c)\\w*")).isTrue();
    }

    @Test
    void testAtomicSafePatterns() {
        assertThat(PatternUtil.isSafePattern("a")).isTrue();
        assertThat(PatternUtil.isSafePattern(".")).isTrue();
        assertThat(PatternUtil.isSafePattern("\\w")).isTrue();
        assertThat(PatternUtil.isSafePattern("(abc)")).isTrue();
        assertThat(PatternUtil.isSafePattern("(ab|c)")).isTrue();
        assertThat(PatternUtil.isSafePattern("(?:abc)")).isTrue();
        assertThat(PatternUtil.isSafePattern("(?:a|b|c)")).isTrue();
    }
    
    @Test
    void testNonSafePatterns() {
        assertThat(PatternUtil.isSafePattern("|")).isFalse();
        assertThat(PatternUtil.isSafePattern("a|")).isFalse();
        assertThat(PatternUtil.isSafePattern("|b")).isFalse();
        assertThat(PatternUtil.isSafePattern("a|b")).isFalse();
        assertThat(PatternUtil.isSafePattern("a|b|.")).isFalse();
    }

    @Test
    void testWrap() {
        assertThat(PatternUtil.wrapPattern("")).isEqualTo("(?:)");
        assertThat(PatternUtil.wrapPattern("a")).isEqualTo("(?:a)");
        assertThat(PatternUtil.wrapPattern("(?:a)")).isEqualTo("(?:(?:a))");
        assertThat(PatternUtil.wrapPattern("\\w|xy")).isEqualTo("(?:\\w|xy)");
        
    }
    
}
