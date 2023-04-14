import java.util.concurrent.locks.*;

public class LazyList {

    private final Node head;
    int size;

    public LazyList() {
        head = new Node(Integer.MIN_VALUE, null);
        head.next = new Node(Integer.MAX_VALUE, null);
        size = 0;
    }

    private boolean validate(Node pred, Node curr) {
        return !pred.marked && !curr.marked && pred.next == curr;
    }

    public boolean add(Integer item) {
        int key = item.hashCode();
        while (true) {
            Node pred = head;
            Node curr = head.next;
            pred.lock();
            try {
                curr.lock();
                try {
                    if (validate(pred, curr)) {
                        if (curr.key == key) {
                            return false;
                        } 
                        else {
                            Node node = new Node(key, item);
                            size++;
                            node.next = curr;
                            pred.next = node;
                            return true;
                        }
                    }
                } finally {
                    curr.unlock();
                }
            } finally {
                pred.unlock();
            }
        }
    }

    public int remove() 
    {
        while (true) {
            Node pred = head;
            Node curr = head.next;
            if (curr.item == null) {
                return -1; // list is empty
            }
            pred.lock();
            try {
                curr.lock();
                try {
                    if (validate(pred, curr)) {
                            curr.marked = true;
                            pred.next = curr.next;
                            size--;
                            return curr.item;
                    }
                } 
                finally {
                    curr.unlock();
                }
            } 
            finally {
                pred.unlock();
            }
        }
    }

    public boolean contains(Integer item) {
        int key = item.hashCode();
        Node curr = head;
        while (curr.key < key)
            curr = curr.next;
        return curr.key == key && !curr.marked;
    }

    private class Node {
        final int key;
        final Integer item;
        volatile Node next;
        volatile boolean marked;
        private final ReentrantLock lock = new ReentrantLock();

        Node(int key, Integer item) {
            this.key = key;
            this.item = item;
            this.marked = false;
        }

        void lock() {
            lock.lock();
        }

        void unlock() {
            lock.unlock();
        }
    }
}
