package hu.webarticum.jrb.core;

import java.util.function.Supplier;
import java.util.regex.Pattern;

public interface Fragment extends Supplier<String> {
    
    public default Pattern toPattern() {
        return Pattern.compile(get());
    }
    
}
