package hu.webarticum.regexbee;

import java.util.function.Supplier;
import java.util.regex.Pattern;

import hu.webarticum.regexbee.common.AlternationFragment;
import hu.webarticum.regexbee.common.ConcatenationFragment;
import hu.webarticum.regexbee.common.NamedGroupFragment;
import hu.webarticum.regexbee.common.QuantifierFragment;

// TODO: add hashCode() and equals() to subtypes (is it really needed?)
// TODO: Serializable?
@FunctionalInterface
public interface BeeFragment extends Supplier<String> {
    
    public default Pattern toPattern() {
        return Pattern.compile(get());
    }

    public default Pattern toPattern(int flags) {
        return Pattern.compile(get(), flags);
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

    public default BeeFragment optional() {
        return new QuantifierFragment(this, 0, 1);
    }

    public default BeeFragment optional(Greediness greediness) {
        return new QuantifierFragment(this, 0, 1, greediness);
    }

    public default BeeFragment any() {
        return new QuantifierFragment(this, 0, QuantifierFragment.MAX_REPETITIONS);
    }

    public default BeeFragment any(Greediness greediness) {
        return new QuantifierFragment(this, 0, QuantifierFragment.MAX_REPETITIONS, greediness);
    }

    public default BeeFragment more() {
        return new QuantifierFragment(this, 1, QuantifierFragment.MAX_REPETITIONS);
    }

    public default BeeFragment more(Greediness greediness) {
        return new QuantifierFragment(this, 1, QuantifierFragment.MAX_REPETITIONS, greediness);
    }

    public default BeeFragment occur(int number) {
        return new QuantifierFragment(this, number, number);
    }

    public default BeeFragment occurAtLeast(int minimum) {
        return new QuantifierFragment(this, minimum, QuantifierFragment.MAX_REPETITIONS);
    }

    public default BeeFragment occurAtMost(int maximum) {
        return new QuantifierFragment(this, 0, maximum);
    }

    public default BeeFragment occur(int minimum, int maximum) {
        return new QuantifierFragment(this, minimum, maximum);
    }

    public default BeeFragment occur(int minimum, int maximum, Greediness greediness) {
        return new QuantifierFragment(this, minimum, maximum, greediness);
    }

}
