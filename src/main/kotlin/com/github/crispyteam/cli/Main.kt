package com.github.crispyteam.cli

import com.github.crispyteam.interpreter.Interpreter
import com.github.crispyteam.interpreter.RuntimeError
import com.github.crispyteam.parsing.Lexer
import com.github.crispyteam.parsing.ParseError
import com.github.crispyteam.parsing.Parser

fun main(args: Array<String>) {
    val parser = Parser()
    val interpreter = Interpreter()

    var line: String?

    while (true) {
        print("> ")
        line = readLine() ?: break
        try {
            println(interpreter.eval(parser.parse(Lexer(line))))
        } catch (p: ParseError) {
            println("Errors while parsing: ${p.message}")
        } catch (r: RuntimeError) {
            println("Errors while Executing : ${r.message}")
        }
    }
}