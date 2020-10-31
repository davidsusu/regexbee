package hu.webarticum.jrb;

import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {
    
    private final Supplier<T> supplier;
    
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
                    computed = true;
                }
            }
        }
        return value;
    }
    
}
