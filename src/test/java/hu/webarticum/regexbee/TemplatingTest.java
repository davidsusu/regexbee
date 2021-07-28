package hu.webarticum.regexbee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import hu.webarticum.regexbee.template.BeeTemplate;

class TemplatingTest extends AbstractBeeTest {

    @Test
    void testSubstituteSelf() {
        BeeFragment fragment = Bee.placeholder().toTemplate().substitute(Bee.UNSIGNED_INT);
        assertThat(match(fragment, "12")).isTrue();
        assertThat(match(fragment, "aaa")).isFalse();
    }

    @Test
    void testSingleParameter() {
        BeeTemplate template = Bee
                .then(Bee.fixed("((("))
                .then(Bee.placeholder())
                .then(Bee.fixed(")))"))
                .toTemplate();
        
        BeeFragment substitutedFragment1 = template.substitute(Bee.intBetween(3, 12));
        assertThat(match(substitutedFragment1, "(((7)))")).isTrue();
        assertThat(match(substitutedFragment1, "(((15)))")).isFalse();
        assertThat(match(substitutedFragment1, "11")).isFalse();
        
        BeeFragment substitutedFragment2 = template.substitute(Bee.ASCII_WORD);
        assertThat(match(substitutedFragment2, "(((lorem)))")).isTrue();
        assertThat(match(substitutedFragment2, "(((!!!)))")).isFalse();
        assertThat(match(substitutedFragment2, "ipsum")).isFalse();
    }

    @Test
    void testWrongParameterCount() {
        BeeTemplate template = Bee
                .then(Bee.fixed("lorem"))
                .then(Bee.placeholder())
                .then(Bee.fixed("ipsum"))
                .then(Bee.placeholder())
                .then(Bee.fixed("dolor"))
                .toTemplate();
        assertThatThrownBy(() -> template.substitute()).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> template.substitute(Bee.WORD)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> template.substitute(Bee.WORD, Bee.WORD, Bee.WORD))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    void testMultipleParameters() {
        BeeTemplate template = Bee
                .then(Bee.placeholder()
                        .or(Bee.fixed("---")))
                .then(Bee.fixed(", "))
                .then(Bee.placeholder().as("second"))
                .then(Bee.fixed(", "))
                .then(Bee.fixed("---")
                        .or(Bee.fixed("!")
                                .then(Bee.placeholder())))
                .toTemplate();

        BeeFragment substitutedFragment1 = template.substitute(
                Bee.UNSIGNED_INT, Bee.ASCII_WORD, Bee.oneCharOf("abxyz"));
        assertThat(matcher(substitutedFragment1, "---, lorem, !b").group("second")).isEqualTo("lorem");
        assertThat(matcher(substitutedFragment1, "12, ipsum, ---").group("second")).isEqualTo("ipsum");
        assertThat(match(substitutedFragment1, "lorem, ipsum, ---")).isFalse();
        assertThat(match(substitutedFragment1, "15, ipsum, !m")).isFalse();

        BeeFragment substitutedFragment2 = template.substitute(
                Bee.CHAR, Bee.CHAR, Bee.CHAR);
        assertThat(matcher(substitutedFragment2, "a, b, !c").group("second")).isEqualTo("b");
        assertThat(matcher(substitutedFragment2, "---, x, ---").group("second")).isEqualTo("x");
        assertThat(match(substitutedFragment2, "lorem, ipsum, ---")).isFalse();
        assertThat(match(substitutedFragment2, "g, h, i")).isFalse();
    }

    @Test
    void testValidParameterNames() { // NOSONAR
        Bee.placeholder("a");
        Bee.placeholder("lorem");
        Bee.placeholder("12");
        Bee.placeholder("3_xxx");
    }

    @Test
    void testInvalidParameterNames() {
        assertThatThrownBy(() -> Bee.placeholder("")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Bee.placeholder("a/")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Bee.placeholder("//x//")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Bee.placeholder("!!!")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testMissingNamedParameter() {
        BeeTemplate template = Bee
                .then(Bee.placeholder("p1"))
                .then(Bee.SPACE)
                .then(Bee.placeholder("p2"))
                .toTemplate();

        Map<String, BeeFragment> parameters1 = new HashMap<String, BeeFragment>();
        parameters1.put("p2", Bee.CHAR);
        assertThatThrownBy(() -> template.substitute(parameters1))
                .isInstanceOf(IllegalArgumentException.class);
        
        Map<String, BeeFragment> parameters2 = new HashMap<String, BeeFragment>();
        parameters2.put("p1", Bee.CHAR);
        parameters2.put("pp2", Bee.WORD);
        assertThatThrownBy(() -> template.substitute(parameters2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testNamedParameters() {
        BeeTemplate template = Bee
                .then(Bee.placeholder("p1"))
                .then(Bee.SPACE)
                .then(Bee.placeholder("p2"))
                .then(Bee.SPACE)
                .then(Bee.placeholder("p1"))
                .then(Bee.SPACE)
                .then(Bee.placeholder("p1")
                        .or(Bee.placeholder("p2")))
                .then(Bee.SPACE)
                .then(Bee.placeholder("p3"))
                .toTemplate();

        Map<String, BeeFragment> parameters1 = new HashMap<String, BeeFragment>();
        parameters1.put("p1", Bee.fixed("*").more());
        parameters1.put("p2", Bee.UNSIGNED_INT);
        parameters1.put("p3", Bee.fixed("%").more());
        BeeFragment substitutedFragment1 = template.substitute(parameters1);
        assertThat(match(substitutedFragment1, "*** 12 *** *** %%%")).isTrue();
        assertThat(match(substitutedFragment1, "*** 12 * 34 %%")).isTrue();
        assertThat(match(substitutedFragment1, "*** x *** *** %%%")).isFalse();
        assertThat(match(substitutedFragment1, "*** 43  *** %")).isFalse();
        
        Map<String, BeeFragment> parameters2 = new HashMap<String, BeeFragment>();
        parameters2.put("p1", Bee.fixed("0").more());
        parameters2.put("p2", Bee.SPACE.any());
        parameters2.put("p3", Bee.NOTHING);
        BeeFragment substitutedFragment2 = template.substitute(parameters2);
        assertThat(match(substitutedFragment2, "00    0 00 ")).isTrue();
        assertThat(match(substitutedFragment2, "0  00  ")).isTrue();
        assertThat(match(substitutedFragment2, "0 00  ")).isFalse();
        assertThat(match(substitutedFragment2, "0   0 0  ")).isFalse();
        
    }

}
