/*
 * Licensed under the Apache License, Version 2.0 (the
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

package xyz.avarel.aje.ast.operations;

import xyz.avarel.aje.ast.Expr;
import xyz.avarel.aje.ast.ExprVisitor;
import xyz.avarel.aje.runtime.Obj;
import xyz.avarel.aje.scope.Scope;

import java.util.function.BinaryOperator;

public class BinaryOperation implements Expr {
    private final Expr left;
    private final Expr right;
    private final BinaryOperator<Obj> operator;

    public BinaryOperation(Expr left, Expr right, BinaryOperator<Obj> operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public Expr getLeft() {
        return left;
    }

    public Expr getRight() {
        return right;
    }

    public BinaryOperator<Obj> getOperator() {
        return operator;
    }

    @Override
    public Obj accept(ExprVisitor visitor, Scope scope) {
        return visitor.visit(this, scope);
    }

    @Override
    public void ast(StringBuilder builder, String indent, boolean isTail) {
        builder.append(indent).append(isTail ? "└── " : "├── ").append("binary op");

        builder.append('\n');
        left.ast(builder, indent + (isTail ? "    " : "│   "), false);

        builder.append('\n');
        right.ast(builder, indent + (isTail ? "    " : "│   "), true);
    }
}
