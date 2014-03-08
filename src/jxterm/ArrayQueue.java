/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jxterm;

/**
 *
 * @author brain
 */
public class ArrayQueue<T> {

    int head = 0;
    int tail = 0;
    private int capacity;
    private T[] buffer;
    private int size;

    public ArrayQueue(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Negative queue capacity");
        }
        this.capacity = capacity;
        buffer = (T[]) new Object[this.capacity];
    }

    public boolean isEmpty() {
        return tail == head;
    }

    public boolean isFull() {
        return (tail + 1) % capacity == head;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSize() {
        return size;
    }

    public void add(T a) {
        if (size == capacity) {
            throw new IllegalStateException("queue full");
        }
        buffer[tail] = a;
        int newtail = (tail + 1) % capacity;
        size++;
        tail = newtail;
    }

    public T remove() {
        if (size == 0) {
            throw new IllegalStateException("remove on empty queue");
        }
        T removed = buffer[head];
        buffer[head] = null;
        size--;
        head = (head + 1) % capacity;
        return removed;
    }

    public T get(int index) {
        if (index < 0 || index >= getSize()) {
            throw new IndexOutOfBoundsException(index + "out of bounds");
        }
        int bindex = (head + index) % capacity;

        return buffer[bindex];
    }
}
