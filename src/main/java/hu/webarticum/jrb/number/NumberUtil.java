package hu.webarticum.jrb.number;

import java.math.BigInteger;

import hu.webarticum.jrb.Fragment;
import hu.webarticum.jrb.LazyFragment;

public class NumberUtil {

    public static Fragment unsignedIntBetween(
            BigInteger low, boolean lowInclusive, BigInteger high, boolean highInclusive) {

        BigInteger from = lowInclusive ? low : low.add(BigInteger.ONE);
        if (from.signum() == -1) {
            throw new IllegalArgumentException(String.format("Minimum value must not be negative: %s", from));
        }
        
        BigInteger to = highInclusive ? high : high.subtract(BigInteger.ONE);
        
        if (from.compareTo(to) > 0) {
            throw new IllegalArgumentException(String.format("Invalid range %s(%b)..%s(%b)",
                    low, lowInclusive, high, highInclusive));
        }
        
        return new LazyFragment(() -> new UnsignedIntBetweenGenerator().generate(from, to));
    }
    
}
