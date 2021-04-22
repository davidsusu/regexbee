package hu.webarticum.regexbee;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleExample {

    public static void main(String[] args) {
        BeeFragment fragment = Bee
                .then(Bee.fixed("xxx")
                        .optional(Greediness.POSSESSIVE)
                        .as("g1"))
                .then(Bee.simple("ggg")
                        .or(Bee.simple("hello")))
                .then(Bee.simple(".")
                        .then(Bee.ASCII_WORD)
                        .optional());
        
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
