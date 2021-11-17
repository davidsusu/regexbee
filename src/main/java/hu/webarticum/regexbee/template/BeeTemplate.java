package hu.webarticum.regexbee.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.webarticum.regexbee.Bee;
import hu.webarticum.regexbee.BeeFragment;

public class BeeTemplate {
    
    private static final String INDEX_GROUP_NAME = "index";
    
    private static final BeeFragment PARAMETER_NAME_FRAGMENT = Bee.ASCII_WORD;
    
    private static final Pattern PARAMETER_NAME_PATTERN = PARAMETER_NAME_FRAGMENT.toPattern();
    
    private static final Pattern PARAMETER_PATTERN = Bee
            .then(Bee.fixed("((??["))
            .then(PARAMETER_NAME_FRAGMENT.as(INDEX_GROUP_NAME))
            .then(Bee.fixed("]??))"))
            .toPattern();

    
    private final List<String> parts;

    private final List<String> parameterNames;
    

    public BeeTemplate(BeeFragment templateFragment) {
        ArrayList<String> collectingParts = new ArrayList<>();
        ArrayList<String> collectingParameterNames = new ArrayList<>();
        String templateText = templateFragment.get();
        Matcher matcher = PARAMETER_PATTERN.matcher(templateText);
        int lastEnd = 0;
        while (matcher.find()) {
            int start = matcher.start();
            collectingParts.add(templateText.substring(lastEnd, start));
            collectingParameterNames.add(matcher.group(INDEX_GROUP_NAME));
            lastEnd = matcher.end();
        }
        collectingParts.add(templateText.substring(lastEnd));
        collectingParts.trimToSize();
        this.parts = collectingParts;
        this.parameterNames = collectingParameterNames;
    }
    
    
    public static boolean isValidParameterName(String name) {
        return PARAMETER_NAME_PATTERN.matcher(name).matches();
    }
    
    
    public BeeFragment substitute(BeeFragment... parameters) {
        return substitute(Arrays.asList(parameters));
    }
    
    public BeeFragment substitute(List<? extends BeeFragment> parameters) {
        return new SubstitutedTemplateFragment(parts, new ArrayList<>(parameters));
    }

    public BeeFragment substitute(Map<String, ? extends BeeFragment> parameterMap) {
        int parameterCount = parameterNames.size();
        ArrayList<BeeFragment> parameters = new ArrayList<>(parameterCount);
        for (int i = 0; i < parameterCount; i++) {
            String parameterName = parameterNames.get(i);
            BeeFragment parameter = parameterMap.get(parameterName);
            if (parameter == null) {
                throw new IllegalArgumentException(String.format(
                        "No parameter given for name '%s'", parameterName));
            }
            parameters.add(parameter);
        }
        return new SubstitutedTemplateFragment(parts, parameters);
    }

    
    private static class SubstitutedTemplateFragment implements BeeFragment {
        
        private final List<String> parts;
        
        private final List<BeeFragment> parameters;
        
        
        private SubstitutedTemplateFragment(List<String> parts, List<BeeFragment> parameters) {
            int parameterCount = parts.size() - 1;
            if (parameters.size() != parameterCount) {
                throw new IllegalArgumentException(String.format(
                        "Expected parameter count: %d, but given: %d",
                        parameterCount, parameters.size()));
            }
            
            this.parts = parts;
            this.parameters = parameters;
        }
        
        
        @Override
        public String get() {
            StringBuilder resultBuilder = new StringBuilder(parts.get(0));
            int parameterCount = parameters.size();
            for (int i = 0; i < parameterCount; i++) {
                resultBuilder.append(parameters.get(i).get());
                resultBuilder.append(parts.get(i + 1));
            }
            return resultBuilder.toString();
        }
        
    }
    
}
