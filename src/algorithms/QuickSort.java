package algorithms;

import java.util.Random;

/**
 * Created by litleleprikon on 30/11/15.
 * Quick Sort class
 */
public class QuickSort extends AbstractSort {
    /**
     * Randomizer to choose pivot index
     */
    private static final Random RANDOM = new Random();

    /**
     * Method to change two elements in array
     * @param array whole array
     * @param i first changed element
     * @param j second changed element
     */
    private void swap(Object[] array, int i, int j) {
        Object tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    /**
     * Pick an element from the array.
     * @param array all array
     * @param begin index of first element of current sub array
     * @param end index of last element of current sub array
     * @param <T> type of elements in array, must implement Comparable
     * @return index of picked element
     */
    private  <T extends Comparable<T>> int partition(T[] array, int begin, int end) {
        int index = begin + RANDOM.nextInt(end - begin + 1);
        T pivot = array[index];
        swap(array, index, end);
        for (int i = index = begin; i < end; ++ i) {
            if (array[i].compareTo(pivot) <= 0) {
                swap(array, index++, i);
            }
        }
        swap(array, index, end);
        return (index);
    }

    /**
     * Recursive sorting method
     * @param array whole array
     * @param begin firs element of sub array
     * @param end last element of sub array
     * @param <T> type of elements in array
     */
    private  <T extends Comparable<T>> void qsort(T[] array, int begin, int end) {
        if (end > begin) {
            int index = partition(array, begin, end);
            qsort(array, begin, index - 1);
            qsort(array, index + 1,  end);
        }
    }

    @Override
    public <T extends Comparable<T>> void sort(T[] data) {
        qsort(data, 0, data.length - 1);
    }
}
