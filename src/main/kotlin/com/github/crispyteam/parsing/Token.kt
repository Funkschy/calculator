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

    VAL,

    ERROR,
    EOF,
}

data class Token(val type: TokenType, val lexeme: String)
