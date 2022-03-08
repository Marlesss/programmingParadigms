package queue;

import java.util.Arrays;

public class LinkedQueue extends AbstractQueue {
    private static class Node {
        private final Object element;
        private Node next;
        private Node prev;

        private Node(Object element, Node prev) {
            this.element = element;
            this.prev = prev;
        }
    }

    private int size;
    private Node back;
    private Node front;

    // Model: a[1]..a[n]
    // Invariant: for i=1..n a[i] != null
    // Let immutable(n): for i=1..n: a'[i] == a[i]

    // Pred: element != null
    // Post: n' = n + 1 && a[n'] == element && immutable(n)
    protected void enqueueImpl(Object element) {
        if (size == 0) {
            back = new Node(element, back);
            front = back;
        } else {
            Node newBack = new Node(element, back);
            back.next = newBack;
            back = newBack;
        }
        size++;
    }

    // Pred: n > 0
    // Post: n' == n && immutable(n) && R == a[1]
    protected Object elementImpl() {
        return front.element;
    }

    // Pred: n > 0
    // Post: n' == n - 1 && R = a[1] && for i = 1..n - 1 a'[i] = a[i + 1]
    protected Object dequeueImpl() {
        Object result = front.element;
        front = front.next;
        if (front != null)
            front.prev = null;
        size--;
        return result;
    }

    // Pred: True
    // Post: n' == n && immutable(n) && R = n
    public int size() {
        return size;
    }

    // Pred: True
    // Post: n' = 0
    public void clear() {
        size = 0;
        back = null;
        front = null;
    }

    // Pred: element != null
    // Post: n' == n && immutable(n) && (a[R] == element && R - min available || R == -1 && element not in a)
    protected int indexOfImpl(Object element) {
        int i = 0;
        Node current = front;
        while (current != null) {
            if (current.element.equals(element)) {
                return i;
            }
            i++;
            current = current.next;
        }
        return -1;
    }

    // Pred: element != null
    // Post: n' == n && immutable(n) && (a[R] == element && R - max available || R == -1 && element not in a)
    protected int lastIndexOfImpl(Object element) {
        int i = size - 1;
        Node current = back;
        while (current != null) {
            if (current.element.equals(element)) {
                return i;
            }
            i--;
            current = current.prev;
        }
        return -1;
    }
}
