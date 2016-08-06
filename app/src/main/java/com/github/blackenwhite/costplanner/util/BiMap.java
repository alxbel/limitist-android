package com.github.blackenwhite.costplanner.util;

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
            throw new IllegalArgumentException();
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
