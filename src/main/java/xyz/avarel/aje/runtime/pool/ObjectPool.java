package xyz.avarel.aje.runtime.pool;

import xyz.avarel.aje.runtime.Any;
import xyz.avarel.aje.runtime.Undefined;
import xyz.avarel.aje.runtime.Variable;

import java.util.HashMap;
import java.util.Map;

public class ObjectPool {
    private final Map<String, Any> pool;

    public ObjectPool() {
        pool = new HashMap<>();
    }

    public ObjectPool(ObjectPool copy) {
        pool = new HashMap<>(copy.pool);
    }

    public Any get(String key) {
        return pool.computeIfAbsent(key, k -> new Variable(Undefined.VALUE));
    }

    public Any put(String key, Any value) {
        return pool.put(key, value); // x = 2; fun a(y) = {y + 2}; a(3) + x
    }

    public ObjectPool copy() {
        return new ObjectPool(this);
    }
}
