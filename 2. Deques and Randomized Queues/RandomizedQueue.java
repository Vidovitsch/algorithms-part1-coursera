import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] values;
    private int size;

    public RandomizedQueue() {
        values = (Item[]) new Object[1];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == values.length) {
            resize(values.length * 2);
        }
        values[size] = item;
        size++;
    }

    public Item dequeue() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int rnd = StdRandom.uniform(size);
        Item value = values[rnd];
        if (rnd == size - 1) {
            values[rnd] = null;
        } else {
            values[rnd] = values[size - 1];
            values[size - 1] = null;
        }
        if (size > 0 && size == values.length / 4) {
            resize(values.length / 2);
        }
        size--;
        return value;
    }

    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int rnd = StdRandom.uniform(size);
        return values[rnd];

    }

    private void resize(int n) {
        Item[] copy = (Item[]) new Object[n];
        for (int i = 0; i < size; i++) {
            copy[i] = values[i];
        }
        values = copy;
    }

    public Iterator<Item> iterator() {
        return new RandomizedIterator();
    }

    private class RandomizedIterator implements Iterator<Item> {
        int n = size;

        public boolean hasNext() {
            return n != 0;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int rnd = StdRandom.uniform(n);
            Item value = values[rnd];
            if (rnd == n - 1) {
                n--;
                return value;
            }
            else {
                values[rnd] = values[n - 1];
                values[n - 1] = value;
                n--;
                return value;
            }

        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}