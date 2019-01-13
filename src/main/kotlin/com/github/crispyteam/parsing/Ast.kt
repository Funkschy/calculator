package com.github.crispyteam.parsing

import com.github.crispyteam.interpreter.Interpreter

sealed class Expr {
    abstract fun accept(interpreter: Interpreter): Double

    data class Identifier(val name: String) : Expr() {
        override fun accept(interpreter: Interpreter) = interpreter.evalIdentifier(this)
    }

    data class Declaration(val identifier: Identifier, val value: Expr) : Expr() {
        override fun accept(interpreter: Interpreter) = interpreter.evalDecl(this)
    }

    data class Number(val value: Double) : Expr() {
        override fun accept(interpreter: Interpreter) = interpreter.evalNumber(this)
    }

    data class BinaryOperation(val left: Expr, val op: Token, val right: Expr) : Expr() {
        override fun accept(interpreter: Interpreter) = interpreter.evalBinary(this)
    }

    data class UnaryOperation(val op: Token, val expr: Expr) : Expr() {
        override fun accept(interpreter: Interpreter) = interpreter.evalUnary(this)
    }
}