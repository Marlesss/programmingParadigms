package queue;

import java.util.function.Predicate;

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
        return indexIf(equalsPredicate(element));
    }

    private Predicate<Object> equalsPredicate(Object element) {
        return o -> o.equals(element);
    }

    // Pred: element != null
    // Post: n' == n && immutable(n) && (a[R] == element && R - max available || R == -1 && element not in a)
    public int lastIndexOf(Object element) {
        assert element != null;
        return lastIndexIf(equalsPredicate(element));
    }


    public int indexIf(Predicate<Object> predicate) {
        return searchByPredicate(true, predicate);
    }

    public int lastIndexIf(Predicate<Object> predicate) {
        return searchByPredicate(false, predicate);
    }

    private int searchByPredicate(boolean first, Predicate<Object> predicate) {
        if (size() == 0) {
            return -1;
        }
        int ans = -1;
        for (int i = 0; i < size; i++) {
            if (predicate.test(element())) {
                if (!first || ans == -1) {
                    ans = i;
                }
            }
            enqueue(dequeue());
        }
        return ans;
    }
}
