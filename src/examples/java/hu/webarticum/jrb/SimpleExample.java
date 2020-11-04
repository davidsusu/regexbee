package hu.webarticum.jrb;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleExample {

    public static void main(String[] args) {
        
        // XXX
        //System.out.println(Fragments.unsignedIntBetween(265, 32405, true).get());
        System.out.println(Fragments.unsignedIntBetween(2264, 2298, true).get());
        System.exit(0);
        
        
        Fragment fragment = Fragments.concat(
            Fragments.optional("g1", Fragments.fixed("xxx"), QuantifierType.POSSESSIVE),
            Fragments.alter(
                Fragments.simple("ggg"),
                Fragments.simple("hello")
            ),
            Fragments.optional(
                Fragments.concat(
                    Fragments.simple("."),
                    Fragments.WORD
                )
            )
        );
        
        System.out.println(fragment.get());
        System.out.println();

        String text = "xxxggghello-yyy";

        Pattern pattern = fragment.toPattern();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            System.out.print(spaces(matcher.start()));
            System.out.println(".");
            System.out.println(text);
            System.out.print(spaces(matcher.start()));
            System.out.println(matcher.group());
            System.out.println();
        }
    }
    
    private static String spaces(int count) {
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            resultBuilder.append(' ');
        }
        return resultBuilder.toString();
    }
    
}
