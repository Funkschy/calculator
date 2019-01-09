package com.github.crispyteam.parsing

import com.github.crispyteam.parsing.TokenType.*

/**
 * The Lexer class is responsible for splitting an input string into a sequence of tokens.
 */
class Lexer(private val source: String) : Iterator<Token> {
    // The position in the source string
    private var position = 0

    /**
     * Takes the current char from the source string and advances.
     * @return Either a null byte or the char at the current position.
     */
    private fun advance(): Char =
            if (atEnd()) {
                0.toChar()
            } else {
                source[position++]
            }

    /**
     * The current char in the source code.
     * @return Either a null byte or the char at the current position.
     */
    private fun current() = if (atEnd()) 0.toChar() else source[position]

    /**
     * Checks if the source code has been completely consumed.
     * @return true if at end, otherwise false
     */
    private fun atEnd() = position >= source.length

    /**
     * Advances while the given predicate evaluates to true.
     * @param predicate An anonymous function, which takes a Char and returns a Boolean.
     * @return The consumed String
     */
    private fun takeWhile(predicate: (Char) -> Boolean): String {
        val start = position

        while (!atEnd() && predicate(current())) {
            advance()
        }

        return source.substring(start, position)
    }

    /**
     * Advances and constructs a Token with the given values.
     * @param type the TokenType.
     * @param lexeme the String of the Token.
     */
    private fun advanceToken(type: TokenType, lexeme: String): Token {
        advance()
        return Token(type, lexeme)
    }

    private fun lexIdentifier() = Token(IDENTIFIER, takeWhile { c -> c.isLetterOrDigit() || c == '_' })

    /**
     * lexNumber scans a number with an optional decimal point
     * @return the scanned number Token
     */
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

    /**
     * scanToken skips whitespace and then determines the type of the current token based on the first character.
     * @return the complete scanned Token
     */
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
            else -> advanceToken(ERROR, "")
        }
    }
}
