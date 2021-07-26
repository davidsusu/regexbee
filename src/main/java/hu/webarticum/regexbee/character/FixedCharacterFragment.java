package hu.webarticum.regexbee.character;

import hu.webarticum.regexbee.util.PatternUtil;

public class FixedCharacterFragment implements CharacterFragment {

    private final String literal;
    
    
    public FixedCharacterFragment(char c) {
        this.literal = PatternUtil.isSpecialCharacter(c) ? "\\" + c : String.valueOf(c);
    }
    

    @Override
    public String get() {
        return literal;
    }
    
}
