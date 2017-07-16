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

package xyz.avarel.aje.runtime.numbers;

import xyz.avarel.aje.runtime.Bool;
import xyz.avarel.aje.runtime.Obj;
import xyz.avarel.aje.runtime.Undefined;
import xyz.avarel.aje.runtime.types.NativeConstructor;
import xyz.avarel.aje.runtime.types.Type;

import java.util.List;

public class Decimal implements Obj<Double> {
    public static final Type<Decimal> TYPE = new DecimalType();

    private final double value;

    private Decimal(double value) {
        this.value = value;
    }

    public static Decimal of(double value) {
        return new Decimal(value);
    }

    public double value() {
        return value;
    }

    @Override
    public Double toJava() {
        return value();
    }

    @Override
    public Type<Decimal> getType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Obj) {
            return isEqualTo((Obj) obj) == Bool.TRUE;
        } else {
            return Double.valueOf(value) == obj;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public Obj plus(Obj other) {
        if (other instanceof Decimal) {
            return plus((Decimal) other);
        }
        return Obj.super.plus(other);
    }

    private Decimal plus(Decimal other) {
        return Decimal.of(value + other.value);
    }

    @Override
    public Obj minus(Obj other) {
        if (other instanceof Decimal) {
            return minus((Decimal) other);
        }
        return Obj.super.minus(other);
    }

    private Decimal minus(Decimal other) {
        return Decimal.of(value - other.value);
    }

    @Override
    public Obj times(Obj other) {
        if (other instanceof Decimal) {
            return times((Decimal) other);
        }
        return Obj.super.times(other);
    }

    private Decimal times(Decimal other) {
        return Decimal.of(value * other.value);
    }

    @Override
    public Obj divide(Obj other) {
        if (other instanceof Decimal) {
            return divide((Decimal) other);
        }
        return Obj.super.divide(other);
    }

    public Decimal divide(Decimal other) {
        return Decimal.of(value / other.value);
    }

    @Override
    public Obj pow(Obj other) {
        if (other instanceof Decimal) {
            return pow((Decimal) other);
        }
        return Obj.super.pow(other);
    }

    private Decimal pow(Decimal other) {
        return Decimal.of(Math.pow(value, other.value));
    }

    @Override
    public Obj mod(Obj other) {
        if (other instanceof Decimal) {
            return mod((Decimal) other);
        }
        return Obj.super.mod(other);
    }

    private Decimal mod(Decimal other) {
        return Decimal.of((value % other.value + other.value) % other.value);
    }

    public Decimal negative() {
        return Decimal.of(-value);
    }

    @Override
    public Obj isEqualTo(Obj other) {
        if (other instanceof Decimal) {
            return this.isEqualTo((Decimal) other);
        }
        return Bool.FALSE;
    }

    private Obj isEqualTo(Decimal other) {
        return Bool.of(value == other.value);
    }

    @Override
    public Obj greaterThan(Obj other) {
        if (other instanceof Decimal) {
            return this.greaterThan((Decimal) other);
        }
        return Bool.FALSE;
    }

    private Obj greaterThan(Decimal other) {
        return Bool.of(value > other.value);
    }

    @Override
    public Obj lessThan(Obj other) {
        if (other instanceof Decimal) {
            return this.lessThan((Decimal) other);
        }
        return Bool.FALSE;
    }

    private Bool lessThan(Decimal other) {
        return Bool.of(value < other.value);
    }

    private static class DecimalType extends Type<Decimal> {
        public DecimalType() {
            super(Numeric.TYPE, "Decimal", new NativeConstructor(Obj.TYPE) {
                @Override
                protected Obj eval(List<Obj> arguments) {
                    Obj obj = arguments.get(0);
                    if (obj instanceof Decimal) {
                        return obj;
                    } else if (obj instanceof Int) {
                        return Decimal.of(((Int) obj).value());
                    }
                    try {
                        return Decimal.of(Double.parseDouble(obj.toString()));
                    } catch (NumberFormatException e) {
                        return Undefined.VALUE;
                    }
                }
            });

            getScope().declare("MAX_VALUE", Decimal.of(Double.MAX_VALUE));
            getScope().declare("MIN_VALUE", Decimal.of(Double.MIN_VALUE));
            getScope().declare("NEGATIVE_INFINITY", Decimal.of(Double.NEGATIVE_INFINITY));
            getScope().declare("POSITIVE_INFINITY", Decimal.of(Double.POSITIVE_INFINITY));
            getScope().declare("NaN", Decimal.of(Double.NaN));
            getScope().declare("BYTES", Decimal.of(Double.BYTES));
            getScope().declare("SIZE", Decimal.of(Double.SIZE));
        }
    }
}
