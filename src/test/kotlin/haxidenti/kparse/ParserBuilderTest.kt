package haxidenti.kparse

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ParserBuilderTest {
    @Test
    fun allParsersTest() {
        val p = Parser.fullParser("abc").source("abc 123  \n 'xxx'").build()
        val tokens = p.parse().toList()

        tokens.size eq 5

        (tokens[0] as WordToken).value eq "abc"
        (tokens[1] as WhiteSpaceToken).symbols eq 1
        (tokens[2] as NumberToken).value eq "123"
        (tokens[3] as WhiteSpaceToken).let { it.symbols eq 4; it.nextLineSymbols eq 1 }
        (tokens[4] as StringToken).value eq "xxx"
    }

    private fun <T> T.eq(expected: T) = assertEquals(expected, this)
}