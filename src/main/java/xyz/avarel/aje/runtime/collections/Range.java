/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
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

package xyz.avarel.aje.runtime.collections;

import xyz.avarel.aje.runtime.NativeObject;
import xyz.avarel.aje.runtime.Obj;
import xyz.avarel.aje.runtime.Type;
import xyz.avarel.aje.runtime.Undefined;
import xyz.avarel.aje.runtime.numbers.Int;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Range implements Obj, Iterable<Int>, NativeObject<List<Integer>> {
    public static final Type TYPE = new Type("range");
    
    private final int start;
    private final int end;

    public Range(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public List<Integer> toNative() {
        List<Integer> list = new ArrayList<>();
        for (Int i : this) {
            list.add(i.value());
        }
        return list;
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    public Vector toVector() {
        Vector vector = new Vector();
        for (Int i : this) {
            vector.add(i);
        }
        return vector;
    }

    @Override
    public String toString() {
        return start + ".." + end;
    }

    @Override
    public Obj get(Obj key) {
        if (key instanceof Int) {
            return get((Int) key);
        }
        return Undefined.VALUE;
    }

    public Obj get(Int index) {
        if (index.value() < size()) {
            return Int.of(start + index.value());
        }
        return Undefined.VALUE;
    }

    @Override
    public Iterator<Int> iterator() {
        return new RangeIterator();
    }

    public int size() {
        return end - start + 1;
    }

    @Override
    public Obj getAttr(String name) {
        switch (name) {
            case "size":
                return Int.of(size());
            case "length":
                return Int.of(size());
            case "lastIndex":
                return Int.of(size() - 1);
        }
        return Undefined.VALUE;
    }

    private final class RangeIterator implements Iterator<Int> {
        private int cursor;

        private RangeIterator() {
            this.cursor = start;
        }

        @Override
        public boolean hasNext() {
            return start < end ? cursor <= end : cursor >= end;
        }

        @Override
        public Int next() {
            return Int.of(start < end ? cursor++ : cursor--);
        }
    }
}