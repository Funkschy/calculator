package com.github.crispyteam.parsing

import com.github.crispyteam.parsing.TokenType.*

class ParseError(msg: String) : RuntimeException(msg)

class Parser(private val lexer: Peekable<Token>) {
    private lateinit var previous: Token

    fun parse(): Expr {
        return expression()
    }

    private fun advance() {
        previous = lexer.next()
    }

    private fun match(tokenType: TokenType): Boolean {
        if (lexer.peek()?.type == tokenType) {
            advance()
            return true
        }

        return false
    }

    private fun consume(tokenType: TokenType) {
        if (!match(tokenType)) throw ParseError("Expected $tokenType, but got $previous")
    }

    private fun matchAny(vararg tokenType: TokenType) = tokenType.any { match(it) }

    private fun expression(): Expr {
        if (lexer.hasNext()) {
            return arithExpr()
        }

        throw ParseError("Unexpected end of file")
    }

    private fun arithExpr(): Expr {
        var left = term()

        while (matchAny(PLUS, MINUS)) {
            val op = previous
            left = Expr.BinaryOperation(left, op, term())
        }

        return left
    }

    private fun term(): Expr {
        var left = factor()

        while (matchAny(STAR, SLASH)) {
            val op = previous
            left = Expr.BinaryOperation(left, op, factor())
        }

        return left
    }

    private fun factor(): Expr {
        if (match(MINUS)) {
            return Expr.UnaryOperation(previous, primary())
        }

        return primary()
    }

    private fun primary(): Expr =
        when {
            match(OPEN_PAREN) -> {
                val expr = expression() ?: throw ParseError("Unexpected end of file")
                consume(CLOSE_PAREN)
                expr
            }
            match(DEC_NUMBER) -> Expr.Number(previous.lexeme.toDouble())
            else -> throw ParseError("Unexpected Token: $previous")
        }
}