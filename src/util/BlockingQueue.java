package util;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue<E> {
    private Queue<E> queue = new LinkedList<>();
    private int capacity = 0;

    public BlockingQueue() {
    }

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(E element) throws InterruptedException {
        while (capacity > 0 && queue.size() >= capacity) {
            wait();
        }
        queue.add(element);
        notifyAll();
    }

    public synchronized E take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        E head = queue.poll();
        notifyAll();
        return head;
    }

    public synchronized int size() {
        return queue.size();
    }

    public synchronized void clear() {
        queue.clear();
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public synchronized String toString() {
        return queue.toString();
    }
}
