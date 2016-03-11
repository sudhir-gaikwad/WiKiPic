package com.wikipic.util;

import com.wikipic.model.Pages;

import java.util.Comparator;

public class PageComparator implements Comparator<Pages> {
    @Override
    public int compare(Pages o1, Pages o2) {
        return Integer.valueOf(o1.getIndex()).compareTo(Integer.valueOf(o2.getIndex()));
    }
}
