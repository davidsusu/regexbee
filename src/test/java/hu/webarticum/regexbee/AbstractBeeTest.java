package hu.webarticum.regexbee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

abstract public class AbstractBeeTest {

    protected static boolean match(BeeFragment fragment, String input) {
        return match(fragment, 0, input);
    }

    protected static boolean match(BeeFragment fragment, int modifiers, String input) {
        return fragment.toPattern(modifiers).matcher(input).matches();
    }

    protected static boolean find(BeeFragment fragment, String input) {
        return find(fragment, 0, input);
    }

    protected static boolean find(BeeFragment fragment, int modifiers, String input) {
        return fragment.toPattern(modifiers).matcher(input).find();
    }
    
    protected static List<String> matchAll(BeeFragment fragment, String input) {
        return matchAll(fragment, 0, input);
    }

    protected static List<String> matchAll(BeeFragment fragment, int modifiers, String input) {
        List<String> result = new ArrayList<>();
        Matcher matcher = fragment.toPattern(modifiers).matcher(input);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }
    
    protected static List<Integer> matchAllPositions(BeeFragment fragment, String input) {
        return matchAllPositions(fragment, 0, input);
    }

    protected static List<Integer> matchAllPositions(BeeFragment fragment, int modifiers, String input) {
        List<Integer> result = new ArrayList<>();
        Matcher matcher = fragment.toPattern(modifiers).matcher(input);
        while (matcher.find()) {
            result.add(matcher.start());
        }
        return result;
    }

    protected List<String> filterMatching(BeeFragment fragment, String... subjects) {
        return filterMatching(fragment, 0, subjects);
    }

    protected List<String> filterMatching(BeeFragment fragment, int modifiers, String... subjects) {
        return filterMatching(fragment, modifiers, Arrays.asList(subjects));
    }

    protected List<String> filterMatching(BeeFragment fragment, List<String> subjects) {
        return filterMatching(fragment, 0, subjects);
    }
    
    protected List<String> filterMatching(BeeFragment fragment, int modifiers, List<String> subjects) {
        return subjects.stream()
                .filter(fragment.toPattern(modifiers).asMatchPredicate())
                .collect(Collectors.toList());
    }

    protected static Matcher matcher(BeeFragment fragment, String input) {
        return matcher(fragment, 0, input);
    }

    protected static Matcher matcher(BeeFragment fragment, int modifiers, String input) {
        Matcher matcher = fragment.toPattern(modifiers).matcher(input);
        if (!matcher.find()) {
            throw new IllegalArgumentException("No match found");
        }
        return matcher;
    }
    
}
