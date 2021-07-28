package hu.webarticum.regexbee.template;

import hu.webarticum.regexbee.BeeFragment;

public class BeeTemplateParameter implements BeeFragment {
    
    private static final String PREFIX = "((??[";
    
    private static final String POSTFIX = "]??))";
    
    private static final String DEFAULT_NAME = "p";
    

    private final String placeholder;
    

    public BeeTemplateParameter() {
        this(DEFAULT_NAME);
    }
    
    public BeeTemplateParameter(String name) {
        if (!BeeTemplate.isValidParameterName(name)) {
            throw new IllegalArgumentException(String.format("Illegal parameter name: '%s'", name));
        }
        
        this.placeholder = PREFIX + name + POSTFIX;
    }
    
    
    @Override
    public String get() {
        return placeholder;
    }
    
}
