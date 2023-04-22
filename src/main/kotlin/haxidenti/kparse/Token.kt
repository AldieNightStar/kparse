package haxidenti.kparse

open class Token(val info: FileInfo, val symbols: Int, val nextLineSymbols: Int, val skipping: Boolean)

typealias TokenMapper<T> = (T) -> Token

open class RegexToken(info: FileInfo, val value: String) :
    Token(info, value.length, value.count { it == '\n' }, false)

class WordToken(info: FileInfo, value: String) : RegexToken(info, value)

class WhiteSpaceToken(info: FileInfo, value: CharSequence) : Token(info, value.length, value.count { it == '\n' }, true)

class StringToken(info: FileInfo, val value: String, symbols: Int) :
    Token(info, symbols, 0, false)

class NumberToken(info: FileInfo, val value: String) : Token(info, value.length, 0, false) {
    val isFloating get() = value.contains(".")
    val number get() = value.toDouble()
    val isNegative get() = value.startsWith("-")
}

class CommentToken(info: FileInfo, val value: CharSequence, nextLines: Int) : Token(info, value.length, nextLines, true)

class BracketToken(info: FileInfo, val value: String) : Token(info, 1, 0, false) {
    val isOpen = isOpenBracket(value)
}

class OperatorToken(info: FileInfo, val value: String) : Token(info, value.length, 0, false)

private fun isOpenBracket(s: String) = s in listOf("(", "[", "{", "<")