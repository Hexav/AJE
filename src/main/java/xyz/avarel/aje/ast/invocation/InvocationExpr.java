package xyz.avarel.aje.ast.invocation;

import xyz.avarel.aje.ast.Expr;
import xyz.avarel.aje.ast.ExprVisitor;
import xyz.avarel.aje.runtime.Obj;
import xyz.avarel.aje.scope.Scope;

import java.util.ArrayList;
import java.util.List;

public class InvocationExpr implements Expr {
    private final Expr left;
    private final List<Expr> arguments;

    public InvocationExpr(Expr left, List<Expr> arguments) {
        this.left = left;
        this.arguments = arguments;
    }

    public Expr getLeft() {
        return left;
    }

    public List<Expr> getArguments() {
        return arguments;
    }

    public InvocationExpr copy() {
        return new InvocationExpr(left, new ArrayList<>(arguments));
    }

    @Override
    public Obj accept(ExprVisitor visitor, Scope scope) {
        return visitor.visit(this, scope);
    }

    @Override
    public void ast(StringBuilder builder, String indent, boolean isTail) {
        builder.append(indent).append(isTail ? "└── " : "├── ").append("invoke\n");
        if (arguments.isEmpty()) {
            left.ast("target", builder, indent + (isTail ? "    " : "│   "), true);
        } else {
            left.ast("target", builder, indent + (isTail ? "    " : "│   "), false);
            builder.append('\n');
            for (int i = 0; i < arguments.size() - 1; i++) {
                arguments.get(i).ast(builder, indent + (isTail ? "    " : "│   "), false);
                builder.append('\n');
            }
            if (arguments.size() > 0) {
                arguments.get(arguments.size() - 1).ast(builder, indent + (isTail ? "    " : "│   "), true);
            }
        }
    }

    @Override
    public String toString() {
        return "invocation";
    }
}
