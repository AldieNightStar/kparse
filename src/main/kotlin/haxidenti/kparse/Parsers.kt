package haxidenti.kparse

private val NUMBER_FLOAT = "-?\\d+\\.\\d+".toRegex()
private val NUMBER_INT = "-?\\d+".toRegex()
private val WORD = "[a-zA-Z_\$]+[a-zA-Z0-9_\$]?".toRegex()
private val SYMBOLS = "[!@#\$%^&*()_=\\-+\\/\\\\.,;'|~:]+".toRegex()
private val BRACKETS = "\\(\\)\\[]<>\\{}".toRegex()

object Parsers {
    fun regex(regex: Regex, map: TokenMapper<RegexToken>): ParserFunc {
        val func: ParserFunc = { info: FileInfo, src: CharSequence ->
            regex.matchAt(src, 0)?.let {
                RegexToken(info, it.value)
            }
        }
        return func.useMapper(map)
    }

    fun string(quotes: String = "\"'`"): ParserFunc = { info, src ->
        if (src[0] in quotes) {
            val quote = src[0]
            val sb = StringBuilder()
            var count = 1
            var escaped = false
            for (c in src.subSequence(1, src.length)) {
                if (c == '\n') {
                    throw ParserException(info, "String can't have \\n symbols")
                }
                if (escaped) {
                    sb.append(c.escapeValue)
                    escaped = false
                    count++
                    continue
                }
                if (c == '\\') {
                    escaped = true
                    count++
                    continue
                }
                if (c == quote) {
                    count++
                    break
                }
                sb.append(c)
                count++
            }
            StringToken(info, sb.toString(), count)
        } else {
            null
        }
    }

    val numberParser: ParserFunc = { info, src ->
        val floatNum = NUMBER_FLOAT.matchAt(src, 0)
        if (floatNum != null) {
            NumberToken(info, floatNum.value)
        } else {
            NUMBER_INT.matchAt(src, 0)?.let { NumberToken(info, it.value) }
        }
    }

    val wordParser = regex(WORD) { WordToken(it.info, it.value) }

    fun commentUntilNextLineParser(commentPrefix: String = "#"): ParserFunc = { info, src ->
        if (src.startsWith(commentPrefix)) {
            val index = src.indexOf('\n')
            if (index < 0) {
                CommentToken(info, src.subSequence(1, src.length), 0)
            } else {
                CommentToken(info, src.subSequence(1, index), 0)
            }
        } else {
            null
        }
    }

    val whiteSpace = regex("\\s+".toRegex()) { WhiteSpaceToken(it.info, it.value) }

    val bracket = regex(BRACKETS) { BracketToken(it.info, it.value) }

    val operator = regex(SYMBOLS) { OperatorToken(it.info, it.value) }
}

private val Char.escapeValue
    get() = when (this) {
        't' -> '\t'
        'n' -> '\n'
        'r' -> '\r'
        '0' -> 0x00
        else -> this
    }

private fun <T : Token> ParserFunc.useMapper(mapper: TokenMapper<T>): ParserFunc =
    { info, src -> this(info, src)?.let { mapper(it as T) } }