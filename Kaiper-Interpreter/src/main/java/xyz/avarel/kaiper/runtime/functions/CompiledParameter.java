package xyz.avarel.kaiper.runtime.functions;

import xyz.avarel.kaiper.ast.Expr;

public class CompiledParameter extends Parameter {
    private final Expr defaultExpr;

    public CompiledParameter(String name, Expr defaultExpr, boolean rest) {
        super(name,rest);
        this.defaultExpr = defaultExpr;
    }

    public boolean hasDefault() {
        return defaultExpr != null;
    }

    public Expr getDefaultExpr() {
        return defaultExpr;
    }
}
