package queue;

public abstract class AbstractQueue implements Queue {
    protected int size;
    // Model: a[1]..a[n]
    // Invariant: for i=1..n a[i] != null
    // Let immutable(n): for i=1..n: a'[i] == a[i]

    // Pred: element != null
    // Post: n' = n + 1 && a[n'] == element && immutable(n)
    public void enqueue(Object element) {
        assert element != null;
        if (size() == 0) {
            initImpl(element);
        } else {
            enqueueImpl(element);
        }
        size++;
    }

    protected abstract void initImpl(Object element);

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
        Object result = elementImpl();
        dequeueImpl();
        size--;
        return result;
    }

    protected abstract void dequeueImpl();

    // Pred: True
    // Post: n' == n && immutable(n) && R = n
    public int size() {
        return size;
    }

    // Pred: True
    // Post: n' = 0
    public void clear() {
        size = 0;
        clearImpl();
    }

    protected abstract void clearImpl();

    // Pred: True
    // Post: n' == n && immutable(n) && R = (n == 0)
    public boolean isEmpty() {
        return size() == 0;
    }

    // Pred: element != null
    // Post: n' == n && immutable(n) && (a[R] == element && R - min available || R == -1 && element not in a)
    public int indexOf(Object element) {
        assert element != null;
        if (size() == 0) {
            return -1;
        }
        int i = 0;
        Object current = getHead();
        while (current != null) {
            if (nodeEquals(current, element)) {
                return i;
            }
            current = getNext(current, i);
            i++;
        }
        return -1;
    }

    protected abstract boolean nodeEquals(Object node, Object element);

    protected abstract Object getHead();

    protected abstract Object getNext(Object current, int i);

    // Pred: element != null
    // Post: n' == n && immutable(n) && (a[R] == element && R - max available || R == -1 && element not in a)
    public int lastIndexOf(Object element) {
        assert element != null;
        if (size() == 0) {
            return -1;
        }
        int i = size() - 1;
        Object current = getTail();
        while (current != null) {
            if (nodeEquals(current, element)) {
                return i;
            }
            current = getPrev(current, i);
            i--;
        }
        return -1;
    }

    protected abstract Object getPrev(Object current, int i);

    protected abstract Object getTail();
}
