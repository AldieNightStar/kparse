package haxidenti.kparse

import org.junit.jupiter.api.Test

class ParserTest {

    @Test
    fun testParser() {
        val tokens = Parser(
            "source", "'alone'  in \n\t123()", listOf(
                Parsers.wordParser,
                Parsers.whiteSpace,
                Parsers.string(),
                Parsers.numberParser,
                Parsers.operator,
                Parsers.bracket
            )
        ).parseNoSkip().toList()

        (tokens[0] as StringToken).value eq "alone"
        (tokens[1] as WhiteSpaceToken).symbols eq 2
        (tokens[2] as WordToken).value eq "in"
        (tokens[3] as WhiteSpaceToken).symbols eq 3
        (tokens[4] as NumberToken).number eq 123.toDouble()
        (tokens[5] as BracketToken).apply { value eq "("; isOpen eq true }
        (tokens[6] as BracketToken).apply { value eq ")"; isOpen eq false }
    }

    @Test
    fun testNamingWithSymbolsInTheMiddle() {
        val src = "i32s 23"

        val toks = Parser.fullParser("Test").source(src).build().parse().toList()

        toks.size eq 2
        (toks[0] as WordToken).value eq "i32s"
        (toks[1] as NumberToken).value eq "23"
    }
}