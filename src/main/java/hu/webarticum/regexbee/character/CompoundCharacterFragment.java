package hu.webarticum.regexbee.character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import hu.webarticum.regexbee.common.AbstractGeneratingFragment;

public class CompoundCharacterFragment extends AbstractGeneratingFragment implements CharacterFragment {
    
    private final boolean positive;

    private final List<CharacterFragment> fragments;
    
    
    public CompoundCharacterFragment(CharacterFragment... fragments) {
        this(true, fragments);
    }

    public CompoundCharacterFragment(boolean positive, CharacterFragment... fragments) {
        this(positive, Arrays.asList(fragments));
    }

    public CompoundCharacterFragment(Collection<CharacterFragment> fragments) {
        this(true, fragments);
    }

    public CompoundCharacterFragment(boolean positive, Collection<CharacterFragment> fragments) {
        this.positive = positive;
        this.fragments = new ArrayList<>(fragments);
    }


    @Override
    public CharacterFragment or(CharacterFragment fragment) {
        return new CompoundCharacterFragment(extend(fragments, fragment));
    }
    
    private static List<CharacterFragment> extend(List<CharacterFragment> fragments, CharacterFragment next) {
        List<CharacterFragment> result = new ArrayList<>(fragments.size() + 1);
        result.addAll(fragments);
        result.add(next);
        return result;
    }
    
    @Override
    protected String generate() {
        StringBuilder resultBuilder = new StringBuilder("[");
        if (!positive) {
            resultBuilder.append('^');
        }
        for (CharacterFragment fragment : fragments) {
            resultBuilder.append(fragment.get());
        }
        resultBuilder.append(']');
        return resultBuilder.toString();
    }
    
}
