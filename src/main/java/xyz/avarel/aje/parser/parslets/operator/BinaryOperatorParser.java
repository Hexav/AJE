package xyz.avarel.aje.parser.parslets.operator;

import xyz.avarel.aje.parser.AJEParser;
import xyz.avarel.aje.parser.BinaryParser;
import xyz.avarel.aje.parser.expr.BinaryOperation;
import xyz.avarel.aje.parser.expr.Expr;
import xyz.avarel.aje.parser.lexer.Token;
import xyz.avarel.aje.runtime.types.Any;

import java.util.function.BinaryOperator;

public class BinaryOperatorParser extends BinaryParser<Expr, Expr> {
    private final BinaryOperator<Any> operator;

    public BinaryOperatorParser(int precedence, boolean leftAssoc, BinaryOperator<Any> operator) {
        super(precedence, leftAssoc);
        this.operator = operator;
    }

    @Override
    public Expr parse(AJEParser parser, Expr left, Token token) {
        Expr right = parser.parse(getPrecedence() - (isLeftAssoc() ? 0 : 1));
        return new BinaryOperation(left, right, operator);
    }
}