# :honeybee: RegexBee

Honeycomb cells for building regular expressions in a fluent way.
Even complex ones.

Issues, recommendations and pull request are welcome.

Main features:

- fluent API
- immutable
- concise, declarative, readable
- limitlessly composable
- has built-in basic fragments and compositions
- character class nesting
- integer range match
- support for different greediness types
- support for named groups and references
- support for lookaround, modifiers and other special groups
- flexible templating
- and many more...

## Examples

So, you can build complex patterns in a fluent, declarative way:

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
```

`BeeFragment`s can be converted to `java.util.regex.Pattern` with calling `.toPattern()`:

```java
Pattern myPattern = myFragment.toPattern();
Matcher myMatcher = myPattern.matcher(someString);

if (myMatcher.matches()) {
    String nameX = myMatcher.group("nameX");
    System.out.println(String.format("We have a nice day, %s!", nameX));
}
```

Let's see an other example with a simple log processor:

```java
private static final Pattern LOG_ENTRY_PATTERN = Bee
        .then(Bee.BEGIN)
        .then(Bee.TIMESTAMP.as(TIMESTAMP_NAME))
        .then(Bee.WHITESPACE.more())
        .then(Bee.oneFixedOf("INFO", "WARN", "ERROR").as(SEVERITY_NAME))
        .then(Bee.WHITESPACE.more())
        .then(Bee.ANYTHING.as(MESSAGE_NAME))
        .then(Bee.END)
        .toPattern();
```

Then, our pattern can be used like this:

```java
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

## Templating

You can insert placeholders at any place of a structure with
`Bee.placeholder()` or `Bee.placeholder("someParam")`,
then you can construct a template with `.toTemplate()`.
Parameters can be substituted by calling `.substitute(...)`.

You can create a single-parameter template with the following method:

```java
BeeTemplate template = Bee
        .then(Bee.fixed("((("))
        .then(Bee.placeholder())
        .then(Bee.fixed(")))"))
        .toTemplate();
```

Then, you can create any number of substituted fragments:

```java
BeeFragment substitutedWithWord = template.substitute(Bee.WORD);
BeeFragment substitutedWithNumber = template.substitute(Bee.UNSIGNED_INT);
```

Alternatively, explicitly named placeholders can be used.
Multiple placeholders with the same name are also supported.
For example:

```java
BeeTemplate template = Bee
        .then(Bee.placeholder("p1"))
        .then(Bee.SPACE)
        .then(Bee.placeholder("p2"))
        .then(Bee.SPACE)
        .then(Bee.placeholder("p1"))
        .then(Bee.SPACE)
        .then(Bee.placeholder("p1")
                .or(Bee.placeholder("p2")))
        .then(Bee.SPACE)
        .then(Bee.placeholder("p3"))
        .toTemplate();
```

In this example `'p1'` and `'p2'` are used multiple times.

You can use a `java.util.Map` for substituting named parameters:

```java
Map<String, BeeFragment> parameters = new HashMap<String, BeeFragment>();
parameters.put("p1", Bee.fixed("*").more());
parameters.put("p2", Bee.UNSIGNED_INT);
parameters.put("p3", Bee.fixed("%").more());
BeeFragment substitutedFragment1 = template.substitute(parameters);
```

Of course, with Java 9+ you can use `Map.of` to make this a little bit cleaner:

```java
BeeFragment substitutedFragment1 = template.substitute(Map.of(
        "p1", Bee.fixed("*").more(),
        "p2", Bee.UNSIGNED_INT,
        "p3", Bee.fixed("%").more()));
```

You can use placeholders just as any fragment.
For example they accept quantifiers too:

```java
BeeTemplate somethingMoreTemplate = Bee.placeholder().more().toTemplate();
```

For more examples
[see the examples package](https://github.com/davidsusu/regexbee/tree/master/src/examples/java/hu/webarticum/regexbee/examples)
or the tests.

## Custom fragments

To create a custom fragment, just implement the `BeeFragment` interface:

```java
public class SeparatedByCommaFragment implements BeeFragment {
    
    private final List<BeeFragment> fragments;
    
    
    public SeparatedByCommaFragment(BeeFragment... fragments) {
        this(Arrays.asList(fragments));
    }
    
    public SeparatedByCommaFragment(Collection<BeeFragment> fragments) {
        this.fragments = new ArrayList<>(fragments);
    }
    
    
    @Override
    public String get() {
        return fragments.stream().map(BeeFragment::get).collect(Collectors.joining(","));
    }
    
}
```

Now `SeparatedByCommaFragment` can be used as any other fragments:

```java
BeeFragment fragment = Bee
        .then(Bee.BEGIN)
        .then(new SeparatedByCommaFragment(
                Bee.UNSIGNED_INT.as("number"),
                Bee.ASCII_WORD.as("word"),
                Bee.oneFixedOf("lorem", "ipsum").as("keyword")))
        .then(Bee.END);
// ...
```
