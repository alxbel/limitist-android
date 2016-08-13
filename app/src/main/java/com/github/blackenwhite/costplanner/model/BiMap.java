package com.github.blackenwhite.costplanner.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BiMap<K, V> {
    private static final String TAG = "BiMap";

    Map<K, V> mTreeMap = new TreeMap<>();
    Map<V, K> mInversedMap = new TreeMap<>();

    public void put(K k, V v) {
        mTreeMap.put(k, v);
        mInversedMap.put(v, k);
    }

    public void putAll(K[] keys, V[] values) {
        for (int i = 0; i < keys.length; i++) {
            mTreeMap.put(keys[i], values[i]);
            mInversedMap.put(values[i], keys[i]);
        }
    }

    public void clear() {
        mTreeMap.clear();
        mInversedMap.clear();
    }

    public Map<K, V> getTreeMap() {
        return mTreeMap;
    }

    public V getValue(K k) {
        V v = mTreeMap.get(k);
        if (v == null) {
            throw new IllegalArgumentException();
        }
        return v;
    }

    public K getKey(V v) throws IllegalArgumentException {
        K k = mInversedMap.get(v);
        if (k == null) {
            if (v instanceof String) {
                k = mInversedMap.get(((String) v).toLowerCase());
                if (k == null) {
                    throw new IllegalArgumentException();
                }
            }
        }
        return k;
    }

    public List<V> getValues() {
        List<V> list = new ArrayList<>();
        list.addAll(mTreeMap.values());
        return list;
    }

    public int size() {
        return mTreeMap.size();
    }
}
