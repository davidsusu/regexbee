# RegexBee

Honeycomb cells for building regular expressions in a fluent way.
Even complex ones.

Alert: currently this library is in an experimental state,
and the api can heavily change between commits.
Do not use it in production.

Issues, recommendations and pull request are welcome.

Regex `Fragment`s are immutable and lazily computed and cached.
You can build complex patterns in a fluent (quasi-declarative) way.

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