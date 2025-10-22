package ru.vsu.vinyukov.demo.rangemap;

import ru.vsu.vinyukov.demo.model.Range;

import java.util.Map;
import java.util.Set;

public interface RangeMap<K extends Comparable<K>, V> {
    void put(Range<K> range, V value);
    V get(K key);
    void remove(Range<K> range);
    void remove(K key);
    Set<Map.Entry<Range<K>, V>> asMapOfRanges();
    Set<Range<K>> asKeySet();
    java.util.Collection<V> values();
    boolean isEmpty();
    void clear();
    int size();
    Map.Entry<Range<K>, V> getEntry(K key);
}