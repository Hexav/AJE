package xyz.avarel.kaiper.ast.pattern;

import xyz.avarel.kaiper.lexer.Position;

// x
public class VariablePattern extends NamedPattern {
    public VariablePattern(Position position, String name) {
        super(position, name);
    }

    @Override
    public <R, C> R accept(PatternVisitor<R, C> visitor, C scope) {
        return visitor.visit(this, scope);
    }

    @Override
    public String toString() {
        return getName();
    }
}