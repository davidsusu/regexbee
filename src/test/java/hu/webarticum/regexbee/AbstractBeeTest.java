package hu.webarticum.regexbee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

abstract public class AbstractBeeTest {

    protected static boolean match(BeeFragment fragment, String input) {
        return fragment.toPattern().matcher(input).matches();
    }
    
    protected static List<String> matchAll(BeeFragment fragment, String input) {
        List<String> result = new ArrayList<>();
        Matcher matcher = fragment.toPattern().matcher(input);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }
    
    protected static List<Integer> matchAllPositions(BeeFragment fragment, String input) {
        List<Integer> result = new ArrayList<>();
        Matcher matcher = fragment.toPattern().matcher(input);
        while (matcher.find()) {
            result.add(matcher.start());
        }
        return result;
    }

    protected List<String> filterMatching(BeeFragment fragment, String... subjects) {
        return filterMatching(fragment, Arrays.asList(subjects));
    }

    protected List<String> filterMatching(BeeFragment fragment, List<String> subjects) {
        return subjects.stream()
                .filter(fragment.toPattern().asMatchPredicate())
                .collect(Collectors.toList());
    }

    protected static Matcher matcher(BeeFragment fragment, String input) {
        Matcher matcher = fragment.toPattern().matcher(input);
        if (!matcher.find()) {
            throw new IllegalArgumentException("No match found");
        }
        return matcher;
    }
    
}
