package haxidenti.kparse

open class Token(val info: FileInfo, val symbols: Int, val nextLineSymbols: Int)

typealias TokenMapper<T> = (T) -> Token

open class RegexToken(info: FileInfo, val value: String) :
    Token(info, value.length, value.count { it == '\n' })

class WordToken(info: FileInfo, value: String) : RegexToken(info, value)

class WhiteSpaceToken(info: FileInfo, value: CharSequence) : Token(info, value.length, value.count { it == '\n' })

class StringToken(info: FileInfo, val value: String, symbols: Int) :
    Token(info, symbols, 0)

class NumberToken(info: FileInfo, val value: String) : Token(info, value.length, 0) {
    val isFloating get() = value.contains(".")
    val number get() = value.toDouble()
}

class CommentToken(info: FileInfo, val value: CharSequence, nextLines: Int) : Token(info, value.length, nextLines)

class BracketToken(info: FileInfo, val value: String) : Token(info, 1, 0)

class OperatorToken(info: FileInfo, val value: String) : Token(info, value.length, 0)