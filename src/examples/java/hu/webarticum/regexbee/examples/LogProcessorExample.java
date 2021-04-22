package hu.webarticum.regexbee.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.webarticum.regexbee.Bee;

public class LogProcessorExample {

    private static final String TIMESTAMP_NAME = "timestamp";
    
    private static final String SEVERITY_NAME = "severity";
    
    private static final String MESSAGE_NAME = "message";
    
    private static final Pattern LOG_ENTRY_PATTERN = Bee
            .then(Bee.BEGIN)
            .then(Bee.TIMESTAMP.as(TIMESTAMP_NAME))
            .then(Bee.WHITESPACE.more())
            .then(Bee.oneFixedOf("INFO", "WARN", "ERROR").as(SEVERITY_NAME))
            .then(Bee.WHITESPACE.more())
            .then(Bee.ANYTHING.as(MESSAGE_NAME))
            .then(Bee.END)
            .toPattern();
    

    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = LogProcessorExample.class.getClassLoader();
        String resourceName = "hu/webarticum/regexbee/examples/sample.log";
        try (InputStream in = classLoader.getResourceAsStream(resourceName)) {
            process(new BufferedReader(new InputStreamReader(in)));
        }
    }
    
    private static void process(BufferedReader bufferedReader) throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            processLine(line);
        }
    }
    
    private static void processLine(String line) {
        System.out.println("--------------------");
        
        Matcher matcher = LOG_ENTRY_PATTERN.matcher(line);
        if (!matcher.matches()) {
            System.out.println(String.format("Unparseable line: %s", line));
            return;
        }

        System.out.println(String.format("timestamp: %s", matcher.group(TIMESTAMP_NAME)));
        System.out.println(String.format("severity: %s", matcher.group(SEVERITY_NAME)));
        System.out.println(String.format("message: %s", matcher.group(MESSAGE_NAME)));
    }
    
}
