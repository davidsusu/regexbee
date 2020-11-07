package hu.webarticum.jrb.number;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

class IntBetweenBuilderTest {
    
    private static final String DEFAULT_TEXT =
            "-55 xxx x-10 -7.3 -0 00 p0 o+0 0.1 3 5+6 +11a 15.3 017 +0020 +36.2 45b 255 2410";
    
    private static final long DEFAULT_LOW = -12L;
    
    private static final long DEFAULT_HIGH = 255L;
    

    @Test
    void testSimpleUnsigned() {
        List<String> data = Arrays.asList(
                "-150", "-3", "0", "7", "24", "+52", "135", "984", "+1500");
        
        Pattern pattern = new IntBetweenBuilder().low(12, true).high(342, true)
                .denyPlusSign().build().toPattern();

        List<String> expected = Arrays.asList("24", "135");
        
        assertThat(filterFullMatch(pattern, data)).isEqualTo(expected);
    }

    @Test
    void testSimpleUnsignedVeryDifferentLength() {
        List<String> data = Arrays.asList(
                "-423543", "-243", "0", "32", "45", "132", "+541",
                "632", "1432", "23654", "111111", "567890", "1252407");
        
        Pattern pattern = new IntBetweenBuilder().low(45, true).high(235413, true)
                .denyPlusSign().build().toPattern();

        List<String> expected = Arrays.asList("45", "132", "632", "1432", "23654", "111111");
        
        assertThat(filterFullMatch(pattern, data)).isEqualTo(expected);
    }
    
    @Test
    void testSimpleUnsignedSameLength() {
        List<String> data = Arrays.asList(
                "-3214", "-24", "0", "24", "135", "+245", "370", "592", "+456", "893", "+1234");
        
        Pattern pattern = new IntBetweenBuilder().low(350, true).high(763, true)
                .denyPlusSign().build().toPattern();

        List<String> expected = Arrays.asList("370", "592");
        
        assertThat(filterFullMatch(pattern, data)).isEqualTo(expected);
    }

    @Test
    void testSimpleUnsignedSameLengthCommonPrefix() {
        List<String> data = Arrays.asList(
                "-229000", "-234", "0", "345", "+1432", "22350", "223450", "+223450", "228933", "2360728");
        
        Pattern pattern = new IntBetweenBuilder().low(223450, true).high(229499, true)
                .denyPlusSign().build().toPattern();

        List<String> expected = Arrays.asList("223450", "228933");
        
        assertThat(filterFullMatch(pattern, data)).isEqualTo(expected);
    }
    
    @Test
    void testSimpleUnsignedSame() {
        List<String> data = Arrays.asList(
                "-523", "-245", "0", "32", "245", "+245", "2450", "3986");
        
        Pattern pattern = new IntBetweenBuilder().low(245, true).high(245, true)
                .denyPlusSign().build().toPattern();

        List<String> expected = Arrays.asList("245");
        
        assertThat(filterFullMatch(pattern, data)).isEqualTo(expected);
    }
    
    @Test
    void testSimpleSigned() {
        List<String> data = Arrays.asList(
                "-523", "-193", "23", "-0", "0", "12", "+32", "4255", "+12012", "78523");
        
        Pattern pattern = new IntBetweenBuilder().low(-234, true).high(35673, true)
                .allowPlusSign().allowNegativeZero(false).build().toPattern();

        List<String> expected = Arrays.asList("-193", "23", "0", "12", "+32", "4255", "+12012");
        
        assertThat(filterFullMatch(pattern, data)).isEqualTo(expected);
    }

    @Test
    void testNegativeOnly() {
        List<String> data = Arrays.asList(
                "-1243", "-342", "-75", "-1", "-0", "0", "132", "532");
        
        Pattern pattern = new IntBetweenBuilder().low(-794, true).high(-45, true)
                .allowPlusSign().allowNegativeZero(false).build().toPattern();

        List<String> expected = Arrays.asList("-342", "-75");
        
        assertThat(filterFullMatch(pattern, data)).isEqualTo(expected);
    }

    @Test
    void testSimpleSignedFullRange() {
        Iterable<Integer> data = range(-10000, 10000);
        
        int low = -135;
        int high = 2410;
        
        Pattern pattern = new IntBetweenBuilder().low(low, true).high(high, true)
                .build().toPattern();
        
        Iterable<Integer> expected = range(low, high);
        
        assertThat(data).filteredOn(e -> pattern.matcher(e.toString()).matches())
                .containsExactlyElementsOf(expected);
    }
    
    @Test
    void testLowExclusive() {
        Iterable<Integer> data = range(0, 100);
        
        int low = 12;
        int high = 25;
        
        Pattern pattern = new IntBetweenBuilder().low(low, false).high(high, true)
                .build().toPattern();
        
        Iterable<Integer> expected = range(low + 1, high);
        
        assertThat(data).filteredOn(e -> pattern.matcher(e.toString()).matches())
                .containsExactlyElementsOf(expected);
    }

    @Test
    void testHighExclusive() {
        Iterable<Integer> data = range(0, 100);
        
        int low = 9;
        int high = 59;
        
        Pattern pattern = new IntBetweenBuilder().low(low, true).high(high, false)
                .build().toPattern();
        
        Iterable<Integer> expected = range(low, high - 1);
        
        assertThat(data).filteredOn(e -> pattern.matcher(e.toString()).matches())
                .containsExactlyElementsOf(expected);
    }

    @Test
    void testBothExclusive() {
        Iterable<Integer> data = range(0, 100);
        
        int low = 23;
        int high = 72;
        
        Pattern pattern = new IntBetweenBuilder().low(low, false).high(high, false)
                .build().toPattern();
        
        Iterable<Integer> expected = range(low + 1, high - 1);
        
        assertThat(data).filteredOn(e -> pattern.matcher(e.toString()).matches())
                .containsExactlyElementsOf(expected);
    }

    @Test
    void testDenyFalseFalseDigitSign() {
        Pattern pattern = new IntBetweenBuilder().low(DEFAULT_LOW, true).high(DEFAULT_HIGH, true)
                .denyPlusSign()
                .allowNegativeZero(false)
                .allowLeadingZeros(false)
                .boundPolicy(IntBetweenBuilder.BoundPolicy.DIGIT_SIGN)
                .build().toPattern();

        List<String> expected = Arrays.asList(
                "-10", "-7", "3", "0", "0", "1", "3", "5", "15", "3", "2", "45", "255");
        
        assertThat(matchAll(pattern, DEFAULT_TEXT)).isEqualTo(expected);
    }
    
    /*
    
    // TODO
    
    plusSignPolicy
        [*]  DENY
        [ ]  ALLOW
        [ ]  REQUIRE
    allowNegativeZero
        [*]  false
        [ ]  true
    allowLeadingZeros
        [*]  false
        [ ]  true
    boundPolicy
        [ ]  NONE
        [ ]  DIGIT_ONLY
        [*]  DIGIT_SIGN
        [ ]  DIGIT_SIGN_NOFRACTION
    
    cases:
        DENY, false, false, DIGIT_SIGN
        . . .
    
    */
    

    private Iterable<Integer> range(int low, int high) {
        return () -> IntStream.rangeClosed(low, high).iterator();
    }
    
    private List<String> filterFullMatch(Pattern pattern, List<String> lines) {
        return lines.stream()
                .filter(line -> pattern.matcher(line).matches())
                .collect(Collectors.toList());
    }

    private List<String> matchAll(Pattern pattern, String text) {
        List<String> result = new ArrayList<>();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

}
