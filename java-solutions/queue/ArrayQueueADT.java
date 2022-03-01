package queue;


import java.util.Arrays;

public class ArrayQueueADT {
    public Object[] elements = new Object[2];
    public int left, right;
    // Model: a[1]..a[n]
    // Invariant: for i=1..n a[i] != null
    // Let immutable(l, r): for i=l..r: a'[i] == a[i]

    // Pred: element != null
    // Post: n' = n + 1 && a[n'] == element && immutable(1, n)
    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert element != null;
        ensureCapacity(queue, queue.right + 1);
        queue.elements[queue.right++] = element;
    }

    private static void ensureCapacity(ArrayQueueADT queue, int size) {
        if (queue.elements.length < size) {
            queue.elements = Arrays.copyOf(queue.elements, size * 2);
        }
    }

    // Pred: n > 0
    // Post: n' == n && immutable(1, n) && R = a[1]
    public static Object element(ArrayQueueADT queue) {
        assert queue.right - queue.left > 0;
        return queue.elements[queue.left];
    }

    // Pred: n > 0
    // Post: n' == n - 1 && immutable(2, n) && R = a[1] && a'[1] = null
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.right - queue.left > 0;
        Object val = queue.elements[queue.left];
        queue.elements[queue.left] = null;
        queue.left++;
        return val;
    }

    // Pred: True
    // Post: n' == n && immutable(1, n) && R = n
    public static int size(ArrayQueueADT queue) {
        return queue.right - queue.left;
    }

    // Pred: True
    // Post: n' == n && immutable(1, n) && R = (n == 0)
    public static boolean isEmpty(ArrayQueueADT queue) {
        return (queue.right - queue.left) == 0;
    }

    // Pred: True
    // Post: n' = 0 && for i=1..n a'[i] = null
    public static void clear(ArrayQueueADT queue) {
        queue.elements = new Object[2];
        queue.left = 0;
        queue.right = 0;
    }
}
