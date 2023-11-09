package util;

import java.util.ArrayList;
import java.util.List;

public class ThreadPool {
    private final int NUM_WORKERS;
    
    private BlockingQueue<Runnable> tasks;
    private List<Thread> workers;
    
    private boolean isAlive = true;
    
    public ThreadPool(int numWorkers) {
        NUM_WORKERS = numWorkers;
        workers = new ArrayList<>(numWorkers);
        initWorkers();
    }

    private void initWorkers() {
        for (int i = 0; i < NUM_WORKERS; ++i) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            Runnable task = tasks.take();
                            task.run();
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            t.start();
            workers.add(t);
            
        }
    }
    
    public void submit(Thread t) throws InterruptedException {
        if (!isAlive) {
            throw new InterruptedException("Thread was ordered to shutdown.");
        }
        tasks.put(t);
    }
    
    public void shutdown() {
        isAlive = false;
    }
    
    public void shutdownNow() {
        shutdown();
        for (Thread w : workers) {
            w.interrupt();
        }
    }
}
