package queue;


import java.util.Arrays;

public class ArrayQueue {
    private Object[] elements = new Object[2];
    private int left, right;
    // Model: a[1]..a[n]
    // Invariant: for i=1..n a[i] != null
    // Let immutable(l, r): for i=l..r: a'[i] == a[i]

    // Pred: element != null
    // Post: n' = n + 1 && a[n'] == element && immutable(1, n)
    public void enqueue(Object element) {
        assert element != null;
        ensureCapacity(right + 1);
        elements[right++] = element;
    }

    private void ensureCapacity(int size) {
        if (elements.length < size) {
            elements = Arrays.copyOf(elements, size * 2);
        }
    }

    // Pred: n > 0
    // Post: n' == n && immutable(1, n) && R = a[1]
    public Object element() {
        assert right - left > 0;
        return elements[left];
    }

    // Pred: n > 0
    // Post: n' == n - 1 && immutable(2, n) && R = a[1] && a'[1] = null
    public Object dequeue() {
        assert right - left > 0;
        Object val = elements[left];
        elements[left] = null;
        left++;
        return val;
    }

    // Pred: True
    // Post: n' == n && immutable(1, n) && R = n
    public int size() {
        return right - left;
    }

    // Pred: True
    // Post: n' == n && immutable(1, n) && R = (n == 0)
    public boolean isEmpty() {
        return (right - left) == 0;
    }

    // Pred: True
    // Post: n' = 0 && for i=1..n a'[i] = null
    public void clear() {
        elements = new Object[2];
        left = 0;
        right = 0;
    }

    // Pred: element != null
    // Post: n' == n && immutable(1, n) && (a[R] == element && R - min available || R == -1 && element not in a)
    public int indexOf(Object element) {
        assert element != null;
        for (int i = left; i < right; i++) {
            if (elements[i] != null && elements[i].equals(element)) {
                return i - left;
            }
        }
        return -1;
    }

    // Pred: element != null
    // Post: n' == n && immutable(1, n) && (a[R] == element && R - max available || R == -1 && element not in a)
    public int lastIndexOf(Object element) {
        assert element != null;
        for (int i = right - 1; i >= left; i--) {
            if (elements[i] != null && elements[i].equals(element)) {
                return i - left;
            }
        }
        return -1;
    }
}
