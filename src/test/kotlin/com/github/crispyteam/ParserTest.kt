package com.github.crispyteam

import com.github.crispyteam.parsing.CalcLexer
import com.github.crispyteam.parsing.Expr.BinaryOperation
import com.github.crispyteam.parsing.Expr.Number
import com.github.crispyteam.parsing.Parser
import com.github.crispyteam.parsing.Peekable
import com.github.crispyteam.parsing.Token
import com.github.crispyteam.parsing.TokenType.PLUS
import com.github.crispyteam.parsing.TokenType.STAR
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec

class ParserTest : StringSpec({
    "parsing addition before multiplication should have correct precedence" {
        val lexer = Peekable(CalcLexer("1 + 2 * 3"))
        val parser = Parser(lexer)

        val expr = parser.parse()
        expr shouldNotBe null

        expr shouldBe BinaryOperation(
            Number(1.0),
            Token(PLUS, "+"),
            BinaryOperation(Number(2.0), Token(STAR, "*"), Number(3.0))
        )
    }

    "parsing multiplication before addition should have correct precedence" {
        val lexer = Peekable(CalcLexer("1 * 2 + 3"))
        val parser = Parser(lexer)

        val expr = parser.parse()
        expr shouldNotBe null

        expr shouldBe BinaryOperation(
            BinaryOperation(Number(1.0), Token(STAR, "*"), Number(2.0)),
            Token(PLUS, "+"),
            Number(3.0)
        )
    }
})