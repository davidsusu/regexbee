package hu.webarticum.regexbee.common;

import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {
    
    private Supplier<T> supplier;
    
    private boolean computed = false;
    
    private T value = null;
    
    
    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }
    
    
    @Override
    public T get() {
        if (!computed) {
            synchronized (this) {
                if (!computed) {
                    value = supplier.get();
                    supplier = null;
                    computed = true;
                }
            }
        }
        return value;
    }
    
}
