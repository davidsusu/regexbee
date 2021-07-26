package hu.webarticum.regexbee.character;

public enum PredefinedCharacterFragment implements CharacterFragment {

    ANY("."),
    ASCII_DIGIT("\\d"),
    NON_ASCII_DIGIT("\\D"),
    WHITESPACE("\\s"),
    NON_WHITESPACE("\\S"),
    ASCII_WORD_CHAR("\\w"),
    NON_ASCII_WORD_CHAR("\\W"),

    UNICODE_LETTER("\\p{L}"),
    UNICODE_DIGIT("\\p{N}"),
    
    /*
    
    TODO
    
    POSIX character classes (US-ASCII only)
    \p{Lower}   A lower-case alphabetic character: [a-z]
    \p{Upper}   An upper-case alphabetic character:[A-Z]
    \p{ASCII}   All ASCII:[\x00-\x7F]
    \p{Alpha}   An alphabetic character:[\p{Lower}\p{Upper}]
    \p{Digit}   A decimal digit: [0-9]
    \p{Alnum}   An alphanumeric character:[\p{Alpha}\p{Digit}]
    \p{Punct}   Punctuation: One of !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
    \p{Graph}   A visible character: [\p{Alnum}\p{Punct}]
    \p{Print}   A printable character: [\p{Graph}\x20]
    \p{Blank}   A space or a tab: [ \t]
    \p{Cntrl}   A control character: [\x00-\x1F\x7F]
    \p{XDigit}  A hexadecimal digit: [0-9a-fA-F]
    \p{Space}   A whitespace character: [ \t\n\x0B\f\r]
     
    java.lang.Character classes (simple java character type)
    \p{javaLowerCase}   Equivalent to java.lang.Character.isLowerCase()
    \p{javaUpperCase}   Equivalent to java.lang.Character.isUpperCase()
    \p{javaWhitespace}  Equivalent to java.lang.Character.isWhitespace()
    \p{javaMirrored}    Equivalent to java.lang.Character.isMirrored()
     
    Classes for Unicode scripts, blocks, categories and binary properties
    \p{IsLatin}     A Latin script character (script)
    \p{InGreek}     A character in the Greek block (block)
    \p{Lu}  An uppercase letter (category)
    \p{IsAlphabetic}    An alphabetic character (binary property)
    \p{Sc}  A currency symbol
    \P{InGreek}     Any character except one in the Greek block (negation)
    [\p{L}&&[^\p{Lu}]]      Any letter except an uppercase letter (subtraction)
    */
    
    ;
    
    
    private final String content;
    
    private PredefinedCharacterFragment(String content) {
        this.content = content;
    }
    
    public String get() {
        return content;
    }
    
}
