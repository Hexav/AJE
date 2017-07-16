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

package xyz.avarel.aje.parser.parslets.variables;

import xyz.avarel.aje.ast.Expr;
import xyz.avarel.aje.ast.value.UndefinedNode;
import xyz.avarel.aje.ast.variables.DeclarationExpr;
import xyz.avarel.aje.exceptions.SyntaxException;
import xyz.avarel.aje.lexer.Token;
import xyz.avarel.aje.lexer.TokenType;
import xyz.avarel.aje.parser.AJEParser;
import xyz.avarel.aje.parser.PrefixParser;

public class DeclarationParser implements PrefixParser {
    @Override
    public Expr parse(AJEParser parser, Token token) {
        if (!parser.getParserFlags().allowVariables()) {
            throw new SyntaxException("Variables are disabled");
        }

        Token name = parser.eat(TokenType.IDENTIFIER);

        if (parser.match(TokenType.ASSIGN)) {
            return new DeclarationExpr(name.getString(), parser.parseSingle());
        }

        return new DeclarationExpr(name.getString(), UndefinedNode.VALUE);
    }
}