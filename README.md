# K-Parse

## Sample Usage
```kotlin
// fileName - Name of the file
// src      - Source of that file
// parsers  - list of ParserFunc parsers. You can use from Parsers or create your own

// Will return all the non skipping tokens. To see with skipping=true - use 
val tokens = Parser(fileName, src, parsers).parse().toList()
```

## Use all the parsers defined before
* Use `fillParser` and do not add something else as it will use predefined parsers
```kotlin
// fileName - Name of the file
// src      - Source of that file

// Will return all the non skipping tokens. To see with skipping=true - use 
val tokens = Parser.fullParser(fileName).source(src).build().parse().toList()
```

## Add custom parsers
```kotlin
// You take info (line and file name) and text. So you need to return Token.
// Token should have some info and how much symbols it uses and \n symbols as well
val myParser: ParserFunc = { info, text -> MySuperToken(info, text, text.length, 0) }

// You can use predefined regex token
val myParser: ParserFunc = Parsers.regex("[abc]+".toRegex()) { MySuperToken(it.info, it.value) }
```

## Use that parsers in Parser
```kotlin
Parser.of(fileName).source(src)
    .parser(myParser) // Here we are adding our custom parser
    .build()
```