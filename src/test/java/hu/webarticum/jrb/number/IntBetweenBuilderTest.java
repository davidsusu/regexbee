package hu.webarticum.jrb.number;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import hu.webarticum.jrb.Fragment;

class IntBetweenBuilderTest {

    @Test
    void helloTest() {
        Fragment fragment = new IntBetweenBuilder()
                //.low(22645).high(22983, true)
                .low(794).high(34659, true)
                .build();
        
        System.out.println(fragment.get());
    }

}
