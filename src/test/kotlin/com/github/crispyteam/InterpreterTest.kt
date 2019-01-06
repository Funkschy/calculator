package com.github.crispyteam

import com.github.crispyteam.interpreter.Interpreter
import com.github.crispyteam.parsing.CalcLexer
import com.github.crispyteam.parsing.Parser
import com.github.crispyteam.parsing.Peekable
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class InterpreterTest : StringSpec({
    "multiplication should be evaluated first" {
        val lexer = Peekable(CalcLexer("1 + 2 * -3"))
        val parser = Parser(lexer)
        val interpreter = Interpreter()

        val result = interpreter.eval(parser.parse())

        result shouldBe -5.0
    }
})