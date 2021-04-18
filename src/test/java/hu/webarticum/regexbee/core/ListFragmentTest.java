package hu.webarticum.regexbee.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import hu.webarticum.regexbee.SimpleFragment;
import hu.webarticum.regexbee.common.ListFragment;

class ListFragmentTest {

    @Test
    void testNoInput() {
        ListFragment listFragment = new ListFragment();
        String expected = "";
        
        assertThat(listFragment.get()).isEqualTo(expected);
    }

    @Test
    void testSingleInput() {
        ListFragment listFragment = new ListFragment(new SimpleFragment("ab"));
        String expected = "ab";
        
        assertThat(listFragment.get()).isEqualTo(expected);
    }

    @Test
    void testManyDifferentInput() {
        ListFragment listFragment = new ListFragment(
                new SimpleFragment("ab"),
                new SimpleFragment("ab|"),
                new SimpleFragment("(abc)"),
                new SimpleFragment("|"),
                new SimpleFragment(""),
                new SimpleFragment("\\w.?"),
                new SimpleFragment("xx|yy"),
                new SimpleFragment("(?:abc|def)"));
        String expected = "(?:ab(?:ab|)(abc)(?:|)\\w.?(?:xx|yy)(?:abc|def))";
        
        assertThat(listFragment.get()).isEqualTo(expected);
    }
    
}
