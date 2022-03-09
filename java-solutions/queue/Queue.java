package queue;

import java.util.Arrays;
import java.util.function.Predicate;

public interface Queue {
    // Model: a[1]..a[n]
    // Invariant: for i=1..n a[i] != null
    // Let immutable(n): for i=1..n: a'[i] == a[i]

    // Pred: element != null
    // Post: n' = n + 1 && a[n'] == element && immutable(n)
    void enqueue(Object element);

    // Pred: n > 0
    // Post: n' == n && immutable(n) && R == a[1]
    Object element();

    // Pred: n > 0
    // Post: n' == n - 1 && R = a[1] && for i = 1..n - 1 a'[i] = a[i + 1]
    Object dequeue();

    // Pred: True
    // Post: n' == n && immutable(n) && R = n
    int size();

    // Pred: True
    // Post: n' == n && immutable(n) && R = (n == 0)
    boolean isEmpty();

    // Pred: True
    // Post: n' = 0
    void clear();

    // Pred: element != null
    // Post: n' == n && immutable(n) && (a[R] == element && R - min available || R == -1 && element not in a)
    int indexOf(Object element);

    // Pred: element != null
    // Post: n' == n && immutable(n) && (a[R] == element && R - max available || R == -1 && element not in a)
    int lastIndexOf(Object element);

    int indexIf(Predicate<Object> predicate);

    int lastIndexIf(Predicate<Object> predicate);
}
