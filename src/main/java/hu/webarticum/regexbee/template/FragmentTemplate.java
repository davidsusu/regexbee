package hu.webarticum.regexbee.template;

import hu.webarticum.regexbee.core.Fragment;

// TODO: multiple parameters?
public interface FragmentTemplate {
    
    public Fragment substitute(Fragment parameter);
    
}
