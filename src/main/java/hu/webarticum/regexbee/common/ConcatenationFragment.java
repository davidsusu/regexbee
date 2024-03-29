package hu.webarticum.regexbee.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import hu.webarticum.regexbee.BeeFragment;
import hu.webarticum.regexbee.util.PatternUtil;

public class ConcatenationFragment extends AbstractGeneratingFragment {
    
    private final List<BeeFragment> fragments;
    

    public ConcatenationFragment(BeeFragment... fragments) {
        this(Arrays.asList(fragments));
    }

    public ConcatenationFragment(Collection<BeeFragment> fragments) {
        this.fragments = new ArrayList<>(fragments);
    }

    private ConcatenationFragment(int size) {
        this.fragments = new ArrayList<>(size);
    }
    
    
    public List<BeeFragment> fragments() {
        return new ArrayList<>();
    }
    
    @Override
    protected String generate() {
        if (fragments.isEmpty()) {
            return "";
        }
        
        if (fragments.size() == 1) {
            return fragments.get(0).get();
        }
        
        StringBuilder resultBuilder = new StringBuilder();
        for (BeeFragment fragment : fragments) {
            String subPattern = fragment.get();
            if (PatternUtil.isSafePattern(subPattern)) {
                resultBuilder.append(subPattern);
            } else {
                resultBuilder.append(PatternUtil.wrapPattern(subPattern));
            }
        }
        
        return resultBuilder.toString();
    }
    
    @Override
    public BeeFragment then(BeeFragment nextFragment) {
        ConcatenationFragment result = new ConcatenationFragment(fragments.size() + 1);
        result.fragments.addAll(this.fragments);
        result.fragments.add(nextFragment);
        return result;
    }
    
}
