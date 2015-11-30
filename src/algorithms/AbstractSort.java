package algorithms;

/**
 * Created by litleleprikon on 30/11/15.
 * Abstract Sort class
 */
public abstract class AbstractSort {
    /**
     * Sort method
     * Gives array and sorts it
     * @param data array of data
     * @param <T> data type, must extends Comparable
     */
    public abstract <T extends Comparable<T>> void sort(T[] data);
}
