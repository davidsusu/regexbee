package hu.webarticum.regexbee.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import hu.webarticum.regexbee.SimpleFragment;

class ConcatenationFragmentTest {

    @Test
    void testNoInput() {
        ConcatenationFragment listFragment = new ConcatenationFragment();
        String expected = "";
        
        assertThat(listFragment.get()).isEqualTo(expected);
    }

    @Test
    void testSingleInput() {
        ConcatenationFragment listFragment = new ConcatenationFragment(new SimpleFragment("ab"));
        String expected = "ab";
        
        assertThat(listFragment.get()).isEqualTo(expected);
    }

    @Test
    void testManyDifferentInput() {
        ConcatenationFragment listFragment = new ConcatenationFragment(
                new SimpleFragment("ab"),
                new SimpleFragment("ab|"),
                new SimpleFragment("(abc)"),
                new SimpleFragment("|"),
                new SimpleFragment(""),
                new SimpleFragment("\\w.?"),
                new SimpleFragment("xx|yy"),
                new SimpleFragment("(?:abc|def)"));
        String expected = "ab(?:ab|)(abc)(?:|)\\w.?(?:xx|yy)(?:abc|def)";
        
        assertThat(listFragment.get()).isEqualTo(expected);
    }
    
}
