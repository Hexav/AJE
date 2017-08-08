///*
// * Licensed under the Apache License, Version 2.0 (the
// * "License"); you may not use this file except in compliance
// * with the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing,
// * software distributed under the License is distributed on an
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// * KIND, either express or implied.  See the License for the
// * specific language governing permissions and limitations
// * under the License.
// */
//
//package xyz.avarel.kaiper.runtime.types;
//
//import xyz.avarel.kaiper.exceptions.ComputeException;
//import xyz.avarel.kaiper.runtime.Obj;
//import xyz.avarel.kaiper.runtime.Tuple;
//import xyz.avarel.kaiper.runtime.functions.Parameter;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//public abstract class NativeConstructor extends Constructor {
//    private final List<Parameter> parameters;
//
//    public NativeConstructor() {
//        this.parameters = Collections.emptyList();
//    }
//
//    public NativeConstructor(Parameter... parameters) {
//        this.parameters = new ArrayList<>();
//        this.parameters.addAll(Arrays.asList(parameters));
//    }
//
//    @Override
//    public Obj invoke(Tuple argument) {
//        if (argument.size() < getArity()) {
//            throw new ComputeException(targetType + " constructor requires " + getArity() + " arguments");
//        }
//
//        return eval(argument);
//    }
//
//    protected abstract Obj eval(List<Obj> arguments);
//}
