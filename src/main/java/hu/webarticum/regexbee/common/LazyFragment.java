package hu.webarticum.regexbee.common;

import java.util.function.Supplier;

import hu.webarticum.regexbee.Fragment;

public class LazyFragment extends Lazy<String> implements Fragment {

    public LazyFragment(Supplier<String> supplier) {
        super(supplier);
    }

}
