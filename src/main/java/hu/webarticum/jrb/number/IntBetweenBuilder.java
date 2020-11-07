package hu.webarticum.jrb.number;

import java.math.BigInteger;

import hu.webarticum.jrb.core.Fragment;
import hu.webarticum.jrb.core.LazyFragment;

public class IntBetweenBuilder {
    
    public enum PlusSignPolicy { DENY, ALLOW, REQUIRE }
    
    public enum BoundPolicy { NONE, DIGIT_ONLY, DIGIT_SIGN, DIGIT_SIGN_NOFRACTION }
    
    
    private BigInteger low;
    
    private boolean lowInclusive;
    
    private BigInteger high;
    
    private boolean highInclusive;
    

    private PlusSignPolicy plusSignPolicy = PlusSignPolicy.DENY;
    
    private boolean allowNegativeZero = false;
    
    private boolean allowLeadingZeros = false;

    private BoundPolicy boundPolicy = BoundPolicy.DIGIT_SIGN;
    
    
    private String generate(BigInteger from, BigInteger to) {
        UnsignedIntBetweenGenerator generator = new UnsignedIntBetweenGenerator();
        
        StringBuilder resultBuilder = new StringBuilder();
        
        boolean hasNegative = from.signum() == -1;
        boolean hasNonNegative = to.signum() != -1;
        
        if (hasNegative && hasNonNegative) {
            resultBuilder.append("(?:");
        }

        if (hasNegative) {
            resultBuilder.append("\\-");
            if (allowLeadingZeros) {
                resultBuilder.append("0*");
            }
            
            BigInteger subFrom;
            if (hasNonNegative) {
                subFrom = allowNegativeZero ? BigInteger.ZERO : BigInteger.ONE;
            } else {
                subFrom = to.abs();
            }
            
            BigInteger subTo = from.abs();
            
            resultBuilder.append(generator.generate(subFrom, subTo));
        }
        
        if (hasNonNegative) {
            if (hasNegative) {
                resultBuilder.append('|');
            }
            
            boolean allowPlus = (plusSignPolicy == PlusSignPolicy.ALLOW);
            if (plusSignPolicy == PlusSignPolicy.REQUIRE) {
                resultBuilder.append("\\+");
            } else if (boundPolicy == BoundPolicy.DIGIT_ONLY) {
                String basePart = "(?<!\\d)";
                resultBuilder.append(allowPlus ? String.format("\\+?%s", basePart) : basePart);
            } else if (boundPolicy == BoundPolicy.DIGIT_SIGN) {
                String basePart = "(?<![0-9\\+\\-])";
                resultBuilder.append(allowPlus ? String.format("(?:\\+|%s)", basePart) : basePart); 
            } else if (boundPolicy == BoundPolicy.DIGIT_SIGN_NOFRACTION) {
                String basePart = "(?<!\\d\\.?|[\\+\\-])";
                resultBuilder.append(allowPlus ? String.format("(?:\\+|%s)", basePart) : basePart); 
            } else if (plusSignPolicy == PlusSignPolicy.ALLOW) {
                resultBuilder.append("\\+?");
            }

            if (allowLeadingZeros) {
                resultBuilder.append("0*");
            }
            
            BigInteger subFrom = hasNegative ? BigInteger.ZERO : from;
            
            resultBuilder.append(generator.generate(subFrom, to));
        }
        
        if (hasNegative && hasNonNegative) {
            resultBuilder.append(')');
        }
        
        if (boundPolicy == BoundPolicy.DIGIT_SIGN_NOFRACTION) {
            resultBuilder.append("(?!\\.?\\d)");
        } else if (boundPolicy != BoundPolicy.NONE) {
            resultBuilder.append("(?!\\d)");
        }
        
        return resultBuilder.toString();
    }
    

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
        
        
        public TerminalBuilder allowPlusSign() {
            return plusSignPolicy(PlusSignPolicy.ALLOW);
        }

        public TerminalBuilder requirePlusSign() {
            return plusSignPolicy(PlusSignPolicy.REQUIRE);
        }

        public TerminalBuilder denyPlusSign() {
            return plusSignPolicy(PlusSignPolicy.DENY);
        }

        public TerminalBuilder plusSignPolicy(PlusSignPolicy plusSignPolicy) {
            IntBetweenBuilder.this.plusSignPolicy = plusSignPolicy;
            return this;
        }

        public TerminalBuilder allowNegativeZero() {
            return allowNegativeZero(true);
        }
        
        public TerminalBuilder allowNegativeZero(boolean allowNegativeZero) {
            IntBetweenBuilder.this.allowNegativeZero = allowNegativeZero;
            return this;
        }

        public TerminalBuilder allowLeadingZeros() {
            return allowLeadingZeros(true);
        }
        
        public TerminalBuilder allowLeadingZeros(boolean allowLeadingZeros) {
            IntBetweenBuilder.this.allowLeadingZeros = allowLeadingZeros;
            return this;
        }

        public TerminalBuilder boundPolicy(BoundPolicy boundPolicy) {
            IntBetweenBuilder.this.boundPolicy = boundPolicy;
            return this;
        }
        

        public Fragment build() {
            BigInteger from = lowInclusive ? low : low.add(BigInteger.ONE);
            BigInteger to = highInclusive ? high : high.subtract(BigInteger.ONE);
            
            if (from.compareTo(to) > 0) {
                throw new IllegalArgumentException(String.format("Invalid range %s(%b)..%s(%b)",
                        low, lowInclusive, high, highInclusive));
            }
            
            return new LazyFragment(() -> IntBetweenBuilder.this.generate(from, to));
        }
        
    }

}
