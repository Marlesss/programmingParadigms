package queue;


import java.util.Arrays;

public class ArrayQueue {
    private Object[] elements = new Object[2];
    private int left, right;
    // Model: a[1]..a[n]
    // Invariant: for i=1..n a[i] != null
    // Let immutable(n): for i=1..n: a'[i] == a[i]

    // Pred: element != null
    // Post: n' = n + 1 && a[n'] == element && immutable(n)
    public void enqueue(Object element) {
        assert element != null;
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
            return;
        }
        if (right == left) {
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
    public Object element() {
        assert right != left;
        return elements[left];
    }

    // Pred: n > 0
    // Post: n' == n - 1 && R = a[1] && for i = 1..n - 1 a'[i] = a[i + 1]
    public Object dequeue() {
        assert right != left;
        Object val = elements[left];
        elements[left] = null;
        left = (left + 1) % elements.length;
        return val;
    }

    // Pred: True
    // Post: n' == n && immutable(n) && R = n
    public int size() {
        if (right >= left)
            return right - left;
        return elements.length - left + right;
    }

    // Pred: True
    // Post: n' == n && immutable(n) && R = (n == 0)
    public boolean isEmpty() {
        return right == left;
    }

    // Pred: True
    // Post: n' = 0
    public void clear() {
        elements = new Object[2];
        left = 0;
        right = 0;
    }

    // Pred: element != null
    // Post: n' == n && immutable(n) && (a[R] == element && R - min available || R == -1 && element not in a)
    public int indexOf(Object element) {
        assert element != null;
        if (left <= right) {
            for (int i = left; i < right; i++) {
                if (elements[i].equals(element)) {
                    return i - left;
                }
            }
        } else {
            for (int i = left; i < elements.length; i++) {
                if (elements[i].equals(element)) {
                    return i - left;
                }
            }
            for (int i = 0; i < right; i++) {
                if (elements[i].equals(element)) {
                    return i + elements.length - left;
                }
            }
        }
        return -1;
    }

    // Pred: element != null
    // Post: n' == n && immutable(n) && (a[R] == element && R - max available || R == -1 && element not in a)
    public int lastIndexOf(Object element) {
        assert element != null;
        if (left <= right) {
            for (int i = right - 1; i >= left; i--) {
                if (elements[i] != null && elements[i].equals(element)) {
                    return i - left;
                }
            }
        } else {
            for (int i = right; i >= 0; i--) {
                if (elements[i] != null && elements[i].equals(element)) {
                    return i + elements.length - left;
                }
            }
            for (int i = elements.length - 1; i >= left; i--) {
                if (elements[i] != null && elements[i].equals(element)) {
                    return i - left;
                }
            }
        }
        return -1;
    }
}
