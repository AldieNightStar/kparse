package haxidenti.kparse.lisp

import haxidenti.kparse.*
import java.util.*
import kotlin.collections.Collection

class LispCommandToken(info: FileInfo, val tokens: List<Token>) : Token(info, tokens.symbols, 0)

fun parseLisp(fileName: String, src: CharSequence): List<Token> =
    parseLisp(Parser.fullParser(fileName).source(src).build().parse().toList())

fun parseLisp(tokens: List<Token>): List<Token> {
    val stack = Stack<MutableList<Token>>().apply { push(mutableListOf()) }
    tokens.forEach {
        if (it is BracketToken) {
            if (it.value == "(") {
                stack.push(mutableListOf())
            } else if (it.value == ")") {
                val list = stack.popOrNull()
                    ?: throw ParserException(it.info, "Too many closing brackets")
                stack.peek().add(LispCommandToken(it.info, list))
            }
        } else {
            stack.peek().add(it)
        }
    }
    if (stack.size > 1) {
        throw RuntimeException("Parser error. Did you forget to close some brackets?")
    } else if (stack.size < 1) {
        throw RuntimeException("Parser error. Looks like you have too much closing brackets")
    }
    return stack.peek()
}

private fun <T> Stack<T>.popOrNull() = if (isNotEmpty()) pop() else null

private val Collection<Token>.symbols get() = this.sumOf { it.symbols }