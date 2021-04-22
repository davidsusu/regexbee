# RegexBee

Honeycomb cells for building regular expressions in a fluent way.
Even complex ones.

Alert: currently this library is in an experimental state,
anything can change between commits.

Issues, recommendations and pull request are welcome.

`BeeFragment`s are immutable, lazily computed and cached.
You can build complex patterns in a fluent (quasi-declarative) way.

Simple example:

```java
BeeFragment myFragment = Bee
        .then(Bee.BEGIN)
        .then(Bee.ASCII_WORD)
        .then(Bee.WHITESPACE.any())
        .then(Bee.ASCII_WORD.captureAs("nameX"))
        .then(Bee.intBetween(-4, 1359)
                .or(Bee.fixed("-").more(Greediness.POSSESSIVE)))
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