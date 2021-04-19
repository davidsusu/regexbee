package hu.webarticum.regexbee;

public enum Greediness {
    
    GREEDY(""),
    
    LAZY("?"),
    
    POSSESSIVE("+"),
    
    ;
    
    
    private final String modifier;
    
    
    private Greediness(String modifier) {
        this.modifier = modifier;
    }
    
    
    public String modifier() {
        return modifier;
    }
    
}