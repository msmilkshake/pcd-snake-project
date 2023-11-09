package util;

import java.util.ArrayList;
import java.util.List;

public class ThreadPool {
    private final int NUM_WORKERS;

    private BlockingQueue<Runnable> tasks = new BlockingQueue<>();
    private List<Thread> workers;

    private boolean isShutdown = false;

    public ThreadPool(int numWorkers) {
        NUM_WORKERS = numWorkers;
        workers = new ArrayList<>(numWorkers);
        initWorkers();
    }

    private void initWorkers() {
        for (int i = 0; i < NUM_WORKERS; ++i) {
            Thread worker = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            if (isShutdown && tasks.isEmpty()) {
                                break;
                            }
                            Runnable task = tasks.take();
                            task.run();
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException("Worker interrupted.");
                    }
                    System.out.println("Worker stopped.");
                }
            });
            workers.add(worker);
            worker.start();
        }
    }

    public void submit(Runnable t) throws InterruptedException {
        if (isShutdown) {
            throw new InterruptedException("ThreadPool was ordered to shutdown.");
        }
        tasks.put(t);
    }

    public void shutdown() {
        isShutdown = true;
    }

    public void shutdownNow() {
        shutdown();
        tasks.clear();
        for (Thread worker : workers) {
            worker.interrupt();
        }
    }
}
