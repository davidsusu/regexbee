package hu.webarticum.regexbee.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import hu.webarticum.regexbee.BeeFragment;
import hu.webarticum.regexbee.Greediness;
import hu.webarticum.regexbee.SimpleFragment;

class QuantifierFragmentTest {

    @Test
    void testIllegalConstructorArguments() {
        BeeFragment baseFragment = new SimpleFragment("abc");
        
        assertThatThrownBy(() ->
                new QuantifierFragment(baseFragment, -1, 1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() ->
                new QuantifierFragment(baseFragment, 1, QuantifierFragment.MAX_REPETITIONS + 1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() ->
                new QuantifierFragment(baseFragment, 2, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testZeroOccurence() {
        BeeFragment baseFragment = new SimpleFragment("abc");
        QuantifierFragment quantifierFragment = new QuantifierFragment(baseFragment, 0, 0);
        
        assertThat(quantifierFragment.get()).isEmpty();
    }

    @Test
    void testSingleOccurence() {
        BeeFragment baseFragment = new SimpleFragment("abc");
        QuantifierFragment quantifierFragment = new QuantifierFragment(baseFragment, 1, 1);
        String expected = "abc";
        
        assertThat(quantifierFragment.get()).isEqualTo(expected);
    }

    @Test
    void testOptionalAtomic() {
        BeeFragment baseFragment = new SimpleFragment("(a|b)");
        QuantifierFragment quantifierFragment = new QuantifierFragment(baseFragment, 0, 1);
        String expected = "(a|b)?";
        
        assertThat(quantifierFragment.get()).isEqualTo(expected);
    }

    @Test
    void testOptionalNonAtomic() {
        BeeFragment baseFragment = new SimpleFragment("abc");
        QuantifierFragment quantifierFragment = new QuantifierFragment(baseFragment, 0, 1);
        String expected = "(?:abc)?";
        
        assertThat(quantifierFragment.get()).isEqualTo(expected);
    }

    @Test
    void testAny() {
        BeeFragment baseFragment = new SimpleFragment("abc");
        QuantifierFragment quantifierFragment = new QuantifierFragment(
                baseFragment,
                0,
                QuantifierFragment.MAX_REPETITIONS);
        String expected = "(?:abc)*";
        
        assertThat(quantifierFragment.get()).isEqualTo(expected);
    }

    @Test
    void testMore() {
        BeeFragment baseFragment = new SimpleFragment("abc");
        QuantifierFragment quantifierFragment = new QuantifierFragment(
                baseFragment,
                1,
                QuantifierFragment.MAX_REPETITIONS);
        String expected = "(?:abc)+";
        
        assertThat(quantifierFragment.get()).isEqualTo(expected);
    }

    @Test
    void testExactly() {
        BeeFragment baseFragment = new SimpleFragment("abc");
        QuantifierFragment quantifierFragment = new QuantifierFragment(baseFragment, 5, 5);
        String expected = "(?:abc){5}";
        
        assertThat(quantifierFragment.get()).isEqualTo(expected);
    }

    @Test
    void testAtLeast() {
        BeeFragment baseFragment = new SimpleFragment("abc");
        QuantifierFragment quantifierFragment = new QuantifierFragment(
                baseFragment,
                5,
                QuantifierFragment.MAX_REPETITIONS);
        String expected = "(?:abc){5,}";
        
        assertThat(quantifierFragment.get()).isEqualTo(expected);
    }

    @Test
    void testAtMost() {
        BeeFragment baseFragment = new SimpleFragment("abc");
        QuantifierFragment quantifierFragment = new QuantifierFragment(baseFragment, 0, 12);
        String expected = "(?:abc){,12}";
        
        assertThat(quantifierFragment.get()).isEqualTo(expected);
    }

    @Test
    void testBound() {
        BeeFragment baseFragment = new SimpleFragment("abc");
        QuantifierFragment quantifierFragment = new QuantifierFragment(baseFragment, 5, 12);
        String expected = "(?:abc){5,12}";
        
        assertThat(quantifierFragment.get()).isEqualTo(expected);
    }

    @Test
    void testLazy() {
        BeeFragment baseFragment = new SimpleFragment("abc");
        QuantifierFragment quantifierFragment = new QuantifierFragment(
                baseFragment,
                0,
                QuantifierFragment.MAX_REPETITIONS,
                Greediness.LAZY);
        String expected = "(?:abc)*?";
        
        assertThat(quantifierFragment.get()).isEqualTo(expected);
    }

    @Test
    void testPossessive() {
        BeeFragment baseFragment = new SimpleFragment("abc");
        QuantifierFragment quantifierFragment = new QuantifierFragment(
                baseFragment,
                1,
                QuantifierFragment.MAX_REPETITIONS,
                Greediness.POSSESSIVE);
        String expected = "(?:abc)++";
        
        assertThat(quantifierFragment.get()).isEqualTo(expected);
    }

    @Test
    void testAtLeastPossessive() {
        BeeFragment baseFragment = new SimpleFragment("abc");
        QuantifierFragment quantifierFragment = new QuantifierFragment(
                baseFragment,
                7,
                QuantifierFragment.MAX_REPETITIONS,
                Greediness.POSSESSIVE);
        String expected = "(?:abc){7,}+";
        
        assertThat(quantifierFragment.get()).isEqualTo(expected);
    }
    
}
