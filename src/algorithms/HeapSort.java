package algorithms;

import data_srtuctures.Heap;

import java.util.Arrays;

/**
 * Created by litleleprikon on 30/11/15.
 * Heap Sort class
 */
public class HeapSort extends AbstractSort{

    @Override
    public <T extends Comparable<T>> void sort(T[] data) {
        Heap<T> heap = new Heap<>(data);
        for(int i = 0; i < data.length; i++) {
            data[i] = heap.getMin();
        }
    }

    public static void main(String[] args) {
        Integer[] data = new Integer[10];
        for(int i = 0; i < data.length; i++) {
            data[i] = data.length - i;
        }
        new HeapSort().sort(data);
        System.out.println(Arrays.toString(data));
    }
}
