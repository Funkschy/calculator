package com.github.crispyteam

import com.github.crispyteam.parsing.Expr
import com.github.crispyteam.parsing.Expr.BinaryOperation
import com.github.crispyteam.parsing.Expr.Number
import com.github.crispyteam.parsing.Lexer
import com.github.crispyteam.parsing.Parser
import com.github.crispyteam.parsing.Token
import com.github.crispyteam.parsing.TokenType.PLUS
import com.github.crispyteam.parsing.TokenType.STAR
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec

class ParserTest : StringSpec({
    "parsing addition before multiplication should have correct precedence" {
        val lexer = Lexer("1 + 2 * 3")
        val parser = Parser()

        val expr = parser.parse(lexer.asSequence().toList())
        expr shouldNotBe null

        expr shouldBe BinaryOperation(
                Number(1.0),
                Token(PLUS, "+"),
                BinaryOperation(Number(2.0), Token(STAR, "*"), Number(3.0))
        )
    }

    "parsing multiplication before addition should have correct precedence" {
        val lexer = Lexer("1 * 2 + 3")
        val parser = Parser()

        val expr = parser.parse(lexer.asSequence().toList())
        expr shouldNotBe null

        expr shouldBe BinaryOperation(
                BinaryOperation(Number(1.0), Token(STAR, "*"), Number(2.0)),
                Token(PLUS, "+"),
                Number(3.0)
        )
    }

    "parsing name equals expression should return correctly formed declaration expression" {
        val lexer = Lexer("test = 1 * 2")
        val parser = Parser()

        val expr = parser.parse(lexer.asSequence().toList())
        expr shouldNotBe null

        expr shouldBe Expr.Declaration(
                Expr.Identifier("test"),
                BinaryOperation(Number(1.0), Token(STAR, "*"), Number(2.0))
        )
    }

    "nested declarations should be parsed correctly" {
        val lexer = Lexer("test = variable = 1 + 2")
        val parser = Parser()

        val expr = parser.parse(lexer)
        expr shouldNotBe null

        expr shouldBe Expr.Declaration(
                Expr.Identifier("test"),
                Expr.Declaration(
                        Expr.Identifier("variable"),
                        Expr.BinaryOperation(
                                Expr.Number(1.0),
                                Token(PLUS, "+"),
                                Expr.Number(2.0)
                        )
                )
        )
    }
})