package data_srtuctures;

/**
 * Created by litleleprikon on 01/11/15.
 * Array-based Min Binary Heap class
 */
public class Heap<T extends Comparable<T>> {

    /**
     * Exception that raises on getting minimum from empty heap
     */
    public static class HeapIsEmptyError extends Error {
        public HeapIsEmptyError() {
            super("Heap is empty");
        }
    }

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
     * Constructor to build heap from array
     * @param sourceArray
     */
    public Heap(T[] sourceArray)
    {
        this();
        data = sourceArray.clone();
        size = sourceArray.length;
        for (int i = size / 2; i >= 0; i--)
        {
            heapify(i);
        }
    }

    /**
     * Returns is heap empty
     * @return true if heap empty, else false
     */
    public boolean isEmpty() {
        return size == 0;
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
     * Private method to decrease array size if quantity of data heapify than array size higher than fourfold
     */
    private void deallocate() {
        Object[] temp = new Object[data.length/DEFAULT_INCREASE_FACTOR];
        System.arraycopy(data, 0, temp, 0, size);
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
        checkAndAllocate();
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

        while (i > 0 && ((T)data[parent]).compareTo((T)data[i]) > 0)
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
    private void heapify(int start) {
        int i = start;
        int leftChild;
        int rightChild;
        int largestChild;

        while(true) {
            leftChild = 2 * i + 1;
            rightChild = 2 * i + 2;
            largestChild = i;
            if(leftChild < size && ((T)data[leftChild]).compareTo((T)data[largestChild]) < 0) {
                largestChild = leftChild;
            }
            if (rightChild < size && ((T)data[rightChild]).compareTo((T)data[largestChild]) < 0)
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
     * Main method of Heap-getting minimal element
     * @return minimal element
     */
    public T getMin()
    {
        if(isEmpty()) {
            throw new HeapIsEmptyError();
        }
        T result = (T)data[0];
        data[0] = data[size - 1];
        data[size - 1] = null;
        size--;
        checkAndDealocate();
        heapify(0);
        return result;
    }
}
