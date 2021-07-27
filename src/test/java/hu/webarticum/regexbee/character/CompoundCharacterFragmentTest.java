package hu.webarticum.regexbee.character;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import hu.webarticum.regexbee.AbstractBeeTest;
import hu.webarticum.regexbee.BeeFragment;

class CompoundCharacterFragmentTest extends AbstractBeeTest {

    @Test
    void testSimple() {
        BeeFragment fragment = new CompoundCharacterFragment(
                new CharacterRangeFragment('x', 'z'),
                PredefinedCharacterFragment.UNICODE_DIGIT);
        assertThat(filterMatching(fragment, "a", "d", "x", "y", "2", "2a", "\u0664"))
                .containsExactly("x", "y", "2", "\u0664");
    }

    @Test
    void testNegative() {
        BeeFragment fragment = new CompoundCharacterFragment(
                false,
                new CharacterRangeFragment('x', 'z'),
                PredefinedCharacterFragment.UNICODE_DIGIT);
        assertThat(filterMatching(fragment, "a", "d", "x", "y", "2", "2a", "\u0664"))
                .containsExactly("a", "d");
    }

    @Test
    void testComplex() {
        BeeFragment fragment = new CompoundCharacterFragment(
                new CompoundCharacterFragment(
                        new CharacterRangeFragment('a', 'f'),
                        new CharacterRangeFragment('d', 'g')),
                new CharacterRangeFragment(false, 'b', 'x'));
        assertThat(filterMatching(fragment, "a", "d", "g", "h", "x", "y", "2", "2a", "\u0664"))
                .containsExactly("a", "d", "g", "y", "2", "\u0664");
    }

}
