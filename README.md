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
* Custom parser should:
  * If token is really as matching then create it and pass symbols count you used to parse it
  * If token is NOT matching then return `null`
  * If token took `"abc"` string and value is `abc` then use `5` symbols instead of `3` anyway
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

# Lisp-like syntax parser

```kotlin
// Returns list of tokens
val tokens = parseLisp("(+ 10 20 30)")

// Then we can get each token
val token = tokens[0]

// Types of tokens:
// - LispCommandToken - command which contains another tokens inside
// - WordToken        - used for variable names or words without ""
// - WhiteSpaceToken  - any of " \t\r\n" characters
// - StringToken      - "This is a string token"
// - CommentToken     - Stands for comments etc
// - BracketToken     - Bracket token itself
// - OperatorToken    - Token with symbols like "<<" or "::" etc

// Check that token is of type Command: (command a b c etc)
if (token is LispCommandToken) {
  // Get size of tokens inside
  token.tokens.size

  // Take first token
  val subtoken = token.tokens[0]
  
  // API for simple token
  token.info.filename   // File name of that token
  token.info.line       // Line of that token
  token.symbols         // How much symbol it took during parsing
  token.nextLineSymbols // How much \n symbols it contains
  token.skipping        // Tells that this one is Not necessary

  // Check that this is word token
  if (subtoken is WordToken) {
      // Get it's string value
      val name = subtoken.value
  }
  
}
```