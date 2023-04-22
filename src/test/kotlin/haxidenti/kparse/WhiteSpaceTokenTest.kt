package haxidenti.kparse

import org.junit.jupiter.api.Test

class WhiteSpaceTokenTest {
    @Test
    fun testTabulation() {
        (Parsers.whiteSpace(FileInfo("src", 1), "    ") as WhiteSpaceToken).tabulation eq 4
        (Parsers.whiteSpace(FileInfo("src", 1), "  ") as WhiteSpaceToken).tabulation eq 2
        (Parsers.whiteSpace(FileInfo("src", 1), "    \n") as WhiteSpaceToken).tabulation eq 0
        (Parsers.whiteSpace(FileInfo("src", 1), "    \n  ") as WhiteSpaceToken).tabulation eq 2
        (Parsers.whiteSpace(FileInfo("src", 1), "    \n    ") as WhiteSpaceToken).tabulation eq 4
    }
}