package hu.webarticum.regexbee.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import hu.webarticum.regexbee.SimpleFragment;

class NamedGroupFragmentTest {

    @Test
    void testEmpty() {
        NamedGroupFragment namedGroupFragment =
                new NamedGroupFragment(new SimpleFragment(""), "g1");
        String expected = "(?<g1>)";
        
        assertThat(namedGroupFragment.get()).isEqualTo(expected);
    }

    @Test
    void testDecoration() {
        NamedGroupFragment namedGroupFragment =
                new NamedGroupFragment(new SimpleFragment("abc"), "G1");
        String expected = "(?<G1>abc)";
        
        assertThat(namedGroupFragment.get()).isEqualTo(expected);
    }

}
