package hu.webarticum.jrb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleExample {

    public static void main(String[] args) {
        Fragment fragment = Fragments.concat(
            Fragments.simple("g1", "xx"),
            Fragments.alter(
                Fragments.simple("alma"),
                Fragments.simple("korte")
            )
        );
        
        System.out.println(fragment.get());
        System.out.println();

        String text = "xxxalmakorteyyy";

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
