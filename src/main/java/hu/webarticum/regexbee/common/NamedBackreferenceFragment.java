package hu.webarticum.regexbee.common;

import hu.webarticum.regexbee.BeeFragment;
import hu.webarticum.regexbee.util.PatternUtil;

public class NamedBackreferenceFragment implements BeeFragment {
    
    private final String groupName;
    

    public NamedBackreferenceFragment(String groupName) {
        this.groupName = PatternUtil.requireValidGroupName(groupName);
    }
    
    
    @Override
    public String get() {
        return "\\k<" + groupName + ">";
    }
    
}
