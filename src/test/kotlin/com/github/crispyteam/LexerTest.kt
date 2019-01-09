package com.github.crispyteam

import com.github.crispyteam.parsing.Lexer
import com.github.crispyteam.parsing.Token
import com.github.crispyteam.parsing.TokenType.*
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class LexerTest : StringSpec({
    "test lexer as sequence returns the correct tokens" {
        val lexer = Lexer("1 + 2 * 3")
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