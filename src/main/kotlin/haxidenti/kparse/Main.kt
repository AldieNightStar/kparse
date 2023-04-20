package haxidenti.kparse

fun main(args: Array<String>) {
    with(Parser.of("xxx.txt")) {
        source("this is me")
        parser(Parsers.wordParser)
    }
}