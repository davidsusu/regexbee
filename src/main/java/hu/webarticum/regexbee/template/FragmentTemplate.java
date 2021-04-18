package hu.webarticum.regexbee.template;

import hu.webarticum.regexbee.Fragment;

// TODO: multiple parameters?
public interface FragmentTemplate {
    
    public Fragment substitute(Fragment parameter);
    
}
