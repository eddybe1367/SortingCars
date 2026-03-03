package SortingCars;

// QuicksortEngine.java

import java.util.*;

/*
 * QuicksortEngine is responsible for:
 *
 * 1) Maintaining a custom thread pool
 * 2) Maintaining a FIFO job queue
 * 3) Executing QuickSort in parallel
 * 4) Never exceeding maxThreads
 * 5) Gracefully terminating all threads
 *
 * IMPORTANT:
 * - ExecutorService is NOT used.
 * - Threads are manually managed.
 */
public class QuicksortEngine {

    private final int maxThreads;

    // List of worker threads (thread pool)
    private final LinkedList<Worker> threadPool = new LinkedList<>();

    // FIFO job queue
    private final LinkedList<SortJob> jobQueue = new LinkedList<>();

    // Indicates whether engine is shutting down
    private boolean isTerminated = false;

    /*
     * Constructor
     * Initially, thread pool is EMPTY.
     */
    public QuicksortEngine(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    /*
     * Called by main to start sorting a list.
     * It creates the first job (entire list).
     */
    public synchronized void sort(LinkedList<Car> list) {
        addJob(new SortJob(list, 0, list.size() - 1));
    }

    /*
     * Adds a new job to the queue (FIFO).
     * Creates new thread if needed and allowed.
     */
    private synchronized void addJob(SortJob job) {

        // Add job to end of queue
        jobQueue.addLast(job);

        // If we can create more threads, create one
        if (threadPool.size() < maxThreads) {
            Worker worker = new Worker();
            threadPool.add(worker);
            worker.start();
        }

        // Wake up waiting threads
        notifyAll();
    }

    /*
     * Worker threads call this method to get next job.
     * If no jobs exist, thread waits.
     */
    private synchronized SortJob getJob() throws InterruptedException {

        // Wait until:
        // - There is a job
        // - OR engine is terminated
        while (jobQueue.isEmpty() && !isTerminated) {
            wait();
        }

        if (isTerminated)
            return null;

        // FIFO: remove first job
        return jobQueue.removeFirst();
    }

    /*
     * Terminates engine.
     * Wakes up all waiting threads and stops them.
     */
    public synchronized void terminate() {
        isTerminated = true;
        notifyAll();
    }

    /*
     * Worker Thread Class
     * Each worker:
     * - Takes job
     * - Executes quicksort
     * - Returns to waiting state if no job
     */
    private class Worker extends Thread {

        @Override
        public void run() {

            while (true) {

                SortJob job;

                try {
                    job = getJob();
                } catch (InterruptedException e) {
                    break;
                }

                // If engine terminated, exit loop
                if (job == null)
                    break;

                // Execute job
                quicksort(job.list, job.low, job.high);
            }
        }
    }

    /*
     * QuickSort Algorithm (Multithreaded)
     */
    private void quicksort(LinkedList<Car> list, int low, int high) {

        if (low < high) {

            int pivotIndex = partition(list, low, high);

            /*
             * If partition is large enough,
             * create new jobs for parallel execution.
             *
             * Otherwise, do it sequentially
             * to avoid too many tiny jobs.
             */
            if (high - low > 1000) {

                addJob(new SortJob(list, low, pivotIndex - 1));
                addJob(new SortJob(list, pivotIndex + 1, high));

            } else {

                quicksort(list, low, pivotIndex - 1);
                quicksort(list, pivotIndex + 1, high);
            }
        }
    }

    /*
     * Partition logic of QuickSort
     */
    private int partition(LinkedList<Car> list, int low, int high) {

        Car pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {

            if (list.get(j).compareTo(pivot) <= 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }

        Collections.swap(list, i + 1, high);
        return i + 1;
    }
}