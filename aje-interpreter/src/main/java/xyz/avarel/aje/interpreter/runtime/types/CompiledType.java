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

package xyz.avarel.aje.interpreter.runtime.types;

import xyz.avarel.aje.runtime.Obj;
import xyz.avarel.aje.runtime.types.Type;

import java.util.ArrayList;
import java.util.List;

public class CompiledType extends Type<CompiledObj> {
    public CompiledType(String name, Type parent) {
        super(parent, name);
    }

    @Override
    public Obj invoke(List<Obj> arguments) {
        List<Obj> newArguments = arguments instanceof ArrayList
                ? arguments
                : new ArrayList<>(arguments);

        Obj instance = new CompiledObj(this);

        newArguments.add(0, instance);

        super.invoke(newArguments);

        return instance;
    }
}