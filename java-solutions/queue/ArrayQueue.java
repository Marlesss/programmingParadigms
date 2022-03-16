package queue;


import java.util.Arrays;
import java.util.function.Predicate;

public class ArrayQueue extends AbstractQueue {
    private Object[] elements = new Object[2];
    private int left, right;
    // Model: a[1]..a[n]
    // Invariant: for i=1..n a[i] != null
    // Let immutable(n): for i=1..n: a'[i] == a[i]

    @Override
    protected void initImpl(Object element) {
        left = 0;
        right = 1;
        elements[0] = element;
    }

    // Pred: element != null
    // Post: n' = n + 1 && a[n'] == element && immutable(n)
    @Override
    protected void enqueueImpl(Object element) {
        elements[right++] = element;
        ensureCapacity();
    }

    private void ensureCapacity() {
        if (elements.length == right) {
            if (left > 0) {
                right = 0;
                return;
            }
            elements = Arrays.copyOf(elements, elements.length * 2);
        } else if (right == left) {
            Object[] buffer = new Object[elements.length * 2];
            System.arraycopy(elements, left, buffer, 0, elements.length - left);
            System.arraycopy(elements, 0, buffer, elements.length - left, right);
            right = right + elements.length - left;
            left = 0;
            elements = buffer;
        }
    }

    // Pred: n > 0
    // Post: n' == n && immutable(n) && R == a[1]
    @Override
    protected Object elementImpl() {
        return elements[left];
    }

    // Pred: n > 0
    // Post: n' == n - 1 && R = a[1] && for i = 1..n - 1 a'[i] = a[i + 1]
    @Override
    protected void dequeueImpl() {
        elements[left] = null;
        left = (left + 1) % elements.length;
    }

    // Pred: True
    // Post: n' = 0
    @Override
    protected void clearImpl() {
        elements = new Object[2];
        left = 0;
        right = 0;
    }
}
