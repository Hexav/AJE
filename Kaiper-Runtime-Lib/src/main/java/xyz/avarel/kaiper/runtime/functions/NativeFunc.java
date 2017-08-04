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

package xyz.avarel.kaiper.runtime.functions;

import xyz.avarel.kaiper.exceptions.ComputeException;
import xyz.avarel.kaiper.runtime.Null;
import xyz.avarel.kaiper.runtime.Obj;
import xyz.avarel.kaiper.runtime.Tuple;

import java.util.*;

public abstract class NativeFunc extends Func {
    //    private final Type receiverType;
    private final List<Parameter> parameters;

    public NativeFunc(String name) {
        this(name, new Parameter[0]);
    }

    public NativeFunc(String name, Parameter... parameters) {
        super(name);
        this.parameters = Arrays.asList(parameters);
    }

    public NativeFunc(String name, String... params) {
        super(name);
        this.parameters = new ArrayList<>(params.length);
        for (String param : params) {
            Parameter of = Parameter.of(param);
            parameters.add(of);
        }
    }

    @Override
    public int getArity() {
        return !parameters.isEmpty() && parameters.get(parameters.size() - 1).isRest()
                ? parameters.size() - 1
                : parameters.size();
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return super.toString() + "$native";
    }

    @Override
    public Obj invoke(Tuple argument) {
        if (argument.size() < getArity()) {
            throw new ComputeException(getName() + " requires " + getArity() + " arguments");
        }

        // todo make magic
        Map<String, Obj> arguments = new HashMap<>(getArity());

        Obj result = eval(arguments);
        return result != null ? result : Null.VALUE;
    }

    protected abstract Obj eval(Map<String, Obj> arguments);
}
