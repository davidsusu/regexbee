package hu.webarticum.jrb.core;

import java.util.function.Supplier;
import java.util.regex.Pattern;

@FunctionalInterface
public interface Fragment extends Supplier<String> {
    
    public default Pattern toPattern() {
        return Pattern.compile(get());
    }
    
}
