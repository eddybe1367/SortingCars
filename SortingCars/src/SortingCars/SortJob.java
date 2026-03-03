package SortingCars;

// SortJob.java

import java.util.LinkedList;

/*
 * SortJob represents one QuickSort task.
 *
 * A job may represent:
 * - Sorting an entire list
 * - Sorting a partition of the list
 *
 * QuickSort naturally splits work into partitions,
 * so each partition becomes a new job.
 */
public class SortJob {

    LinkedList<Car> list;  // List being sorted
    int low;               // Start index
    int high;              // End index

    public SortJob(LinkedList<Car> list, int low, int high) {
        this.list = list;
        this.low = low;
        this.high = high;
    }
}