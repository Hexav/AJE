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

package xyz.avarel.kaiper.runtime.functions;

import xyz.avarel.kaiper.runtime.Obj;

import java.util.ArrayList;
import java.util.List;

public class CurriedFunction extends Function {
    private final Obj argument;
    private final Function delegate;

    public CurriedFunction(Function delegate, Obj argument) {
        super(delegate.getName());

        this.delegate = delegate;
        this.argument = argument;
    }

    @Override
    public Obj invoke(List<Obj> arguments) {
        arguments = new ArrayList<>(arguments);
        arguments.add(argument);

        return delegate.invoke(arguments);
    }

    @Override
    public String toString() {
        return "def " + argument + "::" + getName();
    }
}