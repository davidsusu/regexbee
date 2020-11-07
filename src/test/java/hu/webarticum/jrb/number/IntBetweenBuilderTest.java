package hu.webarticum.jrb.number;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class IntBetweenBuilderTest {

    @Test
    void testSimpleUnsigned() {
        List<String> data = Arrays.asList(
                "-150", "-3", "0", "7", "24", "+52", "135", "984", "+1500");
        
        Pattern pattern = new IntBetweenBuilder().low(12, true).high(342, true)
                .denyPlus().build().toPattern();

        List<String> expected = Arrays.asList("24", "135");
        
        assertThat(filterFullMatch(pattern, data)).isEqualTo(expected);
    }

    @Test
    void testSimpleUnsignedVeryDifferentLength() {
        List<String> data = Arrays.asList(
                "-423543", "-243", "0", "32", "45", "132", "+541",
                "632", "1432", "23654", "111111", "567890", "1252407");
        
        Pattern pattern = new IntBetweenBuilder().low(45, true).high(235413, true)
                .denyPlus().build().toPattern();

        List<String> expected = Arrays.asList("45", "132", "632", "1432", "23654", "111111");
        
        assertThat(filterFullMatch(pattern, data)).isEqualTo(expected);
    }
    
    @Test
    void testSimpleUnsignedSameLength() {
        List<String> data = Arrays.asList(
                "-3214", "-24", "0", "24", "135", "+245", "370", "592", "+456", "893", "+1234");
        
        Pattern pattern = new IntBetweenBuilder().low(350, true).high(763, true)
                .denyPlus().build().toPattern();

        List<String> expected = Arrays.asList("370", "592");
        
        assertThat(filterFullMatch(pattern, data)).isEqualTo(expected);
    }

    @Test
    void testSimpleUnsignedSameLengthCommonPrefix() {
        List<String> data = Arrays.asList(
                "-229000", "-234", "0", "345", "+1432", "22350", "223450", "+223450", "228933", "2360728");
        
        Pattern pattern = new IntBetweenBuilder().low(223450, true).high(229499, true)
                .denyPlus().build().toPattern();

        List<String> expected = Arrays.asList("223450", "228933");
        
        assertThat(filterFullMatch(pattern, data)).isEqualTo(expected);
    }
    
    @Test
    void testSimpleUnsignedSame() {
        List<String> data = Arrays.asList(
                "-523", "-245", "0", "32", "245", "+245", "2450", "3986");
        
        Pattern pattern = new IntBetweenBuilder().low(245, true).high(245, true)
                .denyPlus().build().toPattern();

        List<String> expected = Arrays.asList("245");
        
        assertThat(filterFullMatch(pattern, data)).isEqualTo(expected);
    }
    
    @Test
    void testSimpleSigned() {
        List<String> data = Arrays.asList(
                "-523", "-193", "23", "-0", "0", "12", "+32", "4255", "+12012", "78523");
        
        Pattern pattern = new IntBetweenBuilder().low(-234, true).high(35673, true)
                .allowPlus().allowNegativeZero(false).build().toPattern();

        List<String> expected = Arrays.asList("-193", "23", "0", "12", "+32", "4255", "+12012");
        
        assertThat(filterFullMatch(pattern, data)).isEqualTo(expected);
    }
    
    @Test
    void testSimpleSignedFullRange() {
        List<String> data = range(-1000, 1000);
        
        int low = -13;
        int high = 240;
        
        Pattern pattern = new IntBetweenBuilder().low(low, true).high(high, true)
                .build().toPattern();
        
        List<String> expected = range(low, high);
        
        assertThat(filterFullMatch(pattern, data)).isEqualTo(expected);
    }
    
    @Test
    void testNegativeOnly() {
        List<String> data = Arrays.asList(
                "-1243", "-342", "-75", "-1", "-0", "0", "132", "532");
        
        Pattern pattern = new IntBetweenBuilder().low(-794, true).high(-45, true)
                .allowPlus().allowNegativeZero(false).build().toPattern();

        List<String> expected = Arrays.asList("-342", "-75");
        
        assertThat(filterFullMatch(pattern, data)).isEqualTo(expected);
    }

    @Test
    void testXXX() {
        // TODO
    }
    
    
    // TODO: test all settings with matching on text
    

    private List<String> range(int low, int high) {
        List<String> result = new ArrayList<>();
        for (int i = low; i <= high; i++) {
            result.add("" + i);
        }
        return result;
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
