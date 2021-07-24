package hu.webarticum.regexbee.common;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import hu.webarticum.regexbee.BeeFragment;

public class ModifierGroupFragment extends AbstractGeneratingFragment {
    
    private static final Map<Integer, Character> SWITCH_LETTER_MAP = new LinkedHashMap<>();
    static {
        SWITCH_LETTER_MAP.put(Pattern.CASE_INSENSITIVE, 'i');
        SWITCH_LETTER_MAP.put(Pattern.COMMENTS, 'x');
        SWITCH_LETTER_MAP.put(Pattern.DOTALL, 's');
        SWITCH_LETTER_MAP.put(Pattern.MULTILINE, 'm');
        SWITCH_LETTER_MAP.put(Pattern.UNICODE_CASE, 'u');
        SWITCH_LETTER_MAP.put(Pattern.UNICODE_CASE, 'U');
        SWITCH_LETTER_MAP.put(Pattern.UNIX_LINES, 'd');
    }
    
    
    private final BeeFragment baseFragment;
    
    private final String prefix;
    

    public ModifierGroupFragment(BeeFragment baseFragment, int switchOn, int switchOff) {
        this.baseFragment = baseFragment;
        this.prefix = composePrefix(switchOn, switchOff);
    }
    
    private String composePrefix(int switchOn, int switchOff) {
        StringBuilder resultBuilder = new StringBuilder("(?");
        resultBuilder.append(toLetters(switchOn));
        String offLetters = toLetters(switchOff);
        if (!offLetters.isEmpty()) {
            resultBuilder.append('-');
            resultBuilder.append(offLetters);
        }
        resultBuilder.append(':');
        return resultBuilder.toString();
    }
    
    private String toLetters(int switches) {
        StringBuilder resultBuilder = new StringBuilder();
        for (Map.Entry<Integer, Character> entry : SWITCH_LETTER_MAP.entrySet()) {
            int switchNumber = entry.getKey();
            char switchLetter = entry.getValue();
            if ((switches & switchNumber) != 0) {
                resultBuilder.append(switchLetter);
            }
        }
        return resultBuilder.toString();
    }
    
    
    @Override
    protected String generate() {
        StringBuilder resultBuilder = new StringBuilder(prefix);
        resultBuilder.append(baseFragment.get());
        resultBuilder.append(')');
        return resultBuilder.toString();
    }

}
