package hu.webarticum.regexbee.core;

public class SimpleFragment implements Fragment {
    
    private final String pattern;

    public SimpleFragment(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String get() {
        return pattern;
    }
    
}
