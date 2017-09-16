/*
 *  Copyright 2017 An Tran and Adrian Todt
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package xyz.avarel.kaiper.ast.pattern;

import xyz.avarel.kaiper.interpreter.ExprInterpreter;
import xyz.avarel.kaiper.runtime.Obj;
import xyz.avarel.kaiper.runtime.Tuple;
import xyz.avarel.kaiper.scope.Scope;

public class PatternTester implements PatternVisitor<Boolean, Tuple> {
    private final PatternCase patternCase;
    private final ExprInterpreter interpreter;
    private final Scope scope;

    private int position = 0;

    public PatternTester(PatternCase patternCase, ExprInterpreter interpreter, Scope scope) {
        this.patternCase = patternCase;
        this.interpreter = interpreter;
        this.scope = scope;
    }

    public boolean test(Tuple tuple) {
        for (Pattern pattern : patternCase.getPatterns()) {
            boolean result = pattern.accept(this, tuple);

            if (result) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Boolean visit(PatternCase patternCase, Tuple obj) {
        if (obj.hasAttr("_" + position)) {
            Obj value = obj.getAttr("_" + position);

            position++;

            Tuple tuple = value instanceof Tuple ? (Tuple) value : new Tuple(value);

            return new PatternTester(patternCase, interpreter, scope).test(tuple);
        } else {
            return false;
        }
    }

    @Override
    public Boolean visit(WildcardPattern pattern, Tuple obj) {
        position++;
        return true;
    }

    @Override
    public Boolean visit(ValuePattern pattern, Tuple obj) {
        if (obj.hasAttr("_" + position)) {
            Obj value = obj.getAttr("_" + position);
            Obj target = pattern.getValue().accept(interpreter, scope);
            position++;
            return value.equals(target);
        } else {
            return null;
        }
    }

    @Override
    public Boolean visit(VariablePattern pattern, Tuple obj) {
        if (obj.hasAttr(pattern.getName()) || obj.hasAttr("_" + position)) {
            position++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean visit(DefaultPattern pattern, Tuple obj) {
        return true;
    }

    @Override
    public Boolean visit(RestPattern pattern, Tuple obj) {
        return true;
    }

    @Override
    public Boolean visit(TuplePattern pattern, Tuple obj) {
        Obj value;

        if (obj.hasAttr(pattern.getName())) {
            value = obj.getAttr(pattern.getName());
        } else {
            return false;
        }

        position++;

        Tuple tuple = new Tuple(value);

        // check later
        return pattern.getPattern().accept(new PatternTester(patternCase, interpreter, scope), tuple);
    }
}
