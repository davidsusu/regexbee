package hu.webarticum.regexbee.core;

import java.util.function.Supplier;

public class LazyFragment extends Lazy<String> implements Fragment {

    public LazyFragment(Supplier<String> supplier) {
        super(supplier);
    }
    
}
