/*
 * Decompiled with CFR 0.139.
 */
package eu.craftok.buildtionnary.utils;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator implements Comparator<Object> {
    private Map<Object, Integer> base;

    public ValueComparator(Map<Object, Integer> base) {
        this.base = base;
    }

    @Override
    public int compare(Object o1, Object o2) {
        if (this.base.get(o1) >= this.base.get(o2)) {
            return -1;
        }
        return 1;
    }
}

