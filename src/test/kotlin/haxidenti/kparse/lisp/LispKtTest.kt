package haxidenti.kparse.lisp

import haxidenti.kparse.*
import org.junit.jupiter.api.Test

class LispKtTest {
    @Test
    fun testLisp() {
        val src = "(hi 'jack'  (add 1   2))"
        val toks = parseLisp("File", src)

        toks.size eq 1
        (toks[0] as LispCommandToken).tokens.let {tokens ->
            tokens.size eq 5
            (tokens[0] as WordToken).value eq "hi"
            (tokens[1] as WhiteSpaceToken).symbols eq 1
            (tokens[2] as StringToken).value eq "jack"
            (tokens[3] as WhiteSpaceToken).symbols eq 2
            (tokens[4] as LispCommandToken).tokens.let {tokens ->
                tokens.size eq 5
                (tokens[0] as WordToken).value eq "add"
                (tokens[1] as WhiteSpaceToken).symbols eq 1
                (tokens[2] as NumberToken).value eq "1"
                (tokens[3] as WhiteSpaceToken).symbols eq 3
                (tokens[4] as NumberToken).value eq "2"
            }
        }
    }
}