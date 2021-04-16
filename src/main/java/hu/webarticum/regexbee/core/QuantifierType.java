package hu.webarticum.regexbee.core;

public enum QuantifierType {
    
    GREEDY(""),
    
    RELUCTANT("?"),
    
    POSSESSIVE("+"),
    
    ;
    
    
    private final String modifier;
    
    
    private QuantifierType(String modifier) {
        this.modifier = modifier;
    }
    
    
    public String modifier() {
        return modifier;
    }
    
}