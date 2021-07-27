package hu.webarticum.regexbee.character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import hu.webarticum.regexbee.common.AbstractGeneratingFragment;

public class IntersectionCharacterFragment extends AbstractGeneratingFragment implements CharacterFragment {

    private final boolean positive;

    private final List<CharacterFragment> fragments;
    

    public IntersectionCharacterFragment(CharacterFragment... fragments) {
        this(true, fragments);
    }

    public IntersectionCharacterFragment(boolean positive, CharacterFragment... fragments) {
        this(positive, Arrays.asList(fragments));
    }

    public IntersectionCharacterFragment(Collection<CharacterFragment> fragments) {
        this(true, fragments);
    }

    public IntersectionCharacterFragment(boolean positive, Collection<CharacterFragment> fragments) {
        this.positive = positive;
        this.fragments = new ArrayList<>(fragments);
    }

    
    @Override
    public CharacterFragment and(CharacterFragment fragment) {
        return new IntersectionCharacterFragment(extend(fragments, fragment));
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
        boolean first = true;
        for (CharacterFragment fragment : fragments) {
            if (first) {
                first = false;
            } else {
                resultBuilder.append("&&");
            }
            resultBuilder.append(fragment.get());
        }
        resultBuilder.append(']');
        return resultBuilder.toString();
    }
    
}
