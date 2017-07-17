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

package xyz.avarel.aje.runtime.modules;

import xyz.avarel.aje.runtime.Obj;
import xyz.avarel.aje.runtime.Undefined;

import java.util.HashMap;
import java.util.Map;

public abstract class NativeModule extends Module {
    private final Map<String, Obj> map;

    protected NativeModule() {
        this.map = new HashMap<>();
    }

    public void declare(String name, Obj obj) {
        map.put(name, obj);
    }

    @Override
    public Obj getAttr(String name) {
        return map.getOrDefault(name, Undefined.VALUE);
    }
}