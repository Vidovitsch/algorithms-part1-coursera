import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> head;
    private Node<Item> tail;
    private int size;

    public Deque() {
        head = null;
        tail = null;
        size = 0;
    }

    private class Node<Item> {
        private final Item value;
        private Node<Item> next;
        private Node<Item> previous;
        public Node(Item value) {
            this.value = value;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> newNode = new Node<Item>(value);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            Node<Item> oldHead = head;
            head = newNode;
            head.next = oldHead;
            oldHead.previous = head;
        }
        size++;
    }

    public void addLast(Item value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> newNode = new Node<Item>(value);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            Node<Item> oldTail = tail;
            tail = newNode;
            tail.previous = oldTail;
            oldTail.next = tail;
        }
        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item value = head.value;
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.previous = null;
        }
        size--;
        return value;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item value = tail.value;
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            tail = tail.previous;
            tail.next = null;
        }
        size--;
        return value;
    }

    public Iterator<Item> iterator() {
        return new QueueIterator<Item>(head);
    }

    private class QueueIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public QueueIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.value;
            current = current.next;
            return item;
        }
    }
}