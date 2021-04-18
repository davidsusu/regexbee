package hu.webarticum.regexbee.common;

import hu.webarticum.regexbee.Fragment;

public abstract class AbstractGeneratingFragment implements Fragment {

    private final Lazy<String> supplier;
    
    
    public AbstractGeneratingFragment() {
        this.supplier = new Lazy<>(this::generate);
    }
    
    
    @Override
    public String get() {
        return supplier.get();
    }
    
    protected abstract String generate();

}
