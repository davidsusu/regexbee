# RegexBee

Honeycomb cells for building regular expressions in a fluent way.
Even complex ones.

Alert: currently this library is in an experimental state,
anything can change between commits.

Issues, recommendations and pull request are welcome.

`BeeFragment`s are immutable, lazily computed and cached.
You can build complex patterns in a fluent (quasi-declarative) way.

Example:

```java
BeeFragment myFragment = Bee
        .then(Bee.BEGIN)
        .then(Bee.ASCII_WORD)
        .then(Bee.WHITESPACE.any())
        .then(Bee.ASCII_WORD.as("nameX"))
        .then(Bee.intBetween(-4, 1359)
                .or(Bee.fixed("-").more(Greediness.POSSESSIVE)))
        .then(Bee.ref("nameX"))
        .then(Bee.fixed("??)fixed?text. ").optional())
        .then(Bee.ASCII_WORD.optional(Greediness.LAZY))
        .then(Bee.END);

Pattern myPattern = myFragment.toPattern();
Matcher myMatcher = myPattern.matcher(someString);

if (myMatcher.matches()) {
    String nameX = myMatcher.group("nameX");
    System.out.println(String.format("We have a nice day, %s!", nameX));
}
```

An other example with a simple log processor:

```java
// ...

private static final Pattern LOG_ENTRY_PATTERN = Bee
        .then(Bee.BEGIN)
        .then(Bee.TIMESTAMP.as(TIMESTAMP_NAME))
        .then(Bee.WHITESPACE.any())
        .then(Bee.oneFixedOf("INFO", "WARN", "ERROR").as(SEVERITY_NAME))
        .then(Bee.WHITESPACE.any())
        .then(Bee.ANYTHING.as(MESSAGE_NAME))
        .then(Bee.END)
        .toPattern();

// ...

private void processLine(String line) {
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
```

