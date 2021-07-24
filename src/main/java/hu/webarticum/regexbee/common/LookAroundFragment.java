package hu.webarticum.regexbee.common;

import java.util.EnumMap;
import java.util.Map;

import hu.webarticum.regexbee.BeeFragment;

public class LookAroundFragment extends AbstractGeneratingFragment {
    
    public enum Type {
        AHEAD_POSITIVE,
        AHEAD_NEGATIVE,
        BEHIND_POSITIVE,
        BEHIND_NEGATIVE,
    }
    
    private static final Map<Type, String> PREFIX_MAP = new EnumMap<>(Type.class);
    static {
        PREFIX_MAP.put(Type.AHEAD_POSITIVE, "(?=");
        PREFIX_MAP.put(Type.AHEAD_NEGATIVE, "(?!");
        PREFIX_MAP.put(Type.BEHIND_POSITIVE, "(?<=");
        PREFIX_MAP.put(Type.BEHIND_NEGATIVE, "(?<!");
    }
    
    
    private final BeeFragment baseFragment;
    
    private final String prefix;
    

    public LookAroundFragment(BeeFragment baseFragment, Type type) {
        this.baseFragment = baseFragment;
        this.prefix = PREFIX_MAP.get(type);
    }
    
    
    @Override
    protected String generate() {
        StringBuilder resultBuilder = new StringBuilder(prefix);
        resultBuilder.append(baseFragment.get());
        resultBuilder.append(')');
        return resultBuilder.toString();
    }

}
