package hu.webarticum.regexbee;

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