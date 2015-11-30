package data_srtuctures;

/**
 * Created by litleleprikon on 01/11/15.
 * Array-based Max Binary Heap class
 */
public class Heap<T extends Comparable<T>> {
    /**
     * Private array to store heap elements
     */
    private Object[] data;

    /**
     * Variable to store current quantity of elements in heap
     */
    private int size;

    /**
     * Constant default heap storage size
     * Using if size not defined at Heap creation
     */
    private static final int DEFAULT_INITIAL_SIZE = 8;

    /**
     *
     */
    private static final int DEFAULT_INCREASE_FACTOR = 2;

    /**
     * Minimal capacity that allowed in this heap
     */
    private static final int MINIMAL_CAPACITY = 8;

    /**
     * New heap constructor
     * @param initSize size of initiated array of elements
     */
    public Heap(int initSize) {
        size = 0;
        data = new Object[initSize];
    }

    /**
     * Constructor with default size of elements warehouse
     */
    public Heap() {
        this(DEFAULT_INITIAL_SIZE);
    }

    /**
     * Increases the size of array, if needed
     */
    private void checkAndAllocate() {
        if(size == data.length) {
            allocate();
        }
    }

    /**
     * Decreases the size of array, if needed
     */
    private void checkAndDealocate() {
        if(size < data.length / 4 && data.length > MINIMAL_CAPACITY) {
            deallocate();
        }
    }

    /**
     * Private method to increase array size if array is full
     */
    private void allocate() {
        Object[] temp = new Object[data.length*DEFAULT_INCREASE_FACTOR];
        System.arraycopy(data, 0, temp, 0, size);
        data = temp;
    }

    /**
     * Private method to decrease array size if quantity of data lowerElement than array size higher than fourfold
     */
    private void deallocate() {
        Object[] temp = new Object[data.length/DEFAULT_INCREASE_FACTOR];
        System.arraycopy(data, 0, temp, 0, data.length);
        data = temp;
    }

    /**
     * Insertion method for Heap
     * Firstly inserts element at end of heap
     * After this rises value until heap is not heaped
     * @param value element to add
     */
    public void add(T value)
    {
        data[size++] = value;
        raiseElement(size);
    }

    /**
     * Method to raise element
     * @param start index of element to raise
     */
    private void raiseElement(int start) {
        int i = start - 1;
        int parent = (i - 1) / 2;

        while (i > 0 && ((T)data[parent]).compareTo((T)data[i]) < 0)
        {
            Object temp = data[i];
            data[i] = data[parent];
            data[parent] = temp;

            i = parent;
            parent = (i - 1) / 2;
        }
    }

    /**
     * Method to rebuild heap with main heap property
     * @param start element from which start
     */
    private void lowerElement(int start) {
        int i = start;
        int leftChild;
        int rightChild;
        int largestChild;

        while(true) {
            leftChild = 2 * i + 1;
            rightChild = 2 * i + 2;
            largestChild = i;
            if(leftChild < size && ((T)data[leftChild]).compareTo((T)data[largestChild]) > 0) {
                largestChild = leftChild;
            }
            if (rightChild < size && ((T)data[rightChild]).compareTo((T)data[largestChild]) > 0)
            {
                largestChild = rightChild;
            }

            if (largestChild == i)
            {
                break;
            }

            Object temp = data[i];
            data[i] = data[largestChild];
            data[largestChild] = temp;
            i = largestChild;
        }
    }

    /**
     * Main method of Heap-getting maximal element
     * @return maximal element
     */
    public T getMax()
    {
        T result = (T)data[0];
        data[0] = data[size - 1];
        data[size - 1] = null;
        size--;
        lowerElement(0);
        return result;
    }

    public static void main(String[] args) {
        Heap<Integer> heap = new Heap();
        heap.add(1);
        heap.add(2);
        heap.add(3);
        heap.add(4);
        heap.add(5);
        System.out.println(heap.getMax());
        System.out.println(heap.getMax());
        System.out.println(heap.getMax());
        System.out.println(heap.getMax());
    }
}
