package hu.webarticum.regexbee;

import java.util.function.Supplier;
import java.util.regex.Pattern;

import hu.webarticum.regexbee.common.ListFragment;

@FunctionalInterface
public interface Fragment extends Supplier<String> {
    
    public default Pattern toPattern() {
        return Pattern.compile(get());
    }
    
    public default Fragment then(Fragment nextFragment) {
        return new ListFragment(this, nextFragment);
    }
    
    // TODO .optional([quantifierType]) .any([qT]) .more([qT]) .or()
    
}
