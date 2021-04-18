package hu.webarticum.regexbee.template;

import hu.webarticum.regexbee.BeeFragment;

// TODO: multiple parameters?
public interface FragmentTemplate {
    
    public BeeFragment substitute(BeeFragment parameter);
    
}
