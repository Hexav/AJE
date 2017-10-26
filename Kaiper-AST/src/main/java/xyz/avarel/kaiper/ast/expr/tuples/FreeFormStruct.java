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

package xyz.avarel.kaiper.ast.expr.tuples;

import xyz.avarel.kaiper.ast.ExprVisitor;
import xyz.avarel.kaiper.ast.expr.Expr;
import xyz.avarel.kaiper.lexer.Position;

import java.util.Map;

public class FreeFormStruct extends Expr {
    private final Map<String, Expr> elements;

    public FreeFormStruct(Position position, Map<String, Expr> elements) {
        super(position);
        this.elements = elements;
    }

    public Map<String, Expr> getElements() {
        return elements;
    }

    public int size() {
        return elements.size();
    }

    @Override
    public <R, C> R accept(ExprVisitor<R, C> visitor, C context) {
        return visitor.visit(this, context);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof FreeFormStruct && elements.equals(((FreeFormStruct) o).elements);
    }

    @Override
    public String toString() {
        return elements.toString();
    }
}