package hu.webarticum.regexbee.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import hu.webarticum.regexbee.SimpleFragment;

class AlternationFragmentTest {

    @Test
    void testNoInput() {
        AlternationFragment alternationFragment = new AlternationFragment();
        String expected = "";
        
        assertThat(alternationFragment.get()).isEqualTo(expected);
    }

    @Test
    void testSingleInput() {
        AlternationFragment alternationFragment = new AlternationFragment(new SimpleFragment("ab"));
        String expected = "ab";
        
        assertThat(alternationFragment.get()).isEqualTo(expected);
    }

    @Test
    void testManyDifferentInput() {
        AlternationFragment alternationFragment = new AlternationFragment(
                new SimpleFragment("ab"),
                new SimpleFragment("ab|"),
                new SimpleFragment("(abc)"),
                new SimpleFragment("|"),
                new SimpleFragment(""),
                new SimpleFragment("\\w.?"),
                new SimpleFragment("xx|yy"),
                new SimpleFragment("(?:abc|def)"));
        String expected = "(?:ab|(?:ab|)|(abc)|(?:|)||\\w.?|(?:xx|yy)|(?:abc|def))";
        
        assertThat(alternationFragment.get()).isEqualTo(expected);
    }
    
}
