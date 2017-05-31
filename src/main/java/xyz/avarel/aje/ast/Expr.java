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

package xyz.avarel.aje.ast;

import xyz.avarel.aje.ast.flow.ReturnException;
import xyz.avarel.aje.ast.flow.Statements;
import xyz.avarel.aje.exceptions.AJEException;
import xyz.avarel.aje.exceptions.ComputeException;
import xyz.avarel.aje.parser.lexer.Position;
import xyz.avarel.aje.runtime.Obj;
import xyz.avarel.aje.scope.DefaultScope;
import xyz.avarel.aje.scope.Scope;

public abstract class Expr {
    private final Position position;

    protected Expr(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public abstract Obj accept(ExprVisitor visitor, Scope scope);

    public Expr andThen(Expr after) {
        return new Statements(this, after);
    }

    public Obj compute() {
        try {
            return accept(new ExprVisitor(), DefaultScope.INSTANCE.copy());
        } catch (ReturnException re) {
            return re.getValue();
        } catch (AJEException re) {
            throw re;
        } catch (RuntimeException re) {
            throw new ComputeException(re);
        }
    }

    public void ast(StringBuilder builder, String indent, boolean isTail) {
        builder.append(indent).append(isTail ? "└── " : "├── ").append(toString());
    }

    public void ast(String label, StringBuilder builder, String indent, boolean tail) {
        builder.append(indent).append(tail ? "└── " : "├── ").append(label).append(": \n");
        ast(builder, indent + (tail ? "    " : "│   "), true);
    }
}
