package hu.webarticum.regexbee.character;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import hu.webarticum.regexbee.AbstractBeeTest;
import hu.webarticum.regexbee.BeeFragment;

class CharacterRangeFragmentTest extends AbstractBeeTest {

    @Test
    void testRange() {
        BeeFragment fragment = new CharacterRangeFragment('g', 'k');
        assertThat(filterMatching(fragment, "a", "g", "i", "k", "x")).containsExactly("g", "i", "k");
        assertThat(fragment.toPattern().matcher("piriguhu").replaceAll("_")).isEqualTo("p_r__u_u");
    }

    @Test
    void testNegativeRange() {
        BeeFragment fragment = new CharacterRangeFragment(false, 'g', 'k');
        assertThat(filterMatching(fragment, "a", "g", "i", "k", "x")).containsExactly("a", "x");
        assertThat(fragment.toPattern().matcher("piriguhu").replaceAll("_")).isEqualTo("_i_ig_h_");
    }

    @Test
    void testComplex() {
        BeeFragment fragment = CharacterRangeFragment.builder()
                .addAll("afh")
                .addAll("(?!")
                .addRange('m', 'o')
                .addRange('q', 'r')
                .addRange('x', 'z')
                .build();
        assertThat(filterMatching(
                fragment,
                "a", "g", "h", "k", "n", "p", "q", "r", "s", "y", ",", ":", "(", "?", "!"))
                .containsExactly("a", "h", "n", "q", "r", "y", "(", "?", "!");
        assertThat(fragment.toPattern().matcher("amufareti").replaceAll("_")).isEqualTo("__u___eti");
    }

    @Test
    void testNegativeComplex() {
        BeeFragment fragment = CharacterRangeFragment.builder()
                .withNegativeMatching()
                .addAll("afh")
                .addAll("(?!")
                .addRange('m', 'o')
                .addRange('q', 'r')
                .addRange('x', 'z')
                .build();
        assertThat(filterMatching(
                fragment,
                "a", "g", "h", "k", "n", "p", "q", "r", "s", "y", ",", ":", "(", "?", "!"))
                .containsExactly("g", "k", "p", "s", ",", ":");
        assertThat(fragment.toPattern().matcher("amufareti").replaceAll("_")).isEqualTo("am_far___");
    }
    
}
