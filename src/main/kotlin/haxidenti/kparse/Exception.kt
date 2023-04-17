package haxidenti.kparse

class ParserException(val info: FileInfo, val reason: String) :
    RuntimeException("Error during parsing \"${info.filename}\" on line ${info.line}: $reason")