package haxidenti.kparse

class FileInfo(val filename: String, val line: Int)

typealias ParserFunc = (info: FileInfo, src: CharSequence) -> Token?

class ParserBuilder(private val fileName: String) {

    private var source: String = ""
    private val parsers = mutableListOf<ParserFunc>()

    fun source(src: String): ParserBuilder {
        this.source = src
        return this
    }

    fun parser(parser: ParserFunc) {
        parsers.add(parser)
    }

    fun parsers(parsers: Collection<ParserFunc>) {
        parsers.forEach { this.parsers.add(it) }
    }

    val parser: Parser get() = Parser(fileName, source, parsers)
}

class Parser(
    private val fileName: String,
    private val source: String,
    private val rootParsers: List<ParserFunc>
) {
    private var pos = 0
    private var line = 1
    private val info get() = FileInfo(fileName, line)

    companion object {
        @JvmStatic
        fun of(fileName: String): ParserBuilder {
            return ParserBuilder(fileName)
        }
    }

    fun parse() = sequence {
        while (pos < source.length) {
            val slice = source.substring(pos, source.length)
            val token = parseOne(slice)
            yield(token)
            pos += token.symbols
            line += token.nextLineSymbols
        }
    }

    private fun parseOne(src: String) = rootParsers
        .firstNotNullOfOrNull { it(info, src) } ?: throw ParserException(info, "Unknown token")
}