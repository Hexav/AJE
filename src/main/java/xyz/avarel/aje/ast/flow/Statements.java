/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package xyz.avarel.aje.ast.flow;

import xyz.avarel.aje.ast.Expr;
import xyz.avarel.aje.ast.ExprVisitor;
import xyz.avarel.aje.runtime.Obj;
import xyz.avarel.aje.scope.Scope;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Statements extends Expr implements Iterable<Expr> {
    private final List<Expr> statements;

    public Statements(Expr before, Expr after) {
        super(before.getPosition());

        this.statements = new ArrayList<>();
        statements.add(before);
        statements.add(after);
    }

    @Override
    public Expr andThen(Expr after) {
        statements.add(after);
        return this;
    }

    public List<Expr> getExprs() {
        return statements;
    }

    @Override
    public Obj accept(ExprVisitor visitor, Scope scope) {
        return visitor.visit(this, scope);
    }

    @Override
    public void ast(StringBuilder builder, String indent, boolean isTail) {
        for (int i = 0; i < statements.size() - 1; i++) {
            statements.get(i).ast(builder, indent, false);
            builder.append('\n');
        }
        if (statements.size() >= 1) {
            statements.get(statements.size() - 1).ast(builder, indent, true);
        }
    }

    @Override
    public String toString() {
        return "statements";
    }

    @Override
    public Iterator<Expr> iterator() {
        return statements.iterator();
    }
}
