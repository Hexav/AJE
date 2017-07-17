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

package xyz.avarel.aje.runtime.functions;

public class Parameter {
    private final String name;
    private final boolean rest;

    protected Parameter(String name, boolean rest) {
        this.name = name;
        this.rest = rest;
    }

    public static Parameter of(String name) {
        return Parameter.of(name, false);
    }

    public static Parameter of(String name, boolean rest) {
        return new Parameter(name, rest);
    }

    public String getName() {
        return name;
    }

    public boolean isRest() {
        return rest;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (rest) {
            sb.append("...");
        }

        if (name != null) {
            sb.append(name);
        }

        return sb.toString();
    }
}
