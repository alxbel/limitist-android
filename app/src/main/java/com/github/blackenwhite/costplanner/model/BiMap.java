package com.github.blackenwhite.costplanner.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BiMap<K, V> {
    private static final String TAG = "BiMap";

    Map<K, V> treeMap = new TreeMap<>();
    Map<V, K> inversedTreeMap = new TreeMap<>();

    public void put(K k, V v) {
        treeMap.put(k, v);
        inversedTreeMap.put(v, k);
    }

    public void putAll(K[] keys, V[] values) {
        for (int i = 0; i < keys.length; i++) {
            treeMap.put(keys[i], values[i]);
            inversedTreeMap.put(values[i], keys[i]);
        }
    }

    public void clear() {
        treeMap.clear();
        inversedTreeMap.clear();
    }

    public Map<K, V> getTreeMap() {
        return treeMap;
    }

    public V getValue(K k) {
        V v = treeMap.get(k);
        if (v == null) {
            throw new IllegalArgumentException();
        }
        return v;
    }

    public K getKey(V v) throws IllegalArgumentException {
        K k = inversedTreeMap.get(v);
        if (k == null) {
            if (v instanceof String) {
                k = inversedTreeMap.get(((String) v).toLowerCase());
                if (k == null) {
                    throw new IllegalArgumentException();
                }
            }
        }
        return k;
    }

    public List<V> getValues() {
        List<V> list = new ArrayList<>();
        list.addAll(treeMap.values());
        return list;
    }

    public int size() {
        return treeMap.size();
    }
}
