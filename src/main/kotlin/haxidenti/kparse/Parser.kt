package haxidenti.kparse

import java.lang.IllegalArgumentException

class FileInfo(val filename: String, val line: Int)

typealias ParserFunc = (info: FileInfo, src: CharSequence) -> Token?

class ParserBuilder(private val fileName: String) {

    private var source: CharSequence = ""
    private val parsers = mutableListOf<ParserFunc>()

    fun source(src: CharSequence): ParserBuilder {
        this.source = src
        return this
    }

    fun parser(parser: ParserFunc): ParserBuilder {
        parsers.add(parser)
        return this
    }

    fun parsers(parsers: Collection<ParserFunc>): ParserBuilder {
        parsers.forEach { this.parsers.add(it) }
        return this
    }

    fun build() = Parser(fileName, source, parsers.nonEmpty("Parsers"))

    private fun <T> List<T>.nonEmpty(name: String) =
        ifEmpty { throw IllegalArgumentException("$name should not be empty") }
}

class Parser(
    private val fileName: String,
    private val source: CharSequence,
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

        @JvmStatic
        fun fullParser(fileName: String) = ParserBuilder(fileName).parsers(Parsers.all)
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
        .firstNotNullOfOrNull { it(info, src) }
            ?: throw ParserException(info, "Unknown token: ${src.subSequence(0, 32.max(src.length))}")

    private fun Int.max(n: Int) = if (this <= n) this else n
}