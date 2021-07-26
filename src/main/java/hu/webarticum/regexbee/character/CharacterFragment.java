package hu.webarticum.regexbee.character;

import hu.webarticum.regexbee.BeeFragment;

public interface CharacterFragment extends BeeFragment {

    public default CharacterFragment or(CharacterFragment fragment) {
        return new CompoundCharacterFragment(this, fragment);
    }

    public default CharacterFragment and(CharacterFragment fragment) {
        return new IntersectionCharacterFragment(this, fragment);
    }
    
}
