package com.github.crispyteam.interpreter

import com.github.crispyteam.parsing.Expr
import com.github.crispyteam.parsing.Expr.*
import com.github.crispyteam.parsing.Expr.Number
import com.github.crispyteam.parsing.TokenType.*
import com.github.crispyteam.parsing.ExprVisitor

class RuntimeError(msg: String) : RuntimeException(msg)

interface Interpreter: ExprVisitor {
    fun eval(expr: Expr) = expr.accept(this)
}

class CalcInterpreter : Interpreter {
    private val variables: MutableMap<Identifier, Double> = HashMap()

    override fun eval(expr: Expr) = expr.accept(this)

    override fun evalBinary(expr: BinaryOperation): Double {
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

    override fun evalNumber(expr: Number) = expr.value

    override fun evalUnary(expr: UnaryOperation): Double = -eval(expr.expr)

    override fun evalDecl(expr: Declaration): Double {
        val value = eval(expr.value)
        variables[expr.identifier] = value
        return value
    }

    override fun evalIdentifier(expr: Identifier) = variables[expr]
        ?: throw RuntimeError("Could not find variable with name '${expr.name}'")
}