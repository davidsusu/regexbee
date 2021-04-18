package hu.webarticum.regexbee;

import java.util.regex.Pattern;

public class SimpleFragment implements Fragment {
    
    private final String pattern;

    
    public SimpleFragment(String pattern) {
        this.pattern = pattern;
    }

    public SimpleFragment(String pattern, boolean checkSyntax) {
        if (checkSyntax) {
            Pattern.compile(pattern);
        }
        
        this.pattern = pattern;
    }
    

    @Override
    public String get() {
        return pattern;
    }
    
}
