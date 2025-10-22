package ru.vsu.vinyukov.demo.model;

import java.util.Objects;

public class Range<T extends Comparable<T>> implements Comparable<Range<T>> {
    private final T lower;
    private final T upper;
    private final boolean lowerInclusive;
    private final boolean upperInclusive;

    public Range(T lower, T upper) {
        this(lower, upper, true, true);
    }

    public Range(T lower, T upper, boolean lowerInclusive, boolean upperInclusive) {
        if (lower == null || upper == null) {
            throw new IllegalArgumentException("Bounds cannot be null");
        }
        if (lower.compareTo(upper) > 0) {
            throw new IllegalArgumentException("Lower bound cannot be greater than upper bound");
        }
        this.lower = lower;
        this.upper = upper;
        this.lowerInclusive = lowerInclusive;
        this.upperInclusive = upperInclusive;
    }

    @Override
    public int compareTo(Range<T> other) {
        // Сначала сравниваем по нижней границе
        int lowerCompare = this.lower.compareTo(other.lower);
        if (lowerCompare != 0) {
            return lowerCompare;
        }
        // Если нижние границы равны, сравниваем по верхней
        return this.upper.compareTo(other.upper);
    }

    public boolean contains(T value) {
        if (value == null) return false;

        int lowerCompare = value.compareTo(lower);
        int upperCompare = value.compareTo(upper);

        boolean lowerOk = lowerInclusive ? lowerCompare >= 0 : lowerCompare > 0;
        boolean upperOk = upperInclusive ? upperCompare <= 0 : upperCompare < 0;

        return lowerOk && upperOk;
    }

    public boolean intersects(Range<T> other) {
        if (other == null) return false;

        return this.contains(other.lower) ||
                this.contains(other.upper) ||
                other.contains(this.lower) ||
                other.contains(this.upper) ||
                (this.upper.compareTo(other.lower) == 0 && this.upperInclusive && other.lowerInclusive) ||
                (other.upper.compareTo(this.lower) == 0 && other.upperInclusive && this.lowerInclusive);
    }

    public boolean isConnected(Range<T> other) {
        return this.intersects(other) ||
                this.upper.compareTo(other.lower) == 0 ||
                other.upper.compareTo(this.lower) == 0;
    }

    public Range<T> span(Range<T> other) {
        if (other == null) return this;

        T newLower = this.lower.compareTo(other.lower) < 0 ? this.lower : other.lower;
        T newUpper = this.upper.compareTo(other.upper) > 0 ? this.upper : other.upper;

        return new Range<>(newLower, newUpper, true, true);
    }

    // Геттеры
    public T getLower() { return lower; }
    public T getUpper() { return upper; }
    public boolean isLowerInclusive() { return lowerInclusive; }
    public boolean isUpperInclusive() { return upperInclusive; }

    @Override
    public String toString() {
        return (lowerInclusive ? "[" : "(") + lower + ", " + upper + (upperInclusive ? "]" : ")");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Range<?> range = (Range<?>) obj;
        return lower.equals(range.lower) && upper.equals(range.upper) &&
                lowerInclusive == range.lowerInclusive && upperInclusive == range.upperInclusive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lower, upper, lowerInclusive, upperInclusive);
    }
}