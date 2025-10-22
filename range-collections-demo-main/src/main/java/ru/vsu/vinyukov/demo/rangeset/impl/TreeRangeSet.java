package ru.vsu.vinyukov.demo.rangeset.impl;

import ru.vsu.vinyukov.demo.model.Range;
import ru.vsu.vinyukov.demo.rangeset.RangeSet;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class TreeRangeSet<T extends Comparable<T>> implements RangeSet<T> {
    private final TreeSet<Range<T>> ranges;

    public TreeRangeSet() {
        this.ranges = new TreeSet<>();
    }

    @Override
    public void add(Range<T> newRange) {
        if (newRange == null) {
            throw new IllegalArgumentException("Range cannot be null");
        }

        Set<Range<T>> toMerge = new HashSet<>();
        Iterator<Range<T>> iterator = ranges.iterator();

        while (iterator.hasNext()) {
            Range<T> existing = iterator.next();
            if (existing.isConnected(newRange)) {
                toMerge.add(existing);
                iterator.remove();
            }
        }

        if (!toMerge.isEmpty()) {
            toMerge.add(newRange);
            Range<T> merged = mergeAll(toMerge);
            ranges.add(merged);
        } else {
            ranges.add(newRange);
        }
    }

    @Override
    public void add(T element) {
        add(new Range<>(element, element, true, true));
    }

    @Override
    public void remove(Range<T> rangeToRemove) {
        if (rangeToRemove == null) return;

        Set<Range<T>> toAdd = new HashSet<>();
        Iterator<Range<T>> iterator = ranges.iterator();

        while (iterator.hasNext()) {
            Range<T> existing = iterator.next();
            if (existing.intersects(rangeToRemove)) {
                iterator.remove();
                toAdd.addAll(subtract(existing, rangeToRemove));
            }
        }

        for (Range<T> range : toAdd) {
            ranges.add(range);
        }
    }

    @Override
    public void remove(T element) {
        remove(new Range<>(element, element, true, true));
    }

    @Override
    public boolean contains(T element) {
        for (Range<T> range : ranges) {
            if (range.contains(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean intersects(Range<T> range) {
        for (Range<T> existing : ranges) {
            if (existing.intersects(range)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Range<T> span() {
        if (ranges.isEmpty()) {
            return null;
        }
        return ranges.first().span(ranges.last());
    }

    @Override
    public Set<Range<T>> asRanges() {
        return new TreeSet<>(ranges);
    }

    @Override
    public boolean isEmpty() {
        return ranges.isEmpty();
    }

    @Override
    public void clear() {
        ranges.clear();
    }

    @Override
    public int size() {
        return ranges.size();
    }

    private Range<T> mergeAll(Set<Range<T>> rangesToMerge) {
        T min = null;
        T max = null;
        boolean minInclusive = false;
        boolean maxInclusive = false;

        for (Range<T> range : rangesToMerge) {
            if (min == null || range.getLower().compareTo(min) < 0) {
                min = range.getLower();
                minInclusive = range.isLowerInclusive();
            } else if (range.getLower().compareTo(min) == 0) {
                minInclusive = minInclusive || range.isLowerInclusive();
            }

            if (max == null || range.getUpper().compareTo(max) > 0) {
                max = range.getUpper();
                maxInclusive = range.isUpperInclusive();
            } else if (range.getUpper().compareTo(max) == 0) {
                maxInclusive = maxInclusive || range.isUpperInclusive();
            }
        }

        return new Range<>(min, max, minInclusive, maxInclusive);
    }

    private Set<Range<T>> subtract(Range<T> from, Range<T> what) {
        Set<Range<T>> result = new HashSet<>();

        if (from.getLower().compareTo(what.getLower()) < 0) {
            T leftUpper = what.getLower();
            boolean leftUpperInclusive = !what.isLowerInclusive();
            if (leftUpperInclusive || from.getLower().compareTo(leftUpper) < 0) {
                result.add(new Range<>(from.getLower(), leftUpper,
                        from.isLowerInclusive(), leftUpperInclusive));
            }
        }

        if (from.getUpper().compareTo(what.getUpper()) > 0) {
            T rightLower = what.getUpper();
            boolean rightLowerInclusive = !what.isUpperInclusive();
            if (rightLowerInclusive || from.getUpper().compareTo(rightLower) > 0) {
                result.add(new Range<>(rightLower, from.getUpper(),
                        rightLowerInclusive, from.isUpperInclusive()));
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return ranges.toString();
    }
}