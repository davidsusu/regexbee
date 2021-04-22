package hu.webarticum.regexbee.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NamedBackreferenceTest {

    @Test
    void testValidName() {
        NamedBackreferenceFragment fragment = new NamedBackreferenceFragment("ggg");
        String expected = "\\k<ggg>";
        
        assertThat(fragment.get()).isEqualTo(expected);
    }
    
}