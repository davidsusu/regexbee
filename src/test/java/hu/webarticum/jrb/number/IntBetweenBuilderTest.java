package hu.webarticum.jrb.number;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import hu.webarticum.jrb.Fragment;

class IntBetweenBuilderTest {

    @Test
    void helloTest() {
        Fragment fragment = new IntBetweenBuilder()
                //.low(22645).high(22983, true)
                .low(75).high(3463, true)
                .build();

        System.out.println(fragment.get());
        
        for (int i = 0; i < 5000; i++) {
            System.out.println(i + ": " + fragment.toPattern().matcher(i + "").matches());
        }
    }

}
