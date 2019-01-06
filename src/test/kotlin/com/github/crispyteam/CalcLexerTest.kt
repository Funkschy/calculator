package com.github.crispyteam

import com.github.crispyteam.parsing.CalcLexer
import com.github.crispyteam.parsing.Peekable
import com.github.crispyteam.parsing.Token
import com.github.crispyteam.parsing.TokenType.*
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class CalcLexerTest : StringSpec({
    "test peek should return same token, until next was called" {
        val lexer = Peekable(CalcLexer("1 + 1"))
        lexer.peek() shouldBe Token(DEC_NUMBER, "1")
        lexer.peek() shouldBe Token(DEC_NUMBER, "1")
        lexer.hasNext() shouldBe true

        lexer.next() shouldBe Token(DEC_NUMBER, "1")
        lexer.peek() shouldBe Token(PLUS, "+")
        lexer.peek() shouldBe Token(PLUS, "+")
        lexer.hasNext() shouldBe true

        lexer.next() shouldBe Token(PLUS, "+")
        lexer.peek() shouldBe Token(DEC_NUMBER, "1")
        lexer.peek() shouldBe Token(DEC_NUMBER, "1")
        lexer.hasNext() shouldBe true

        lexer.next() shouldBe Token(DEC_NUMBER, "1")
        lexer.peek() shouldBe null
        lexer.peek() shouldBe null
        lexer.hasNext() shouldBe false
    }

    "test lexer as sequence returns the correct tokens" {
        val lexer = CalcLexer("1 + 2 * 3")
        val tokens = lexer.asSequence().toList()

        val expected = listOf(
            Token(DEC_NUMBER, "1"),
            Token(PLUS, "+"),
            Token(DEC_NUMBER, "2"),
            Token(STAR, "*"),
            Token(DEC_NUMBER, "3")
        )

        tokens shouldBe expected
    }
})