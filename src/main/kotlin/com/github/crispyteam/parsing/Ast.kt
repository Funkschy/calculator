package com.github.crispyteam.parsing

import com.github.crispyteam.parsing.Expr.*

interface ExprVisitor {
    fun evalBinary(expr: BinaryOperation): Double

    fun evalNumber(expr: Expr.Number): Double

    fun evalUnary(expr: UnaryOperation): Double

    fun evalDecl(expr: Declaration): Double

    fun evalIdentifier(expr: Identifier): Double
}

sealed class Expr {
    abstract fun accept(visitor: ExprVisitor): Double

    data class Identifier(val name: String) : Expr() {
        override fun accept(visitor: ExprVisitor) = visitor.evalIdentifier(this)
    }

    data class Declaration(val identifier: Identifier, val value: Expr) : Expr() {
        override fun accept(visitor: ExprVisitor) = visitor.evalDecl(this)
    }

    data class Number(val value: Double) : Expr() {
        override fun accept(visitor: ExprVisitor) = visitor.evalNumber(this)
    }

    data class BinaryOperation(val left: Expr, val op: Token, val right: Expr) : Expr() {
        override fun accept(visitor: ExprVisitor) = visitor.evalBinary(this)
    }

    data class UnaryOperation(val op: Token, val expr: Expr) : Expr() {
        override fun accept(visitor: ExprVisitor) = visitor.evalUnary(this)
    }
}