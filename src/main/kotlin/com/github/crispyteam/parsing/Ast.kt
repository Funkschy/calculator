package com.github.crispyteam.parsing

sealed class Expr {
    data class Identifier(val name: String) : Expr()
    data class Declaration(val identifier: Identifier, val value: Expr) : Expr()
    data class Number(val value: Double) : Expr()
    data class BinaryOperation(val left: Expr, val op: Token, val right: Expr) : Expr()
    data class UnaryOperation(val op: Token, val expr: Expr) : Expr()
}