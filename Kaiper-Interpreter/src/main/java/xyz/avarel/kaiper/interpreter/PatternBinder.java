package xyz.avarel.kaiper.interpreter;

import xyz.avarel.kaiper.Pair;
import xyz.avarel.kaiper.ast.pattern.*;
import xyz.avarel.kaiper.runtime.Obj;
import xyz.avarel.kaiper.runtime.Tuple;
import xyz.avarel.kaiper.runtime.collections.Array;
import xyz.avarel.kaiper.scope.Scope;

import java.util.LinkedHashMap;
import java.util.Map;

public class PatternBinder implements PatternVisitor<Pair<String, Obj>, Tuple> {
    private static final Pair<String, Obj> SUCCESS_NO_ASSIGNMENT = new Pair<>(null, null);

    private final PatternCase patternCase;
    private final ExprInterpreter interpreter;
    private final Scope scope;

    private int position = 0;

    public PatternBinder(PatternCase patternCase, ExprInterpreter interpreter, Scope scope) {
        this.patternCase = patternCase;
        this.interpreter = interpreter;
        this.scope = scope;
    }

    public boolean bind(Tuple tuple) {
        Map<String, Obj> results = new LinkedHashMap<>();
        for (Pattern pattern : patternCase.getPatterns()) {
            Pair<String, Obj> result = pattern.accept(this, tuple);

            if (result != null) {
                if (result != SUCCESS_NO_ASSIGNMENT) {
//                    if (results.containsKey(result.getFirst())) {
//                        throw new InterpreterException("Duplicate field matches for " + result.getFirst());
//                    }
                    results.put(result.getFirst(), result.getSecond());
                }
            } else {
                return false;
            }
        }

        for (Map.Entry<String, Obj> result : results.entrySet()) {
            scope.declare(result.getKey(), result.getValue());
        }

        return true;
    }

    @Override
    public Pair<String, Obj> visit(PatternCase patternCase, Tuple obj) {
        if (obj.hasAttr("_" + position)) {
            Obj value = obj.getAttr("_" + position);

            position++;

            Tuple tuple = value instanceof Tuple ? (Tuple) value : new Tuple(value);

            if (new PatternBinder(patternCase, interpreter, scope).bind(tuple)) {
                return SUCCESS_NO_ASSIGNMENT;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Pair<String, Obj> visit(WildcardPattern pattern, Tuple obj) {
        position++;
        return SUCCESS_NO_ASSIGNMENT;
    }

    @Override
    public Pair<String, Obj> visit(ValuePattern pattern, Tuple obj) {
        System.out.println(obj);
        System.out.println(pattern);

        if (obj.hasAttr("_" + position)) {
            Obj value = obj.getAttr("_" + position);
            Obj target = pattern.getValue().accept(interpreter, scope);
            position++;
            return value.equals(target) ? SUCCESS_NO_ASSIGNMENT : null;
        } else {
            return null;
        }
    }

    @Override
    public Pair<String, Obj> visit(VariablePattern pattern, Tuple obj) {
        Obj value;

        if (obj.hasAttr(pattern.getName())) {
            value = obj.getAttr(pattern.getName());
        } else if (obj.hasAttr("_" + position)) {
            value = obj.getAttr("_" + position);
        } else {
            return null;
        }

        position++;
        return new Pair<>(pattern.getName(), value);
    }

    @Override
    public Pair<String, Obj> visit(DefaultPattern pattern, Tuple obj) {
        Pair<String, Obj> result = pattern.getDelegate().accept(this, obj);

        if (result == null) {
            Obj value = pattern.getDefaultExpr().accept(interpreter, scope);
            return new Pair<>(pattern.getDelegate().getName(), value);
        } else {
            return result;
        }
    }

    @Override
    public Pair<String, Obj> visit(RestPattern pattern, Tuple obj) {
        Obj value;

        if (obj.hasAttr(pattern.getName())) {
            Obj val = obj.getAttr(pattern.getName());
            value = val instanceof Array ? val : Array.of(val);
        } else { // empty
            int endPosition = obj.size() - (patternCase.size() - (patternCase.getPatterns().indexOf(pattern) + 1));

            if (position != endPosition && obj.hasAttr("_" + position)) {
                Array array = Array.of();

                do {
                    array.add(obj.getAttr("_" + position));
                    position++;
                } while (position < endPosition && obj.hasAttr("_" + position));

                value = array;
            } else {
                return new Pair<>(pattern.getName(), new Array());
            }
        }

        return new Pair<>(pattern.getName(), value);
    }

    @Override
    public Pair<String, Obj> visit(TuplePattern pattern, Tuple obj) {
        Obj value;

        if (obj.hasAttr(pattern.getName())) {
            value = obj.getAttr(pattern.getName());
        } else {
            return null;
        }

        position++;

        Tuple tuple = new Tuple(value);

        // check later
        return pattern.getPattern().accept(new PatternBinder(patternCase, interpreter, scope), tuple);
    }
}