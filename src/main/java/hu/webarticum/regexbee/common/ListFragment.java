package hu.webarticum.regexbee.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import hu.webarticum.regexbee.Fragment;
import hu.webarticum.regexbee.util.PatternUtil;

public class ListFragment extends AbstractGeneratingFragment {
    
    private final List<Fragment> fragments;
    

    public ListFragment(Fragment... fragments) {
        this(Arrays.asList(fragments));
    }

    public ListFragment(Collection<Fragment> fragments) {
        this.fragments = new ArrayList<>(fragments);
    }

    private ListFragment(int size) {
        this.fragments = new ArrayList<>(size);
    }
    
    
    public List<Fragment> fragments() {
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
        
        StringBuilder resultBuilder = new StringBuilder("(?:");
        for (Fragment fragment : fragments) {
            String subPattern = fragment.get();
            if (PatternUtil.isSafe(subPattern)) {
                resultBuilder.append(subPattern);
            } else {
                resultBuilder.append(PatternUtil.wrap(subPattern));
            }
        }
        resultBuilder.append(")");
        
        return resultBuilder.toString();
    }
    
    @Override
    public Fragment then(Fragment nextFragment) {
        ListFragment result = new ListFragment(fragments.size() + 1);
        result.fragments.addAll(this.fragments);
        result.fragments.add(nextFragment);
        return result;
    }
    
}
