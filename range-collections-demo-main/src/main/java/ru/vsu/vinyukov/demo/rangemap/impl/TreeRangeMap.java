package ru.vsu.vinyukov.demo.rangemap.impl;

import ru.vsu.vinyukov.demo.model.Range;
import ru.vsu.vinyukov.demo.rangemap.RangeMap;

import java.util.*;
import java.util.Map.Entry;

public class TreeRangeMap<K extends Comparable<K>, V> implements RangeMap<K, V> {
    private final TreeMap<Range<K>, V> map;

    public TreeRangeMap() {
        this.map = new TreeMap<>();
    }

    @Override
    public void put(Range<K> range, V value) {
        if (range == null) {
            throw new IllegalArgumentException("Range cannot be null");
        }

        // Удаляем пересекающиеся диапазоны
        Set<Range<K>> toRemove = new HashSet<>();
        for (Range<K> existing : map.keySet()) {
            if (existing.intersects(range)) {
                toRemove.add(existing);
            }
        }

        for (Range<K> r : toRemove) {
            map.remove(r);
        }

        map.put(range, value);
    }

    @Override
    public V get(K key) {
        for (Entry<Range<K>, V> entry : map.entrySet()) {
            if (entry.getKey().contains(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public void remove(Range<K> range) {
        map.remove(range);
    }

    @Override
    public void remove(K key) {
        Range<K> toRemove = null;
        for (Range<K> range : map.keySet()) {
            if (range.contains(key)) {
                toRemove = range;
                break;
            }
        }
        if (toRemove != null) {
            map.remove(toRemove);
        }
    }

    @Override
    public Set<Entry<Range<K>, V>> asMapOfRanges() {
        return new HashSet<>(map.entrySet());
    }

    @Override
    public Set<Range<K>> asKeySet() {
        return new TreeSet<>(map.keySet());
    }

    @Override
    public Collection<V> values() {
        return new ArrayList<>(map.values());
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Entry<Range<K>, V> getEntry(K key) {
        for (Entry<Range<K>, V> entry : map.entrySet()) {
            if (entry.getKey().contains(key)) {
                return new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue());
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return map.toString();
    }
}