package com.github.crispyteam.interpreter

import com.github.crispyteam.parsing.Expr
import com.github.crispyteam.parsing.Expr.*
import com.github.crispyteam.parsing.Expr.Number
import com.github.crispyteam.parsing.TokenType.*

class RuntimeError(msg: String) : RuntimeException(msg)

class Interpreter {
    private val variables: MutableMap<Identifier, Double> = HashMap()

    fun eval(expr: Expr) =
            when (expr) {
                is BinaryOperation -> evalBinary(expr)
                is UnaryOperation -> evalUnary(expr)
                is Number -> evalNumber(expr)
                is Declaration -> evalDecl(expr)
                is Identifier -> evalIdentifier(expr)
            }

    private fun evalBinary(expr: BinaryOperation): Double {
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

    private fun evalNumber(expr: Number) = expr.value

    private fun evalUnary(expr: UnaryOperation): Double = -eval(expr.expr)

    private fun evalDecl(expr: Declaration): Double {
        val value = eval(expr.value)
        variables[expr.identifier] = value
        return value
    }

    private fun evalIdentifier(expr: Identifier) = variables[expr]
            ?: throw RuntimeError("Could not find variable with name '${expr.name}'")
}