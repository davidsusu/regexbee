package hu.webarticum.regexbee.common;

import hu.webarticum.regexbee.BeeFragment;

public class AtomicGroupFragment extends AbstractGeneratingFragment {

    private final BeeFragment baseFragment;
    

    public AtomicGroupFragment(BeeFragment baseFragment) {
        this.baseFragment = baseFragment;
    }
    
    
    @Override
    protected String generate() {
        StringBuilder resultBuilder = new StringBuilder("(?>");
        resultBuilder.append(baseFragment.get());
        resultBuilder.append(')');
        return resultBuilder.toString();
    }

}
