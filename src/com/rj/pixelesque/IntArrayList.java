package com.rj.pixelesque;

import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

public class IntArrayList extends AbstractList<Integer>
implements List<Integer> , RandomAccess /* todo , Cloneable, java.io.Serializable */{

    private static final int INT_SIZE_MINUS_ONE = 15;
    private static final int RIGHT_SHIFT = 4;

    private int size;
    private int isNull[];
    private int data[];

    IntArrayList(int size) {
        if (size < 0) {
            throw new RuntimeException("invalid size");
        }
        this.size = size;
        isNull = new int[(size + INT_SIZE_MINUS_ONE) >>> RIGHT_SHIFT];
        data = new int[size];
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    public Integer set(int index, Integer element) {
        rangeCheck(index);

        Integer oldValue = get(index);
        if (element == null) {
            isNull[index >>> RIGHT_SHIFT] &= ~(1 << (index & INT_SIZE_MINUS_ONE));
        } else {
            isNull[index >>> RIGHT_SHIFT] |= (1 << (index & INT_SIZE_MINUS_ONE));
            data[index] = element;
        }
        return oldValue;
    }

    @Override
    public Integer get(int index) {
        rangeCheck(index);
        if ((isNull[index >>> RIGHT_SHIFT] & (1 << (index & INT_SIZE_MINUS_ONE))) == 0) {
            return null;
        }
        return new Integer(data[index]);
    }
    
    public int get() {
    	return data[size-1];
    }

    @Override
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
