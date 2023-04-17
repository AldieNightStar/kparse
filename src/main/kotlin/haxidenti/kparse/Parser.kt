package haxidenti.kparse

class FileInfo(val filename: String, val line: Int)

open class Token(val info: FileInfo, val symbols: Int, val nextLineSymbols: Int)

typealias ParserFunc = (info: FileInfo, src: CharSequence) -> Token?

class Parser(
    private val fileName: String,
    private val source: String,
    private val rootParsers: List<ParserFunc>
) {
    private var pos = 0
    private var line = 1
    private val info get() = FileInfo(fileName, line)

    fun parse(src: CharSequence) = sequence {
        while (pos < source.length) {
            yield(
                parseOne(src.substring(pos, src.length))
            )
        }
    }

    private fun parseOne(src: String) = rootParsers
        .firstNotNullOfOrNull { it(info, src) } ?: throw ParserException(info, "Unknown token")
}