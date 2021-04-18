package hu.webarticum.regexbee.common;

import hu.webarticum.regexbee.BeeFragment;

public abstract class AbstractGeneratingFragment implements BeeFragment {

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
