package com.github.crispyteam.parsing

enum class TokenType {
    OPEN_PAREN,
    CLOSE_PAREN,

    DEC_NUMBER,

    IDENTIFIER,

    PLUS,
    MINUS,
    STAR,
    SLASH,
    EQUALS,

    ERROR,
    EOF,
}

/**
 * A lexical Token is a string with an assigned meaning.
 */
data class Token(val type: TokenType, val lexeme: String)
