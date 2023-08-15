package haxidenti.kparse.lisp

import haxidenti.kparse.*
import java.util.*

class LispCommandToken(info: FileInfo, val tokens: List<Token>) : Token(info, tokens.symbols, 0, false) {
    val head get() = if (tokens.isNotEmpty()) tokens.first() else null
    val tail get() = tokens.slice(1 until tokens.size)
    val commandName
        get(): String? {
            val tok = head ?: return null
            return when (tok) {
                is WordToken -> tok.value
                is OperatorToken -> tok.value
                is NumberToken -> tok.value
                else -> throw ParserException(info, "Can't get command name. Word/Operator/Number token is required")
            }
        }
}

fun parseLisp(fileName: String, src: CharSequence): List<Token> =
    parseLisp(Parser.fullParser(fileName).source(src).build().parse().toList())

fun parseLisp(tokens: List<Token>): List<Token> {
    val stack = Stack<MutableList<Token>>().apply { push(mutableListOf()) }
    tokens.forEach {
        if (it is BracketToken) {
            if (it.value == "(") {
                // Add new list to the stack
                stack.push(mutableListOf())
            } else if (it.value == ")") {
                val list = stack.popOrNull()
                    ?: throw ParserException(it.info, "Too many closing brackets")
                // Pop out list from stack and put to previous list (inside)
                stack.peek().add(LispCommandToken(it.info, list))
            } else {
                // Add non "(" and ")" symbols as operator token
                stack.peek().add(it)
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