package haxidenti.kparse

import org.junit.jupiter.api.Assertions

infix fun <T> T.eq(expected: T) {
    Assertions.assertEquals(expected, this)
}