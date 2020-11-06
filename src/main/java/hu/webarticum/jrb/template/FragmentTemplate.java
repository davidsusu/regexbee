package hu.webarticum.jrb.template;

import hu.webarticum.jrb.core.Fragment;

// TODO: multiple parameters?
public interface FragmentTemplate {
    
    public Fragment substitute(Fragment parameter);
    
}
