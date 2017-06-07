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

package xyz.avarel.aje.runtime;

import xyz.avarel.aje.runtime.collections.Vector;
import xyz.avarel.aje.runtime.functions.NativeFunc;
import xyz.avarel.aje.runtime.numbers.Int;

import java.util.List;

public class Text implements Obj<String> {
    public static final Type<Text> TYPE = new TextType();

    private final String value;

    private Text(String value) {
        this.value = value;
    }

    public static Text of(String value) {
        return new Text(value);
    }

    public String value() {
        return value;
    }

    @Override
    public String toJava() {
        return value();
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return value();
    }

    public int length() {
        return value().length();
    }

    @Override
    public Obj plus(Obj other) {
        return Text.of(value() + other.toString());
    }

    @Override
    public Obj get(Obj key) {
        if (key instanceof Int) {
            return get((Int) key);
        }
        return Undefined.VALUE;
    }

    private Obj get(Int index) {
        int i = index.value();
        if (i < 0) {
            i += length();
        }
        if (i < 0 || i >= length()) {
            return Undefined.VALUE;
        }
        return Text.of(value().substring(i, i + 1));
    }

    public char get(int i) {
        return value().charAt(i);
    }

    @Override
    public Obj slice(Obj startObj, Obj endObj, Obj stepObj) {
        int start;
        int end;
        int step;

        if (startObj == null) {
            start = 0;
        } else {
            if (startObj instanceof Int) {
                start = ((Int) startObj).value();
                if (start < 0) {
                    start += length();
                }
            } else {
                return Undefined.VALUE;
            }
        }

        if (endObj == null) {
            end = length();
        } else {
            if (endObj instanceof Int) {
                end = ((Int) endObj).value();
                if (end < 0) {
                    end += length();
                }
            } else {
                return Undefined.VALUE;
            }
        }

        if (stepObj == null) {
            step = 1;
        } else {
            if (stepObj instanceof Int) {
                step = ((Int) stepObj).value();
            } else {
                return Undefined.VALUE;
            }
        }

        if (step == 1) {
            return Text.of(value().substring(start, end));
        } else {
            if (step > 0) {
                StringBuilder buffer = new StringBuilder();

                for (int i = start; i < end; i += step) {
                    buffer.append(get(i));
                }

                return Text.of(buffer.toString());
            } else if (step < 0) {
                StringBuilder buffer = new StringBuilder();

                for (int i = end - 1; i >= start; i += step) {
                    buffer.append(get(i));
                }

                return Text.of(buffer.toString());
            } else { // step == 0
                return Undefined.VALUE;
            }
        }
    }

    @Override
    public Obj getAttr(String name) {
        switch (name) {
            case "length":
                return Int.of(length());
            case "lastIndex":
                return Int.of(length() - 1);
            case "type":
                return TYPE;
        }
        return Obj.super.getAttr(name);
    }

    public Bool contains(Text text) {
        return Bool.of(value.contains(text.value));
    }

    public Int indexOf(Text text) {
        return Int.of(value.indexOf(text.value));
    }

    public Vector split(Text text) {
        Vector vector = new Vector();
        for (String part : value.split(text.value)) {
            vector.add(Text.of(part));
        }
        return vector;
    }

    public Bool startsWith(Text text) {
        return Bool.of(value.startsWith(text.value));
    }

    public Text substring(Int start) {
        return Text.of(value.substring(start.value()));
    }

    public Text substring(int start) {
        return Text.of(value.substring(start));
    }

    public Text substring(Int start, Int end) {
        return Text.of(value.substring(start.value(), end.value()));
    }

    public Text substring(int start, int end) {
        return Text.of(value.substring(start, end));
    }

    public Vector toVector() {
        Vector vector = new Vector();
        for (int i = 0; i < length(); i++) {
            vector.add(substring(i, i + 1));
        }
        return vector;
    }

    public Text toLowerCase() {
        return Text.of(value.toLowerCase());
    }

    public Text toUpperCase() {
        return Text.of(value.toUpperCase());
    }

    public Text trim() {
        return Text.of(value.trim());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Text && value().equals(((Text) obj).value());
    }

    @Override
    public int hashCode() {
        return value().hashCode();
    }

    private static class TextType extends Type<Text> {
        public TextType() {
            super("String");

            getScope().declare("contains", new NativeFunc(this, this) {
                @Override
                protected Obj eval(List<Obj> arguments) {
                    return ((Text) arguments.get(0)).contains((Text) arguments.get(1));
                }
            });
            getScope().declare("indexOf", new NativeFunc(this, this) {
                @Override
                protected Obj eval(List<Obj> arguments) {
                    return ((Text) arguments.get(0)).indexOf((Text) arguments.get(1));
                }
            });
            getScope().declare("split", new NativeFunc(this, this) {
                @Override
                protected Obj eval(List<Obj> arguments) {
                    return ((Text) arguments.get(0)).split((Text) arguments.get(1));
                }
            });
            getScope().declare("substring", new NativeFunc(this, Int.TYPE) {
                @Override
                protected Obj eval(List<Obj> arguments) {
                    if (arguments.size() >= 2) {
                        if (arguments.get(1) instanceof Int) {
                            return ((Text) arguments.get(0)).substring((Int) arguments.get(1), (Int) arguments.get(2));
                        }
                        return Undefined.VALUE;
                    } else {
                        return ((Text) arguments.get(0)).substring((Int) arguments.get(1));
                    }
                }
            });
            getScope().declare("toVector", new NativeFunc(this) {
                @Override
                protected Obj eval(List<Obj> arguments) {
                    return ((Text) arguments.get(0)).toVector();
                }
            });
            getScope().declare("toLowerCase", new NativeFunc(this) {
                @Override
                protected Obj eval(List<Obj> arguments) {
                    return ((Text) arguments.get(0)).toLowerCase();
                }
            });
            getScope().declare("toUpperCase", new NativeFunc(this) {
                @Override
                protected Obj eval(List<Obj> arguments) {
                    return ((Text) arguments.get(0)).toUpperCase();
                }
            });
            getScope().declare("trim", new NativeFunc(this) {
                @Override
                protected Obj eval(List<Obj> arguments) {
                    return ((Text) arguments.get(0)).trim();
                }
            });
        }
    }
}
