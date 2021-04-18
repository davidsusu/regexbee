package hu.webarticum.regexbee.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import hu.webarticum.regexbee.Fragment;
import hu.webarticum.regexbee.util.PatternUtil;

public class AlternationFragment extends AbstractGeneratingFragment {

    private final List<Fragment> fragments;
    

    public AlternationFragment(Fragment... fragments) {
        this(Arrays.asList(fragments));
    }

    public AlternationFragment(Collection<Fragment> fragments) {
        this.fragments = new ArrayList<>(fragments);
    }

    private AlternationFragment(int size) {
        this.fragments = new ArrayList<>(size);
    }
    
    
    @Override
    protected String generate() {
        if (fragments.isEmpty()) {
            return "";
        }
        
        if (fragments.size() == 1) {
            return fragments.get(0).get();
        }
        
        StringBuilder resultBuilder = new StringBuilder("(?:");
        boolean first = true;
        for (Fragment fragment : fragments) {
            if (first) {
                first = false;
            } else {
                resultBuilder.append('|');
            }
            String subPattern = fragment.get();
            if (PatternUtil.isSafe(subPattern)) {
                resultBuilder.append(subPattern);
            } else {
                resultBuilder.append(PatternUtil.wrap(subPattern));
            }
        }
        resultBuilder.append(')');
        
        return resultBuilder.toString();
    }

    
    @Override
    public Fragment or(Fragment nextFragment) {
        AlternationFragment result = new AlternationFragment(fragments.size() + 1);
        result.fragments.addAll(this.fragments);
        result.fragments.add(nextFragment);
        return result;
    }
    
}
