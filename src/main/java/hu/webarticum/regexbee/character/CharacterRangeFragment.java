package hu.webarticum.regexbee.character;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import hu.webarticum.regexbee.common.AbstractGeneratingFragment;
import hu.webarticum.regexbee.util.PatternUtil;

public class CharacterRangeFragment extends AbstractGeneratingFragment implements CharacterFragment {
    
    private final boolean positiveMatching;

    private final List<CharacterRange> ranges;
    
    
    public CharacterRangeFragment(char from, char to) {
        this(true, from, to);
    }

    public CharacterRangeFragment(boolean positive, char from, char to) {
        this(builder().withPositiveMatching(positive).addRange(from, to));
    }

    public CharacterRangeFragment(String chars) {
        this(true, chars);
    }

    public CharacterRangeFragment(boolean positive, String chars) {
        this(builder().withPositiveMatching(positive).addAll(chars));
    }

    private CharacterRangeFragment(CharacterRangeFragmentBuilder builder) {
        this.positiveMatching = builder.positiveMatching;
        this.ranges = cleanRanges(builder.ranges);
    }
    
    private List<CharacterRange> cleanRanges(List<CharacterRange> ranges) {
        ArrayList<CharacterRange> result = new ArrayList<>(ranges);
        if (ranges.size() < 2) {
            return ranges;
        }
        
        result.sort(this::sortRangesForClean);
        Iterator<CharacterRange> iterator = result.iterator();
        CharacterRange previous = iterator.next();
        while (iterator.hasNext()) {
            CharacterRange current = iterator.next();
            if (current.from == previous.from && current.to <= previous.to) {
                iterator.remove();
            } else {
                previous = current;
            }
        }
        result.trimToSize();
        return result;
    }
    
    private int sortRangesForClean(CharacterRange range1, CharacterRange range2) {
        int fromCmp = Character.compare(range1.from, range2.from);
        if (fromCmp != 0) {
            return fromCmp;
        }
        
        return 0 - Character.compare(range1.to, range2.to);
    }
    
    public static CharacterRangeFragmentBuilder builder() {
        return new CharacterRangeFragmentBuilder();
    }
    
    
    @Override
    protected String generate() {
        StringBuilder resultBuilder = new StringBuilder("[");
        if (!positiveMatching) {
            resultBuilder.append('^');
        }
        for (CharacterRange range : ranges) {
            resultBuilder.append(range);
        }
        resultBuilder.append(']');
        return resultBuilder.toString();
    }
    
    
    public static class CharacterRangeFragmentBuilder {

        private boolean positiveMatching = true;

        private final List<CharacterRange> ranges = new ArrayList<>();
        
        
        private CharacterRangeFragmentBuilder() {
            // use builder()
        }
        

        public CharacterRangeFragmentBuilder withPositiveMatching() {
            return withPositiveMatching(true);
        }

        public CharacterRangeFragmentBuilder withNegativeMatching() {
            return withPositiveMatching(false);
        }

        public CharacterRangeFragmentBuilder withPositiveMatching(boolean positiveMatching) {
            this.positiveMatching = positiveMatching;
            return this;
        }

        public CharacterRangeFragmentBuilder add(char c) {
            return addRange(c, c);
        }

        public CharacterRangeFragmentBuilder addAll(String chars) {
            return addAll(chars.toCharArray());
        }

        public CharacterRangeFragmentBuilder addAll(char... chars) {
            for (char c : chars) {
                ranges.add(new CharacterRange(c, c));
            }
            return this;
        }

        public CharacterRangeFragmentBuilder addAll(Collection<Character> chars) {
            for (char c : chars) {
                ranges.add(new CharacterRange(c, c));
            }
            return this;
        }

        public CharacterRangeFragmentBuilder addRange(char from, char to) {
            ranges.add(new CharacterRange(from, to));
            return this;
        }
        
        public CharacterRangeFragment build() {
            return new CharacterRangeFragment(this);
        }
        
    }
    
    
    private static class CharacterRange {
        
        private final char from;
        
        private final char to;
        
        
        private CharacterRange(char from, char to) {
            if (from > to) {
                throw new IllegalArgumentException(String.format(
                        "from can not be greater then to, but (%c=%d, %c=%2d) given",
                        from, (int) from, to, (int) to));
            }
            
            this.from = from;
            this.to = to;
        }
        
        
        @Override
        public String toString() {
            StringBuilder resultBuilder = new StringBuilder();
            resultBuilder.append(PatternUtil.escapeCharacterIfNecessary(from));
            if (to > from + 1) {
                resultBuilder.append('-');
            }
            if (to != from) {
                resultBuilder.append(PatternUtil.escapeCharacterIfNecessary(to));
            }
            return resultBuilder.toString();
        }
        
    }
    
}
