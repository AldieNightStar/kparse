package haxidenti.kparse

import org.junit.jupiter.api.Test

class ParserTest {

    @Test
    fun testParser() {
        val tokens = Parser(
            "source", "'alone'  in \n\t123", listOf(
                Parsers.wordParser,
                Parsers.whiteSpace,
                Parsers.string(),
                Parsers.numberParser
            )
        ).parse().toList()

        (tokens[0] as StringToken).value eq "alone"
        (tokens[1] as WhiteSpaceToken).symbols eq 2
        (tokens[2] as WordToken).value eq "in"
        (tokens[3] as WhiteSpaceToken).symbols eq 3
        (tokens[4] as NumberToken).number eq 123.toDouble()
    }
}