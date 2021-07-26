package hu.webarticum.regexbee.character;

import hu.webarticum.regexbee.common.AbstractGeneratingFragment;

// TODO improve! Make possible something like this: [\p{L}&&[^a-f]]
public class CharacterClassFragment extends AbstractGeneratingFragment implements CharacterFragment {
    
    private final boolean positiveMatching;
    
    private final CharacterRange range;
    
    
    public CharacterClassFragment(char from, char to) {
        this(from, to, true);
    }

    public CharacterClassFragment(char from, char to, boolean positive) {
        this.positiveMatching = positive;
        this.range = new CharacterRange(from, to);
    }
    
    
    @Override
    protected String generate() {
        StringBuilder resultBuilder = new StringBuilder("[");
        if (!positiveMatching) {
            resultBuilder.append('^');
        }
        resultBuilder.append(range.from);
        if (range.to > range.from + 1) {
            resultBuilder.append('-');
        }
        if (range.to != range.from) {
            resultBuilder.append(range.to);
        }
        resultBuilder.append(']');
        return resultBuilder.toString();
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
        
    }
    
}
