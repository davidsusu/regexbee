package hu.webarticum.jrb;

import java.util.function.Supplier;
import java.util.regex.Pattern;

public interface Fragment extends Supplier<String> {
    
    public default Pattern toPattern() {
        return Pattern.compile(get());
    }
    
}
