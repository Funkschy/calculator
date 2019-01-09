package com.github.crispyteam.parsing

import com.github.crispyteam.parsing.TokenType.*

class Lexer(private val source: String) : Iterator<Token> {
    private var position = 0

    private fun advance(): Char =
            if (atEnd()) {
                0.toChar()
            } else {
                source[position++]
            }

    private fun current() = if (atEnd()) 0.toChar() else source[position]

    private fun atEnd() = position >= source.length

    private fun takeWhile(predicate: (Char) -> Boolean): String {
        val start = position

        while (!atEnd() && predicate(current())) {
            advance()
        }

        return source.substring(start, position)
    }

    private fun advanceToken(type: TokenType, lexeme: String): Token {
        advance()
        return Token(type, lexeme)
    }

    private fun lexIdentifier() = Token(IDENTIFIER, takeWhile { c -> c.isLetterOrDigit() || c == '_' })

    private fun lexNumber(): Token {
        var lexeme = takeWhile(Char::isDigit)

        if (current() == '.') {
            advance()
            lexeme += "." + takeWhile(Char::isDigit)
        }

        return Token(DEC_NUMBER, lexeme)
    }

    override fun hasNext() = !atEnd()

    override fun next() = scanToken() ?: throw IllegalStateException("Trying to pull Token past EOF")

    private fun scanToken(): Token? {
        takeWhile(Char::isWhitespace)
        val current = current()

        return when {
            atEnd() -> null
            current.isLetter() -> lexIdentifier()
            current.isDigit() -> lexNumber()
            current == '+' -> advanceToken(PLUS, "+")
            current == '-' -> advanceToken(MINUS, "-")
            current == '*' -> advanceToken(STAR, "*")
            current == '/' -> advanceToken(SLASH, "/")
            current == '=' -> advanceToken(EQUALS, "=")
            current == '(' -> advanceToken(OPEN_PAREN, "(")
            current == ')' -> advanceToken(CLOSE_PAREN, ")")
            else -> Token(ERROR, "")
        }
    }
}
