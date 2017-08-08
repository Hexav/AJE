package xyz.avarel.kaiper.runtime.types;

import xyz.avarel.kaiper.ast.Expr;
import xyz.avarel.kaiper.exceptions.ComputeException;
import xyz.avarel.kaiper.exceptions.InterpreterException;
import xyz.avarel.kaiper.interpreter.ExprInterpreter;
import xyz.avarel.kaiper.interpreter.PatternBinder;
import xyz.avarel.kaiper.pattern.Pattern;
import xyz.avarel.kaiper.pattern.PatternCase;
import xyz.avarel.kaiper.pattern.RestPattern;
import xyz.avarel.kaiper.runtime.Tuple;
import xyz.avarel.kaiper.scope.Scope;

public class CompiledConstructor extends Constructor {
    private final PatternCase patternCase;
    private final ExprInterpreter visitor;
    private final Scope scope;
    private final Expr expr;

    public CompiledConstructor(PatternCase patternCase, Expr expr, ExprInterpreter visitor, Scope scope) {
        this.patternCase = patternCase;
        this.visitor = visitor;
        this.scope = scope;
        this.expr = expr;
    }

    @Override
    public int getArity() {
        boolean rest = false;
        for (Pattern pattern : patternCase.getPatterns()) {
            if (pattern instanceof RestPattern) rest = true;
        }

        return patternCase.size() - (rest ? 1 : 0);
    }

    @Override
    public CompiledObj invoke(Tuple arguments) {
        if (!(targetType instanceof CompiledType)) {
            throw new ComputeException("Internal error");
        }

        Scope constructorScope = this.scope.subPool();

        if (!new PatternBinder(patternCase, visitor, constructorScope).bindFrom(arguments)) {
            throw new InterpreterException("Could not match arguments (" + arguments + ") to " + getName() + "(" + patternCase + ")");
        }

        CompiledObj instance = new CompiledObj((CompiledType) targetType, this.scope.subPool());

        constructorScope.declare("this", instance);

        expr.accept(visitor, constructorScope);

        return instance;
    }

}