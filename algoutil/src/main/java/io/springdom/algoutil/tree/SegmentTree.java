package io.springdom.algoutil.tree;

import java.util.Arrays;

@SuppressWarnings("unused")
public class SegmentTree {

    private final int[] input;
    private final Integer[] tree;
    private final int initialSize;

    private static final double LOG_10_2 = Math.log10(2);

    private SegmentTree(int[] arr) {
        input = arr;
        initialSize = arr.length;
        // For array of length N, Tree Size = ( 2 x 2 ^ (CEIL[log base 2 (N)]) ) - 1
        int treeSize = (int) (2 * Math.pow(2, Math.ceil(Math.log10(initialSize) / LOG_10_2)) - 1);
        tree = new Integer[treeSize];
        construct(arr, 0, initialSize - 1, 0);
    }

    public static SegmentTree of(int... arr) {
        return new SegmentTree(arr);
    }

    private int construct(int[] arr, int start, int end, int index) {
        if (start == end) {
            tree[index] = arr[start]; // Leaf node.
            return tree[index];
        }
        int mid = (end - start) / 2 + start;
        tree[index] =
                // Recursive call to find left sub tree sum.
                construct(arr, start, mid, 2 * index + 1) +
                        // Recursive call to find right sub tree sum.
                        construct(arr, mid + 1, end, 2 * index + 2);
        return tree[index];
    }

    public int rangeSum(int startIndex, int endIndex) {
        return rangeSum(startIndex, endIndex, 0, initialSize - 1, 0);
    }

    private int rangeSum(int queryStart, int queryEnd, int start, int end, int index) {
        if (end < queryStart || start > queryEnd)
            return 0; // Completely outside the range.
        if (queryStart <= start && queryEnd >= end)
            return tree[index]; // Completely inside the range.
        int mid = (end - start) / 2 + start;
        return
                // Recursive call to find queried sum on left sub tree.
                rangeSum(queryStart, queryEnd, start, mid, 2 * index + 1) +
                        // Recursive call to find queried sum on right sub tree.
                        rangeSum(queryStart, queryEnd, mid + 1, end, 2 * index + 2);
    }

    public void updateIndex(int index, int value) {
        if (index < 0 || index >= input.length)
            return;
        int difference = value - input[index];
        input[index] += difference;
        updateIndex(0, initialSize - 1, index, 0, difference);
    }

    private void updateIndex(int start, int end, int indexToBeUpdated, int index, int difference) {
        if (start > indexToBeUpdated || end < indexToBeUpdated)
            return; // Completely outside the range of update.
        tree[index] += difference;  // Add the difference to the current number.
        if (end > start) {  // When start == end is crossed, we are going out of bounds. So this must happens until that position.
            int mid = (end - start) / 2 + start;
            // Recursive update to left subtree.
            updateIndex(start, mid, indexToBeUpdated, 2 * index + 1, difference);
            // Recursive update to right subtree.
            updateIndex(mid + 1, end, indexToBeUpdated, 2 * index + 2, difference);
        }
    }

    @Override
    public String toString() {
        return "SegmentTree{" +
                "input=" + Arrays.toString(input) +
                ", tree=" + Arrays.toString(tree) +
                '}';
    }
}
