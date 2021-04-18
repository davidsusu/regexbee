package hu.webarticum.regexbee;

import java.util.function.Supplier;
import java.util.regex.Pattern;

import hu.webarticum.regexbee.common.AlternationFragment;
import hu.webarticum.regexbee.common.ConcatenationFragment;
import hu.webarticum.regexbee.common.NamedGroupFragment;

// TODO: rename to BeeFragment or similar?
// TODO: add hashCode() and equals() to subtypes
@FunctionalInterface
public interface Fragment extends Supplier<String> {
    
    public default Pattern toPattern() {
        return Pattern.compile(get());
    }
    
    public default Fragment then(Fragment nextFragment) {
        return new ConcatenationFragment(this, nextFragment);
    }

    public default Fragment or(Fragment nextFragment) {
        return new AlternationFragment(this, nextFragment);
    }
    
    public default Fragment captureAs(String groupName) {
        return new NamedGroupFragment(this, groupName);
    }
    
    // TODO .optional([quantifierType]) .any([qT]) .more([qT])
    
}
