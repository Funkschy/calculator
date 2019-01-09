package com.github.crispyteam.parsing

import com.github.crispyteam.parsing.Expr.*
import com.github.crispyteam.parsing.Expr.Number
import com.github.crispyteam.parsing.TokenType.*

class ParseError(msg: String) : RuntimeException(msg)

class Parser {
    private lateinit var tokens: List<Token>
    private var position = 0

    fun parse(tokens: Iterator<Token>) = parse(tokens.asSequence().toList())

    fun parse(tokens: List<Token>): Expr {
        val errors = tokens.asSequence().filter { it.type == ERROR }.map { it.lexeme }.joinToString(", ")
        if (errors.isNotEmpty()) {
            throw ParseError("Lex Errors: $errors")
        }

        this.tokens = tokens
        this.position = 0

        return expression()
    }

    private fun advance() {
        position++
    }

    private fun goBack() {
        position--
    }

    private fun atEnd() = position >= tokens.size

    private fun current() = tokens[position]

    private fun previous() = tokens[position - 1]

    private fun match(tokenType: TokenType): Boolean {
        if (!atEnd() && current().type == tokenType) {
            advance()
            return true
        }

        return false
    }

    private fun consume(tokenType: TokenType) {
        if (!match(tokenType)) throw ParseError("Expected '$tokenType', but got '${previous().type}'")
    }

    private fun matchAny(vararg tokenType: TokenType) = tokenType.any { match(it) }

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

        return arithExpr()
    }

    private fun arithExpr(): Expr {
        var left = term()

        while (matchAny(PLUS, MINUS)) {
            val op = previous()
            left = BinaryOperation(left, op, term())
        }

        return left
    }

    private fun term(): Expr {
        var left = factor()

        while (matchAny(STAR, SLASH)) {
            val op = previous()
            left = BinaryOperation(left, op, factor())
        }

        return left
    }

    private fun factor(): Expr {
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