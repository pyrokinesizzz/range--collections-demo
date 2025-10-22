package ru.vsu.vinyukov.demo.rangeset;

import ru.vsu.vinyukov.demo.model.Range; // Исправленный импорт
import java.util.Set;

public interface RangeSet<T extends Comparable<T>> {
    void add(Range<T> range);
    void add(T element);
    void remove(Range<T> range);
    void remove(T element);
    boolean contains(T element);
    boolean intersects(Range<T> range);
    Range<T> span();
    Set<Range<T>> asRanges();
    boolean isEmpty();
    void clear();
    int size();
}