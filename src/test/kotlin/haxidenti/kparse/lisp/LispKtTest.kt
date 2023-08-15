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
            tokens.size eq 3
            (tokens[0] as WordToken).value eq "hi"
            (tokens[1] as StringToken).value eq "jack"
            (tokens[2] as LispCommandToken).tokens.let {tokens ->
                tokens.size eq 3
                (tokens[0] as WordToken).value eq "add"
                (tokens[1] as NumberToken).value eq "1"
                (tokens[2] as NumberToken).value eq "2"
            }
        }
    }

    @Test
    fun testLispWhenFuncsAreSym() {
        val src = "(< 2 3)"
        val toks = parseLisp("File", src)

        toks.size eq 1
        (toks[0] as LispCommandToken).tokens.let { tokens ->
            tokens.size eq 3
            (tokens[0] as BracketToken).value eq "<"
            (tokens[1] as NumberToken).value eq "2"
            (tokens[2] as NumberToken).value eq "3"
        }
    }
}