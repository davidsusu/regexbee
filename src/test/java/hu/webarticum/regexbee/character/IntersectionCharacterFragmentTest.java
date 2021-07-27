package hu.webarticum.regexbee.character;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import hu.webarticum.regexbee.AbstractBeeTest;
import hu.webarticum.regexbee.BeeFragment;

class IntersectionCharacterFragmentTest extends AbstractBeeTest {

    @Test
    void testSimple() {
        BeeFragment fragment = new IntersectionCharacterFragment(
                new CharacterRangeFragment('a', 'f'),
                new CharacterRangeFragment('d', 'h'));
        assertThat(filterMatching(fragment, "a", "d", "f", "g", "h", "2", "2a"))
                .containsExactly("d", "f");
    }

    @Test
    void testComplex() {
        BeeFragment fragment = new IntersectionCharacterFragment(
                new CompoundCharacterFragment(
                        new CharacterRangeFragment("acefknop"),
                        new CharacterRangeFragment('g', 'k')),
                new IntersectionCharacterFragment(
                        new CharacterRangeFragment('e', 'i'),
                        new CharacterRangeFragment('f', 'n')));
        assertThat(filterMatching(fragment, "a", "b", "c", "f", "g", "h", "i", "k", "n", "p", "q", "2a", "F"))
                .containsExactly("f", "g", "h", "i");
    }
    
}
