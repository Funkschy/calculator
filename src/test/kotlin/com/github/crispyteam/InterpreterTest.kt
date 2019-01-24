package com.github.crispyteam

import com.github.crispyteam.cli.interpreter
import com.github.crispyteam.parsing.Lexer
import com.github.crispyteam.parsing.Parser
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class CalcInterpreterTest : StringSpec({
    "multiplication should be evaluated first" {
        val lexer = Lexer("1 + 2 * -3.5")
        val parser = Parser()
        val interpreter = interpreter()

        val result = interpreter.eval(parser.parse(lexer.asSequence().toList()))

        result shouldBe -6.0
    }

    "variables should be saved in environment with evaluated value" {
        var lexer = Lexer("test = 1 + 2")
        val parser = Parser()
        val interpreter = interpreter()

        var result = interpreter.eval(parser.parse(lexer.asSequence().toList()))

        result shouldBe 3.0

        lexer = Lexer("test + 2")
        result = interpreter.eval(parser.parse(lexer))

        result shouldBe 5.0
    }

    "declarations should return the assigned value" {
        var lexer = Lexer("test = variable = 1 + 2")
        val parser = Parser()
        val interpreter = interpreter()

        var result = interpreter.eval(parser.parse(lexer.asSequence().toList()))

        result shouldBe 3.0

        lexer = Lexer("test + variable")
        result = interpreter.eval(parser.parse(lexer))

        result shouldBe 6.0
    }
})