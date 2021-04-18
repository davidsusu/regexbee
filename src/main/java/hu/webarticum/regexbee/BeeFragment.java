package hu.webarticum.regexbee;

import java.util.function.Supplier;
import java.util.regex.Pattern;

import hu.webarticum.regexbee.common.AlternationFragment;
import hu.webarticum.regexbee.common.ConcatenationFragment;
import hu.webarticum.regexbee.common.NamedGroupFragment;

// TODO: rename to BeeFragment or similar?
// TODO: add hashCode() and equals() to subtypes
@FunctionalInterface
public interface BeeFragment extends Supplier<String> {
    
    public default Pattern toPattern() {
        return Pattern.compile(get());
    }
    
    public default BeeFragment then(BeeFragment nextFragment) {
        return new ConcatenationFragment(this, nextFragment);
    }

    public default BeeFragment or(BeeFragment nextFragment) {
        return new AlternationFragment(this, nextFragment);
    }
    
    public default BeeFragment captureAs(String groupName) {
        return new NamedGroupFragment(this, groupName);
    }
    
    // TODO .optional([quantifierType]) .any([qT]) .more([qT])
    
}
