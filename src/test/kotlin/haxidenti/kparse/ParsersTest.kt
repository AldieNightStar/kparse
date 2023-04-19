package haxidenti.kparse

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ParsersTest {

    val info: FileInfo = FileInfo("source", 1)


    @Test
    fun testParseNumbers() {
        (Parsers.numberParser(info, "123.44") as NumberToken).number eq 123.44
        (Parsers.numberParser(info, "321") as NumberToken).number eq 321.toDouble()
        (Parsers.numberParser(info, "-44") as NumberToken).number eq (-44).toDouble()
        (Parsers.numberParser(info, "-998.12") as NumberToken).number eq -998.12
        Parsers.numberParser(info, "a123") eq null
    }

    @Test
    fun testString() {
        (Parsers.string()(info, "'test'") as StringToken).value eq "test"
        (Parsers.string()(info, "'test\\''") as StringToken).value eq "test\'"
        (Parsers.string()(info, "'test\\\\\\''") as StringToken).value eq "test\\\'"
        (Parsers.string()(info, "'\\n123'") as StringToken).value eq "\n123"
        Parsers.string()(info, "x'1'") eq null
    }

    @Test
    fun testComments() {
        Parsers.commentUntilNextLineParser()(info, "123 # dsadsada") eq null
        (Parsers.commentUntilNextLineParser()(info, "# 123 123 123") as CommentToken).value eq " 123 123 123"
        (Parsers.commentUntilNextLineParser()(info, "# end\n123") as CommentToken).value eq " end"
        (Parsers.commentUntilNextLineParser()(info, "#\n") as CommentToken).value eq ""
    }
}