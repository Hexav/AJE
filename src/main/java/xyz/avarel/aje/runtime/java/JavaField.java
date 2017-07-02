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

package xyz.avarel.aje.runtime.java;

import xyz.avarel.aje.runtime.Obj;
import xyz.avarel.aje.runtime.Type;
import xyz.avarel.aje.runtime.Undefined;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class JavaField extends JavaObject implements Obj<Object> {
    private final JavaObject parent;
    private final String name;

    public JavaField(JavaObject parent, String name) {
        super(parent.getObject());
        this.parent = parent;
        this.name = name;
    }

    public JavaField(JavaField parent, String name) {
        super(parent.getField());
        this.parent = parent;
        this.name = name;
    }

    public Object getField() {
        if (name != null) {
            try {
                Field field = getObject().getClass().getField(name);
                return field.get(getObject());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public Type getType() {
        Object field = getField();
        if (JavaUtils.hasAJETypeEquivalent(field)) {
            return JavaUtils.mapJavaToAJEType(field).getType();
        }

        return type;
    }

    @Override
    public Object toJava() {
        Object field = getField();
        if (JavaUtils.hasAJETypeEquivalent(field)) {
            return JavaUtils.mapJavaToAJEType(field).toJava();
        }

        return getField();
    }

    @Override
    public Obj setAttr(String name, Obj value) {
        if (name != null) {
            try {
                Field field = getField().getClass().getField(name);
                Object val = value.toJava();
                field.set(getField(), val);
                return value;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return Undefined.VALUE;
            }
        }
        return Undefined.VALUE;
    }

    @Override
    public Obj getAttr(String name) {
        return new JavaField(this, name);
    }

    @Override
    public Obj invoke(List<Obj> arguments) {
        if (name == null) return Undefined.VALUE;

        List<Object> nativeArgs = arguments.stream()
                .map(Obj::toJava)
                .collect(Collectors.toList());

        List<Class<?>> classes = nativeArgs.stream()
                .map(Object::getClass)
                .collect(Collectors.toList());

        Object result = null;

        outer: for (Method method : getObject().getClass().getMethods()) {
            if (!method.getName().equals(name)) continue;
            if (classes.size() != method.getParameterCount()) continue;

            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < method.getParameterCount(); i++) {

                if (!JavaUtils.isAssignable(classes.get(i), parameterTypes[i])) {
                    continue outer;
                }
            }

            try {
                result = method.invoke(getObject(), nativeArgs.toArray());
            } catch (IllegalAccessException | InvocationTargetException ignore) {}
        }

        if (parent instanceof JavaField && result == ((JavaField) parent).getField()
                || parent != null && result == parent.getObject()) {
            return parent;
        }

        return JavaUtils.mapJavaToAJEType(result);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JavaField) {
            return getField() == ((JavaField) obj).getField();
        } else if (obj instanceof JavaObject) {
            return getField() == ((JavaObject) obj).getObject();
        }

        Object field = getField();

        if (JavaUtils.hasAJETypeEquivalent(field)) {
            return JavaUtils.mapJavaToAJEType(field).equals(obj);
        }

        return getObject() == obj;
    }

    @Override
    public String toString() {
        Object field = getField();
        if (JavaUtils.hasAJETypeEquivalent(field)) {
            return JavaUtils.mapJavaToAJEType(field).toString();
        }

        return getField().toString();
    }

    @Override
    public int hashCode() {
        Object field = getField();
        if (JavaUtils.hasAJETypeEquivalent(field)) {
            return JavaUtils.mapJavaToAJEType(field).hashCode();
        }

        return getField().hashCode();
    }
}