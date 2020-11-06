package hu.webarticum.jrb.number;

import java.math.BigInteger;

import hu.webarticum.jrb.Fragment;
import hu.webarticum.jrb.LazyFragment;

public class IntBetweenBuilder {
    
    private BigInteger low;
    
    private boolean lowInclusive;
    
    private BigInteger high;
    
    private boolean highInclusive;
    

    public SetHighBuilder low(long low) {
        return low(BigInteger.valueOf(low), true);
    }
    
    public SetHighBuilder low(BigInteger low) {
        return low(low, true);
    }

    public SetHighBuilder low(long low, boolean inclusive) {
        return low(BigInteger.valueOf(low), inclusive);
    }
    
    public SetHighBuilder low(BigInteger low, boolean inclusive) {
        this.low = low;
        this.lowInclusive = inclusive;
        return new SetHighBuilder();
    }
    
    
    public class SetHighBuilder {
        
        private SetHighBuilder() {
            // hidden constructor
        }
        

        public TerminalBuilder high(long high) {
            return high(BigInteger.valueOf(high), true);
        }
        
        public TerminalBuilder high(BigInteger high) {
            return high(high, false);
        }

        public TerminalBuilder high(long high, boolean inclusive) {
            return high(BigInteger.valueOf(high), inclusive);
        }
        
        public TerminalBuilder high(BigInteger high, boolean inclusive) {
            IntBetweenBuilder.this.high = high;
            IntBetweenBuilder.this.highInclusive = inclusive;
            return new TerminalBuilder();
        }
        
    }
    
    
    public class TerminalBuilder {
        
        private TerminalBuilder() {
            // hidden constructor
        }
        
        
        // TODO: more options
        // add boundaries
        // advanced boundaries? e.g. (?!\.\d)
        // allow leading zeros
        // signed/unsigned, allow plus sign
        // etc.
        

        public Fragment build() {
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
    
}
