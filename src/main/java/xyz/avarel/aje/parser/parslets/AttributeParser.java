package xyz.avarel.aje.parser.parslets;

import xyz.avarel.aje.Precedence;
import xyz.avarel.aje.parser.AJEParser;
import xyz.avarel.aje.parser.BinaryParser;
import xyz.avarel.aje.parser.lexer.Token;
import xyz.avarel.aje.parser.lexer.TokenType;
import xyz.avarel.aje.runtime.types.Any;

public class AttributeParser extends BinaryParser<Any, Any> {
    public AttributeParser() {
        super(Precedence.ACCESS, true);
    }

    @Override
    public Any parse(AJEParser parser, Any left, Token token) {
        Token ntoken = parser.eat(TokenType.NAME);

        return left.get(ntoken.getText());
    }
}
