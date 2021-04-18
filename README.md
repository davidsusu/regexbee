# RegexBee

Honeycomb cells for building regular expressions in a fluent way.
Even complex ones.

Simple example:

```java
Fragment myFragment = Fragments.BEGIN
        .then(Fragments.word())
        .then(Fragments.SPACE.any())
        .then(Fragments.intBetween(-4, 1359)
                .or(Fragments.fixed("-"))
        .then(Fragments.fixed("??)fixed?text. ").optional());
        .then(Fragments.word().optional(QuantifierType.POSSESSIVE))
        .then(Fragments.END);
Pattern myPattern = myFragment.toPattern();

if (myPattern.matcher(someString).matches()) {
    System.out.println("I have a nice day.");
}
```