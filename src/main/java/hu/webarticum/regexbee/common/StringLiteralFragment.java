package hu.webarticum.regexbee.common;

import java.util.regex.Pattern;

import hu.webarticum.regexbee.BeeFragment;
import hu.webarticum.regexbee.character.CharacterFragment;
import hu.webarticum.regexbee.util.PatternUtil;

public class StringLiteralFragment extends AbstractGeneratingFragment {

    private final String leftDelimiter;
    
    private final boolean leftDelimiterIncluded;
    
    private final String rightDelimiter;
    
    private final boolean rightDelimiterIncluded;
    
    private final boolean selfEscapingEnabled;
    
    private final boolean normalEscapingEnabled;
    
    private final char escaper;
    
    private final CharacterFragment escapableCharsFragment;
    
    private final BeeFragment embeddableFragment;
    

    public StringLiteralFragment(char delimiter) {
        this(delimiter, delimiter);
    }
    
    public StringLiteralFragment(char delimiter, char escaper) {
        this(builder()
                .withDelimiter(delimiter, true)
                .withSelfEscapingEnabled(delimiter == escaper)
                .withNormalEscapingEnabled(delimiter != escaper)
                .withEscaper(escaper));
    }

    private StringLiteralFragment(StringLiteralFragmentBuilder builder) {
        checkBuilder(builder);
        this.leftDelimiter = builder.leftDelimiter;
        this.leftDelimiterIncluded = builder.leftDelimiterIncluded;
        this.rightDelimiter = builder.rightDelimiter;
        this.rightDelimiterIncluded = builder.rightDelimiterIncluded;
        this.selfEscapingEnabled = builder.selfEscapingEnabled;
        this.normalEscapingEnabled = builder.normalEscapingEnabled;
        this.escaper = builder.escaper;
        this.escapableCharsFragment = builder.escapableCharsFragment;
        this.embeddableFragment = builder.embeddableFragment;
    }
    
    private static void checkBuilder(StringLiteralFragmentBuilder builder) {
        if (builder.leftDelimiter.isEmpty()) {
            throw new IllegalArgumentException("Left delimiter can not be empty");
        }
        if (builder.rightDelimiter.isEmpty()) {
            throw new IllegalArgumentException("Right delimiter can not be empty");
        }
        
        // TODO
        
    }
    
    public static StringLiteralFragmentBuilder builder() {
        return new StringLiteralFragmentBuilder();
    }
    

    @Override
    protected String generate() {
        StringBuilder resultBuilder = new StringBuilder();
        if (leftDelimiterIncluded) {
            resultBuilder.append(PatternUtil.fixedOf(leftDelimiter));
        }
        
        resultBuilder.append("(?:");
        if (embeddableFragment != null) {
            resultBuilder.append(embeddableFragment.get());
            resultBuilder.append('|');
        }
        resultBuilder.append(generateSafePart());
        if (normalEscapingEnabled) {
            resultBuilder.append('|');
            resultBuilder.append(PatternUtil.escapeCharacterIfNecessary(escaper));
            if (escapableCharsFragment != null) {
                resultBuilder.append(escapableCharsFragment.get());
            } else {
                resultBuilder.append('.');
            }
        }
        if (selfEscapingEnabled) {
            resultBuilder.append('|');
            resultBuilder.append(PatternUtil.fixedOf(rightDelimiter + rightDelimiter));
        }
        if (rightDelimiter.length() > 1) {
            resultBuilder.append('|');
            resultBuilder.append(PatternUtil.escapeCharacterIfNecessary(rightDelimiter.charAt(0)));
            resultBuilder.append("(?!");
            resultBuilder.append(Pattern.quote(rightDelimiter.substring(1)));
            resultBuilder.append(')');
        }
        resultBuilder.append(")*+");
        
        if (rightDelimiterIncluded) {
            resultBuilder.append(PatternUtil.fixedOf(rightDelimiter));
        }
        return resultBuilder.toString();
    }
    
    private String generateSafePart() {
        StringBuilder resultBuilder = new StringBuilder("[^");
        resultBuilder.append(PatternUtil.escapeCharacterIfNecessary(rightDelimiter.charAt(0)));
        if (normalEscapingEnabled) {
            resultBuilder.append(PatternUtil.escapeCharacterIfNecessary(escaper));
        }
        resultBuilder.append(']');
        return resultBuilder.toString();
    }
    
    
    public static class StringLiteralFragmentBuilder {
        
        private String leftDelimiter = "\"";
        
        private boolean leftDelimiterIncluded = true;
        
        private String rightDelimiter = "\"";
        
        private boolean rightDelimiterIncluded = true;
        
        private boolean selfEscapingEnabled = false;
        
        private boolean normalEscapingEnabled = true;
        
        private char escaper = '\\';
        
        private CharacterFragment escapableCharsFragment;
        
        private BeeFragment embeddableFragment = null;
        
        
        private StringLiteralFragmentBuilder() {
            // use builder()
        }
        
        
        // direct setters
        
        public StringLiteralFragmentBuilder withLeftDelimiter(String leftDelimiter) {
            this.leftDelimiter = leftDelimiter;
            return this;
        }

        public StringLiteralFragmentBuilder withLeftDelimitierIncluded(boolean leftDelimiterIncluded) {
            this.leftDelimiterIncluded = leftDelimiterIncluded;
            return this;
        }
        
        public StringLiteralFragmentBuilder withRightDelimiter(String rightDelimiter) {
            this.rightDelimiter = rightDelimiter;
            return this;
        }

        public StringLiteralFragmentBuilder withRightDelimitierIncluded(boolean rightDelimiterIncluded) {
            this.rightDelimiterIncluded = rightDelimiterIncluded;
            return this;
        }

        public StringLiteralFragmentBuilder withSelfEscapingEnabled(boolean selfEscapingEnabled) {
            this.selfEscapingEnabled = selfEscapingEnabled;
            return this;
        }

        public StringLiteralFragmentBuilder withNormalEscapingEnabled(boolean normalEscapingEnabled) {
            this.normalEscapingEnabled = normalEscapingEnabled;
            return this;
        }

        public StringLiteralFragmentBuilder withEscaper(char escaper) {
            this.escaper = escaper;
            return this;
        }

        public StringLiteralFragmentBuilder withEscapableCharsFragment(
                CharacterFragment escapableCharsFragment) {
            this.escapableCharsFragment = escapableCharsFragment;
            return this;
        }
        
        public StringLiteralFragmentBuilder withEmbeddableFragment(BeeFragment embeddableFragment) {
            this.embeddableFragment = embeddableFragment;
            return this;
        }
        
        
        // alternative setters

        public StringLiteralFragmentBuilder withLeftDelimiter(char leftDelimiter) {
            this.leftDelimiter = String.valueOf(leftDelimiter);
            return this;
        }

        public StringLiteralFragmentBuilder withLeftDelimitierIncluded() {
            this.leftDelimiterIncluded = true;
            return this;
        }

        public StringLiteralFragmentBuilder withLeftDelimitierExcluded() {
            this.leftDelimiterIncluded = false;
            return this;
        }

        public StringLiteralFragmentBuilder withRightDelimiter(char rightDelimiter) {
            this.rightDelimiter = String.valueOf(rightDelimiter);
            return this;
        }

        public StringLiteralFragmentBuilder withRightDelimitierIncluded() {
            this.rightDelimiterIncluded = true;
            return this;
        }

        public StringLiteralFragmentBuilder withRightDelimitierExcluded() {
            this.rightDelimiterIncluded = false;
            return this;
        }

        public StringLiteralFragmentBuilder withSelfEscapingEnabled() {
            this.selfEscapingEnabled = true;
            return this;
        }

        public StringLiteralFragmentBuilder withSelfEscapingDisabled() {
            this.selfEscapingEnabled = false;
            return this;
        }

        public StringLiteralFragmentBuilder withNormalEscapingEnabled() {
            this.normalEscapingEnabled = true;
            return this;
        }

        public StringLiteralFragmentBuilder withNormalEscapingDisabled() {
            this.normalEscapingEnabled = false;
            return this;
        }
        
        
        // combined setters

        public StringLiteralFragmentBuilder withDelimiter(char delimiter) {
            return withDelimiter(String.valueOf(delimiter));
        }

        public StringLiteralFragmentBuilder withDelimiter(String delimiter) {
            this.leftDelimiter = delimiter;
            this.rightDelimiter = delimiter;
            return this;
        }

        public StringLiteralFragmentBuilder withDelimiter(char delimiter, boolean delimiterIncluded) {
            return withDelimiter(String.valueOf(delimiter), delimiterIncluded);
        }

        public StringLiteralFragmentBuilder withDelimiter(String delimiter, boolean delimiterIncluded) {
            this.leftDelimiter = delimiter;
            this.leftDelimiterIncluded = delimiterIncluded;
            this.rightDelimiter = delimiter;
            this.rightDelimiterIncluded = delimiterIncluded;
            return this;
        }

        public StringLiteralFragmentBuilder withLeftDelimiter(
                char leftDelimiter, boolean leftDelimiterIncluded) {
            return withLeftDelimiter(String.valueOf(leftDelimiter), leftDelimiterIncluded);
        }
        
        public StringLiteralFragmentBuilder withLeftDelimiter(
                String leftDelimiter, boolean leftDelimiterIncluded) {
            this.leftDelimiter = leftDelimiter;
            this.leftDelimiterIncluded = leftDelimiterIncluded;
            return this;
        }

        public StringLiteralFragmentBuilder withRightDelimiter(
                char rightDelimiter, boolean rightDelimiterIncluded) {
            return withRightDelimiter(String.valueOf(rightDelimiter), rightDelimiterIncluded);
        }
        
        public StringLiteralFragmentBuilder withRightDelimiter(
                String rightDelimiter, boolean rightDelimiterIncluded) {
            this.rightDelimiter = rightDelimiter;
            this.rightDelimiterIncluded = rightDelimiterIncluded;
            return this;
        }

        public StringLiteralFragmentBuilder withEscaping(
                char escaper, boolean selfEscapingEnabled) {
            this.escaper = escaper;
            this.normalEscapingEnabled = true;
            this.selfEscapingEnabled = selfEscapingEnabled;
            return this;
        }
        
        public StringLiteralFragmentBuilder withoutAnyEscaping() {
            this.normalEscapingEnabled = false;
            this.selfEscapingEnabled = false;
            return this;
        }
        
        
        // build
        
        public StringLiteralFragment build() {
            return new StringLiteralFragment(this);
        }

    }
    
}
