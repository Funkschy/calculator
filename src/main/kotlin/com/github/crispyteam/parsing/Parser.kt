package com.github.crispyteam.parsing

import com.github.crispyteam.parsing.Expr.*
import com.github.crispyteam.parsing.Expr.Number
import com.github.crispyteam.parsing.TokenType.*

class ParseError(msg: String) : RuntimeException(msg)

class Parser {
    private lateinit var tokens: List<Token>
    private var position = 0

    fun parse(tokens: Iterator<Token>) = parse(tokens.asSequence().toList())

    /**
     * Parses an expression from the token list.
     * @param tokens the list of tokens.
     * @return the first expression in the token list.
     */
    fun parse(tokens: List<Token>): Expr {
        // filter out errors and report them as a ParseError
        val errors = tokens.asSequence().filter { it.type == ERROR }.map { it.lexeme }.joinToString(", ")
        if (errors.isNotEmpty()) {
            throw ParseError("Lex Errors: $errors")
        }

        // initialise the attributes
        this.tokens = tokens
        this.position = 0

        return expression()
    }

    /**
     * Increments the position in the token list.
     */
    private fun advance() {
        position++
    }

    /**
     * Decrements the position in the token list.
     */
    private fun goBack() {
        position--
    }

    /**
     * Checks if there are no more tokens to scan.
     * @return true if at end (no more tokens).
     */
    private fun atEnd() = position >= tokens.size

    /**
     * The token at the current position.
     * @return the token.
     */
    private fun current() = tokens[position]

    /**
     * The last token, that was scanned.
     */
    private fun previous() = tokens[position - 1]

    /**
     * Checks if the current tokens type equals tokenType and if yes, advances the parser.
     * @param tokenType the expected type for the current token.
     * @return true, if the current token is of type tokenType.
     */
    private fun match(tokenType: TokenType): Boolean {
        if (!atEnd() && current().type == tokenType) {
            advance()
            return true
        }

        return false
    }

    /**
     * Like match, but throws an exception, if the result of match is false
     */
    private fun consume(tokenType: TokenType) {
        if (!match(tokenType)) throw ParseError("Expected '$tokenType', but got '${previous().type}'")
    }

    /**
     * Checks if any of the tokenTypes matches the current token.
     */
    private fun matchAny(vararg tokenType: TokenType) = tokenType.any { match(it) }

    /**
     * parse a single expression.
     */
    private fun expression(): Expr {
        if (!atEnd()) {
            return declaration()
        }

        throw ParseError("Unexpected end of file")
    }

    private fun declaration(): Expr {
        if (match(IDENTIFIER)) {
            val name = Identifier(previous().lexeme)
            if (match(EQUALS)) {
                return Declaration(name, expression())
            }

            goBack()
        }

        return term()
    }

    private fun term(): Expr {
        var left = factor()

        while (matchAny(PLUS, MINUS)) {
            val op = previous()
            left = BinaryOperation(left, op, factor())
        }

        return left
    }

    private fun factor(): Expr {
        var left = unary()

        while (matchAny(STAR, SLASH)) {
            val op = previous()
            left = BinaryOperation(left, op, unary())
        }

        return left
    }

    private fun unary(): Expr {
        if (match(MINUS)) {
            return UnaryOperation(previous(), primary())
        }

        return primary()
    }

    private fun primary(): Expr =
        when {
            match(OPEN_PAREN) -> {
                val expr = expression()
                consume(CLOSE_PAREN)
                expr
            }
            match(DEC_NUMBER) -> Number(previous().lexeme.toDouble())
            match(IDENTIFIER) -> Identifier(previous().lexeme)
            else -> throw ParseError("Unexpected Token: '${previous().lexeme}'")
        }
}