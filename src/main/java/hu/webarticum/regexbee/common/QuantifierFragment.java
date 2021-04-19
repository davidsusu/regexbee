package hu.webarticum.regexbee.common;

import hu.webarticum.regexbee.BeeFragment;
import hu.webarticum.regexbee.Greediness;
import hu.webarticum.regexbee.util.PatternUtil;

public class QuantifierFragment extends AbstractGeneratingFragment {
    
    public static final int MAX_REPETITIONS = 0x7FFFFFFF;
    
    
    private final BeeFragment baseFragment;
    
    private final int minimum;
    
    private final int maximum;
    
    private final Greediness greediness;
    

    public QuantifierFragment(BeeFragment baseFragment, int minimum, int maximum) {
        this(baseFragment, minimum, maximum, Greediness.GREEDY);
    }
    
    public QuantifierFragment(
            BeeFragment baseFragment, int minimum, int maximum, Greediness greediness) {
        
        checkBounds(minimum, maximum);
        
        this.baseFragment = baseFragment;
        this.minimum = minimum;
        this.maximum = maximum;
        this.greediness = greediness;
    }
    
    private static final void checkBounds(int minimum, int maximum) {
        if (minimum < 0) {
            throw new IllegalArgumentException(String.format(
                    "Minimum must be non-negative, but %d given",
                    minimum));
        }

        if (maximum > MAX_REPETITIONS) {
            throw new IllegalArgumentException(String.format(
                    "Maximum can not be greater than %d, but %d given",
                    MAX_REPETITIONS,
                    maximum));
        }
        
        if (maximum < minimum) {
            throw new IllegalArgumentException(String.format(
                    "Minimum can not be greater than maximum, but %d > %d",
                    minimum,
                    maximum));
        }
    }


    @Override
    protected String generate() {
        if (minimum == 0 && maximum == 0) {
            return "";
        }

        String basePattern = baseFragment.get();
        
        if (minimum == 1 && maximum == 1) {
            return basePattern;
        }
        
        
        boolean isAtomic = PatternUtil.isAtomicPattern(basePattern);
        String atomicBasePattern = isAtomic ? basePattern : PatternUtil.wrapPattern(basePattern);
        
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(atomicBasePattern);
        resultBuilder.append(generateQuantifierString());
        resultBuilder.append(greediness.modifier());
        
        return resultBuilder.toString();
    }
    
    private String generateQuantifierString() {
        if (minimum == 0 && maximum == 1) {
            return "?";
        } else if (minimum == 0 && maximum == MAX_REPETITIONS) {
            return "*";
        } else if (minimum == 1 && maximum == MAX_REPETITIONS) {
            return "+";
        } else if (minimum == maximum) {
            return "{" + minimum + "}";
        } else if (minimum == 0) {
            return "{," + maximum + "}";
        } else if (maximum == MAX_REPETITIONS) {
            return "{" + minimum + ",}";
        } else {
            return "{" + minimum + "," + maximum + "}";
        }
    }

}
