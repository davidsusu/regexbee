package hu.webarticum.regexbee.common;

import java.util.function.Supplier;

import hu.webarticum.regexbee.BeeFragment;

public class LazyFragment extends Lazy<String> implements BeeFragment {

    public LazyFragment(Supplier<String> supplier) {
        super(supplier);
    }

}
