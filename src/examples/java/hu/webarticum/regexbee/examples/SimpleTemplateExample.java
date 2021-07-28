package hu.webarticum.regexbee.examples;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.webarticum.regexbee.Bee;
import hu.webarticum.regexbee.BeeFragment;
import hu.webarticum.regexbee.template.BeeTemplate;

public class SimpleTemplateExample {
    
    private static final BeeTemplate THREE_PARS_TEMPLATE = Bee
            .then(Bee.fixed("((("))
            .then(Bee.placeholder()) // any subpattern can be substituted here
            .then(Bee.fixed(")))"))
            .toTemplate();

    private static final String SUBJECT =
            "lorem (((ipsum))) () (((3))) (((L))) M ((())) dolor (((XL))) " +
            "(((99))) ((())) sit (((7))) ((())) (((XS))) amet (((kkk))) ()";

    
    public static void main(String[] args) {
        System.out.println("Subject:");
        System.out.println(SUBJECT);
        System.out.println();
        
        // first, substitute the placeholder with a number
        runWith("Substitute with number...", THREE_PARS_TEMPLATE.substitute(Bee.UNSIGNED_INT));

        // then, substitute with some dress size
        runWith(
                "Substitute with dress size...",
                THREE_PARS_TEMPLATE.substitute(
                        Bee.fixed("XXS")
                            .or(Bee.fixed("XS"))
                            .or(Bee.fixed("S"))
                            .or(Bee.fixed("M"))
                            .or(Bee.fixed("L"))
                            .or(Bee.fixed("XL"))
                            .or(Bee.fixed("XXL"))
                        .caseInsensitive()));
        
        // finally, substitute with nothing
        runWith("Substitute with nothing...",THREE_PARS_TEMPLATE.substitute(Bee.NOTHING));

    }
    
    private static void runWith(String title, BeeFragment fragment) {
        System.out.println(title);
        Pattern pattern = fragment.toPattern();
        Matcher matcher = pattern.matcher(SUBJECT);
        while (matcher.find()) {
            System.out.println(String.format(
                    "  Match found at %d: '%s'",
                    matcher.start(), matcher.group()));
        }
        System.out.println();
    }
    
}
