package queue;

import java.util.Arrays;
import java.util.function.Predicate;

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

    private Node back;
    private Node front;

    // Model: a[1]..a[n]
    // Invariant: for i=1..n a[i] != null
    // Let immutable(n): for i=1..n: a'[i] == a[i]

    // Pred: element != null
    // Post: n' = n + 1 && a[n'] == element && immutable(n)
    @Override
    protected void initImpl(Object element) {
        back = new Node(element, null);
        front = back;
    }

    @Override
    protected void enqueueImpl(Object element) {
        Node newBack = new Node(element, back);
        back.next = newBack;
        back = newBack;
    }

    // Pred: n > 0
    // Post: n' == n && immutable(n) && R == a[1]
    @Override
    protected Object elementImpl() {
        return front.element;
    }

    // Pred: n > 0
    // Post: n' == n - 1 && R = a[1] && for i = 1..n - 1 a'[i] = a[i + 1]
    @Override
    protected void dequeueImpl() {
        front = front.next;
        if (front != null)
            front.prev = null;
    }

    // Pred: True
    // Post: n' = 0
    @Override
    protected void clearImpl() {
        back = null;
        front = null;
    }
}
