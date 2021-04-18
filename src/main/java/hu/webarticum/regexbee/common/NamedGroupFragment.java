package hu.webarticum.regexbee.common;

import hu.webarticum.regexbee.BeeFragment;
import hu.webarticum.regexbee.util.PatternUtil;

public class NamedGroupFragment extends AbstractGeneratingFragment {
    
    private final BeeFragment baseFragment;
    
    private final String groupName;
    

    public NamedGroupFragment(BeeFragment baseFragment, String groupName) {
        this.baseFragment = baseFragment;
        this.groupName = PatternUtil.requireValidGroupName(groupName);
    }
    
    
    @Override
    protected String generate() {
        StringBuilder resultBuilder = new StringBuilder("(?<");
        resultBuilder.append(groupName);
        resultBuilder.append('>');
        resultBuilder.append(baseFragment.get());
        resultBuilder.append(')');
        return resultBuilder.toString();
    }

}
