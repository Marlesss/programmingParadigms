package queue;

public abstract class AbstractQueue implements Queue {
    // Model: a[1]..a[n]
    // Invariant: for i=1..n a[i] != null
    // Let immutable(n): for i=1..n: a'[i] == a[i]

    // Pred: element != null
    // Post: n' = n + 1 && a[n'] == element && immutable(n)
    public void enqueue(Object element) {
        assert element != null;
        enqueueImpl(element);
    }

    protected abstract void enqueueImpl(Object element);

    // Pred: n > 0
    // Post: n' == n && immutable(n) && R == a[1]
    public Object element() {
        assert size() != 0;
        return elementImpl();
    }

    protected abstract Object elementImpl();

    // Pred: n > 0
    // Post: n' == n - 1 && R = a[1] && for i = 1..n - 1 a'[i] = a[i + 1]
    public Object dequeue() {
        assert size() != 0;
        return dequeueImpl();
    }

    protected abstract Object dequeueImpl();

    // Pred: True
    // Post: n' == n && immutable(n) && R = (n == 0)
    public boolean isEmpty() {
        return size() == 0;
    }

    // Pred: element != null
    // Post: n' == n && immutable(n) && (a[R] == element && R - min available || R == -1 && element not in a)
    public int indexOf(Object element) {
        assert element != null;
        return indexOfImpl(element);
    }

    protected abstract int indexOfImpl(Object element);

    // Pred: element != null
    // Post: n' == n && immutable(n) && (a[R] == element && R - max available || R == -1 && element not in a)
    public int lastIndexOf(Object element) {
        assert element != null;
        return lastIndexOfImpl(element);
    }

    protected abstract int lastIndexOfImpl(Object element);
}
