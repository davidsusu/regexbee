package hu.webarticum.regexbee.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import hu.webarticum.regexbee.Bee;
import hu.webarticum.regexbee.BeeFragment;

public class CustomFragmentExample {

    public static void main(String[] args) {
        BeeFragment fragment = Bee
                .then(Bee.BEGIN)
                .then(new SeparatedByCommaFragment(
                        Bee.UNSIGNED_INT.as("number"),
                        Bee.ASCII_WORD.as("word"),
                        Bee.oneFixedOf("lorem", "ipsum").as("keyword")))
                .then(Bee.END);
        Pattern pattern = fragment.toPattern(Pattern.MULTILINE);
        String lines =
                "12,apple,lorem\n" +
                "3,banana,ipsum\n" +
                "999,orange,lorem";
        Matcher matcher = pattern.matcher(lines);
        while (matcher.find()) {
            System.out.println("number: " + matcher.group("number"));
            System.out.println("word: " + matcher.group("word"));
            System.out.println("keyword: " + matcher.group("keyword"));
            System.out.println();
        }
    }
    
    
    private static class SeparatedByCommaFragment implements BeeFragment {
        
        private final List<BeeFragment> fragments;
        
        
        private SeparatedByCommaFragment(BeeFragment... fragments) {
            this(Arrays.asList(fragments));
        }
        
        private SeparatedByCommaFragment(Collection<BeeFragment> fragments) {
            this.fragments = new ArrayList<>(fragments);
        }
        
        
        @Override
        public String get() {
            return fragments.stream().map(BeeFragment::get).collect(Collectors.joining(","));
        }
        
    }
    
}
