package xyz.avarel.aje.parser.parslets.types;

import xyz.avarel.aje.parser.AJEParser;
import xyz.avarel.aje.parser.PrefixParser;
import xyz.avarel.aje.parser.expr.Expr;
import xyz.avarel.aje.parser.expr.ValueExpr;
import xyz.avarel.aje.parser.lexer.Token;
import xyz.avarel.aje.parser.lexer.TokenType;
import xyz.avarel.aje.runtime.types.compiled.CompiledFunction;

import java.util.ArrayList;
import java.util.List;

public class LambdaParser implements PrefixParser<Expr> {
    @Override
    public Expr parse(AJEParser parser, Token token) {
        List<String> parameters = new ArrayList<>();
        List<Token> scriptTokens = new ArrayList<>();

        boolean implicit = false;

        if (parser.match(TokenType.LEFT_PAREN)) {
            if (!parser.match(TokenType.RIGHT_PAREN)) {
                do {
                    Token t = parser.eat(TokenType.NAME);
                    parameters.add(t.getText());
                } while (parser.match(TokenType.COMMA));
                parser.eat(TokenType.RIGHT_PAREN);
            }
            parser.eat(TokenType.ARROW);
        }

        while (!parser.match(TokenType.RIGHT_BRACE)) {
            Token t = parser.eat();

            if (t.getText().equals("it")) implicit = true;

            scriptTokens.add(t);
        }

        if (!parameters.contains("it") && implicit) parameters.add("it");

        return new ValueExpr(new CompiledFunction(parameters, scriptTokens, parser.getObjects()));
    }
}
