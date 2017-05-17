package xyz.avarel.aje.runtime.pool;

import xyz.avarel.aje.runtime.Any;
import xyz.avarel.aje.runtime.Slice;
import xyz.avarel.aje.runtime.Truth;
import xyz.avarel.aje.runtime.Undefined;
import xyz.avarel.aje.runtime.functions.AJEFunction;
import xyz.avarel.aje.runtime.functions.ComposedFunction;
import xyz.avarel.aje.runtime.functions.NativeFunction;
import xyz.avarel.aje.runtime.numbers.Complex;
import xyz.avarel.aje.runtime.numbers.Decimal;
import xyz.avarel.aje.runtime.numbers.Int;
import xyz.avarel.aje.runtime.numbers.Numeric;

import java.util.Collections;
import java.util.List;

public enum DefaultFunctions {
    SQUARE_ROOT(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                double value = Numeric.convert(a, Decimal.TYPE).toNative();

                if (value < 0) {
                    return this.invoke(Complex.of(value));
                }

                return Decimal.of(Math.sqrt(value));
            } else if (a instanceof Complex) {
                return ((Complex) a).pow(Complex.of(0.5, 0));
            }
            return Undefined.VALUE;
        }
    }),
    CUBE_ROOT(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(Math.cbrt(Numeric.convert(a, Decimal.TYPE).toNative()));
            } else if (a instanceof Complex) {
                return ((Complex) a).pow(Complex.of(0.3333333333333333, 0));
            }
            return Undefined.VALUE;
        }
    }),
    EXPONENTIAL(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(Math.exp(Numeric.convert(a, Decimal.TYPE).toNative()));
            } else if (a instanceof Complex) {
                return ((Complex) a).exp();
            }
            return Undefined.VALUE;
        }
    }),
    LOG10(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(Math.log10(Numeric.convert(a, Decimal.TYPE).toNative()));
            } else if (a instanceof Complex) {
                return ((Complex) a).ln().divide(Complex.of(10).ln());
            }
            return Undefined.VALUE;
        }
    }),
    LOG_NATURAL(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(Math.log(Numeric.convert(a, Decimal.TYPE).toNative()));
            } else if (a instanceof Complex) {
                return ((Complex) a).ln();
            }
            return Undefined.VALUE;
        }
    }),
    ROUND(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(Math.round(Numeric.convert(a, Decimal.TYPE).toNative()));
            } else if (a instanceof Complex) {
                return ((Complex) a).round();
            }
            return Undefined.VALUE;
        }
    }),
    FLOOR(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(Math.floor(Numeric.convert(a, Decimal.TYPE).toNative()));
            } else if (a instanceof Complex) {
                return ((Complex) a).floor();
            }
            return Undefined.VALUE;
        }
    }),
    CEILING(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(Math.ceil(Numeric.convert(a, Decimal.TYPE).toNative()));
            } else if (a instanceof Complex) {
                return ((Complex) a).ceil();
            }
            return Undefined.VALUE;
        }
    }),

    SUM(new NativeFunction(true, Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            if (arguments.isEmpty()) return Int.of(0);
            Any accumulator = arguments.get(0);
            for (int i = 1; i < arguments.size(); i++) {
                accumulator = Numeric.process(accumulator, arguments.get(i), Any::times);
            }
            return accumulator;
        }
    }),

    PRODUCT(new NativeFunction(true, Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            if (arguments.isEmpty()) return Int.of(0);
            Any accumulator = arguments.get(0);
            for (int i = 1; i < arguments.size(); i++) {
                accumulator = Numeric.process(accumulator, arguments.get(i), Any::times);
            }
            return accumulator;
        }
    }),

    COMPOSE(new NativeFunction(AJEFunction.TYPE, AJEFunction.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            return new ComposedFunction((AJEFunction) arguments.get(0), (AJEFunction) arguments.get(1));
        }
    }),

    SINE(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(Math.sin(Numeric.convert(a, Decimal.TYPE).toNative()));
            } else if (a instanceof Complex) {
                return ((Complex) a).sin();
            }
            return Undefined.VALUE;
        }
    }),
    COSINE(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(Math.cos(Numeric.convert(a, Decimal.TYPE).toNative()));
            } else if (a instanceof Complex) {
                return ((Complex) a).cos();
            }
            return Undefined.VALUE;
        }
    }),
    TANGENT(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(Math.tan(Numeric.convert(a, Decimal.TYPE).toNative()));
            } else if (a instanceof Complex) {
                return ((Complex) a).tan();
            }
            return Undefined.VALUE;
        }
    }),
    COSECANT(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(1).divide((Decimal) SINE.get().invoke(Numeric.convert(a, Decimal.TYPE)));
            } else if (a instanceof Complex) {
                return Complex.of(1).divide((Complex) SINE.get().invoke(Numeric.convert(a, Complex.TYPE)));
            }
            return Undefined.VALUE;
        }
    }),
    SECANT(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(1).divide((Decimal) COSINE.get().invoke(Numeric.convert(a, Decimal.TYPE)));
            } else if (a instanceof Complex) {
                return Complex.of(1).divide((Complex) COSINE.get().invoke(Numeric.convert(a, Complex.TYPE)));
            }
            return Undefined.VALUE;
        }
    }),
    COTANGENT(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(1).divide((Decimal) TANGENT.get().invoke(Numeric.convert(a, Decimal.TYPE)));
            } else if (a instanceof Complex) {
                return Complex.of(1).divide((Complex) TANGENT.get().invoke(Numeric.convert(a, Complex.TYPE)));
            }
            return Undefined.VALUE;
        }
    }),
    HYPERBOLIC_SINE(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(Math.sinh(Numeric.convert(a, Decimal.TYPE).toNative()));
            } else if (a instanceof Complex) {
                return ((Complex) a).sinh();
            }
            return Undefined.VALUE;
        }
    }),
    HYPERBOLIC_COSINE(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(Math.cosh(Numeric.convert(a, Decimal.TYPE).toNative()));
            } else if (a instanceof Complex) {
                return ((Complex) a).cosh();
            }
            return Undefined.VALUE;
        }
    }),
    HYPERBOLIC_TANGENT(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(Math.tanh(Numeric.convert(a, Decimal.TYPE).toNative()));
            } else if (a instanceof Complex) {
                return ((Complex) a).tanh();
            }
            return Undefined.VALUE;
        }
    }),
    HYPERBOLIC_COSECANT(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(1).divide((Decimal) HYPERBOLIC_SINE.get().invoke(Numeric.convert(a, Decimal.TYPE)));
            } else if (a instanceof Complex) {
                return Complex.of(1).divide((Complex) HYPERBOLIC_SINE.get().invoke(Numeric.convert(a, Complex.TYPE)));
            }
            return Undefined.VALUE;
        }
    }),
    HYPERBOLIC_SECANT(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(1).divide((Decimal) HYPERBOLIC_COSINE.get().invoke(Numeric.convert(a, Decimal.TYPE)));
            } else if (a instanceof Complex) {
                return Complex.of(1).divide((Complex) HYPERBOLIC_COSINE.get().invoke(Numeric.convert(a, Complex.TYPE)));
            }
            return Undefined.VALUE;
        }
    }),
    HYPERBOLIC_COTANGENT(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            if (a instanceof Int || a instanceof Decimal) {
                return Decimal.of(1)
                        .divide((Decimal) HYPERBOLIC_TANGENT.get().invoke(Numeric.convert(a, Decimal.TYPE)));
            } else if (a instanceof Complex) {
                return HYPERBOLIC_COSINE.get().invoke(a).divide(HYPERBOLIC_SINE.get().invoke(a));
            }
            return Undefined.VALUE;
        }
    }),
    ARCSINE(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            return Decimal.of(Math.asin(Numeric.convert(a, Decimal.TYPE).toNative()));
        }
    }),
    ARCCOSINE(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            return Decimal.of(Math.acos(Numeric.convert(a, Decimal.TYPE).toNative()));
        }
    }),
    ARCTANGENT(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            return Decimal.of(Math.atan(Numeric.convert(a, Decimal.TYPE).toNative()));
        }
    }),
    ARCCOSECANT(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            return ARCSINE.get().invoke(Decimal.of(1).divide(Numeric.convert(a, Decimal.TYPE)));
        }
    }),
    ARCSECANT(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            return ARCCOSINE.get().invoke(Decimal.of(1).divide(Numeric.convert(a, Decimal.TYPE)));
        }
    }),
    ARCCOTANGENT(new NativeFunction(Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            return ARCTANGENT.get().invoke(Decimal.of(1).divide(Numeric.convert(a, Decimal.TYPE)));
        }
    }),
    ARCTANGENT2(new NativeFunction(Numeric.TYPE, Numeric.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Any a = arguments.get(0);
            Any b = arguments.get(1);
            return Decimal.of(Math.atan2(
                    Numeric.convert(a, Decimal.TYPE).toNative(),
                    Numeric.convert(b, Decimal.TYPE).toNative()));
        }
    }),

    MAP(new NativeFunction(Slice.TYPE, AJEFunction.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Slice arg = (Slice) arguments.get(0);
            AJEFunction transform = (AJEFunction) arguments.get(1);

            Slice slice = new Slice();
            for (Any obj : arg) {
                slice.add(transform.invoke(Collections.singletonList(obj)));
            }
            return slice;
        }
    }),
    FILTER(new NativeFunction(Slice.TYPE, AJEFunction.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Slice arg = (Slice) arguments.get(0);
            AJEFunction predicate = (AJEFunction) arguments.get(1);

            Slice slice = new Slice();
            for (Any obj : arg) {
                Truth condition = (Truth) predicate.invoke(Collections.singletonList(obj));
                if (condition == Truth.TRUE) slice.add(obj);
            }
            return slice;
        }
    }),
    FOLD(new NativeFunction(Slice.TYPE, Any.TYPE, AJEFunction.TYPE) {
        @Override
        protected Any eval(List<Any> arguments) {
            Slice arg = (Slice) arguments.get(0);
            Any accumulator = arguments.get(1);
            AJEFunction operation = (AJEFunction) arguments.get(2);

            for (Any obj : arg) {
                accumulator = operation.invoke(accumulator, obj);
            }
            return accumulator;
        }
    }),;
    private final NativeFunction function;

    DefaultFunctions(NativeFunction function) {
        this.function = function;
    }

    public NativeFunction get() {
        return function;
    }
}