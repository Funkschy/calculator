package com.github.crispyteam.interpreter

import com.github.crispyteam.parsing.Expr
import com.github.crispyteam.parsing.TokenType.*

class RuntimeError(msg: String) : RuntimeException(msg)

class Interpreter {
    private val variables: MutableMap<String, Double> = HashMap()

    fun eval(expr: Expr) =
        when (expr) {
            is Expr.BinaryOperation -> evalBinary(expr)
            is Expr.UnaryOperation -> evalUnary(expr)
            is Expr.Number -> evalNumber(expr)
            is Expr.Declaration -> evalDecl(expr)
        }

    private fun evalBinary(expr: Expr.BinaryOperation): Double {
        val left = eval(expr.left)
        val right = eval(expr.right)

        return when (expr.op.type) {
            PLUS -> left + right
            MINUS -> left - right
            STAR -> left * right
            SLASH -> left / right
            else -> throw RuntimeError("Invalid operator: ${expr.op}")
        }
    }

    private fun evalNumber(expr: Expr.Number) = expr.value

    private fun evalUnary(expr: Expr.UnaryOperation): Double = -eval(expr.expr)

    private fun evalDecl(expr: Expr.Declaration): Double {
        val value = eval(expr.value)
        variables[expr.name] = value
        return value
    }
}