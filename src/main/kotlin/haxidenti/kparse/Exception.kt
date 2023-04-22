package haxidenti.kparse

class ParserException(val info: FileInfo, val reason: String) :
    RuntimeException("Error Parsing $info. $reason")