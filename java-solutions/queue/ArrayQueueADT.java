package queue;


import java.util.Arrays;

public class ArrayQueueADT {
    private Object[] elements = new Object[2];
    private int left, right;
    // Model: a[1]..a[n]
    // Invariant: for i=1..n a[i] != null
    // Let immutable(n): for i=1..n: a'[i] == a[i]

    // Pred: element != null
    // Post: n' = n + 1 && a[n'] == element && immutable(n)
    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert element != null;
        queue.elements[queue.right++] = element;
        ensureCapacity(queue);
    }

    private static void ensureCapacity(ArrayQueueADT queue) {
        if (queue.elements.length == queue.right) {
            if (queue.left > 0) {
                queue.right = 0;
                return;
            }
            queue.elements = Arrays.copyOf(queue.elements, queue.elements.length * 2);
            return;
        }
        if (queue.right == queue.left) {
            Object buffer[] = new Object[queue.elements.length * 2];
            System.arraycopy(queue.elements, queue.left, buffer, 0, queue.elements.length - queue.left);
            System.arraycopy(queue.elements, 0, buffer, queue.elements.length - queue.left, queue.right);
            queue.right = queue.right + queue.elements.length - queue.left;
            queue.left = 0;
            queue.elements = buffer;
            return;
        }
    }

    // Pred: n > 0
    // Post: n' == n && immutable(n) && R == a[1]
    public static Object element(ArrayQueueADT queue) {
        assert queue.right != queue.left;
        return queue.elements[queue.left];
    }

    // Pred: n > 0
    // Post: n' == n - 1 && R = a[1] && for i = 1..n - 1 a'[i] = a[i + 1]
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.right != queue.left;
        Object val = queue.elements[queue.left];
        queue.elements[queue.left] = null;
        queue.left = (queue.left + 1) % queue.elements.length;
        return val;
    }

    // Pred: True
    // Post: n' == n && immutable(n) && R = n
    public static int size(ArrayQueueADT queue) {
        if (queue.right >= queue.left)
            return queue.right - queue.left;
        return queue.elements.length - queue.left + queue.right;
    }

    // Pred: True
    // Post: n' == n && immutable(n) && R = (n == 0)
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.right == queue.left;
    }

    // Pred: True
    // Post: n' = 0
    public static void clear(ArrayQueueADT queue) {
        queue.elements = new Object[2];
        queue.left = 0;
        queue.right = 0;
    }

    // Pred: element != null
    // Post: n' == n && immutable(n) && (a[R] == element && R - min available || R == -1 && element not in a)
    public static int indexOf(ArrayQueueADT queue, Object element) {
        assert element != null;
        if (queue.left <= queue.right) {
            for (int i = queue.left; i < queue.right; i++) {
                if (queue.elements[i].equals(element)) {
                    return i - queue.left;
                }
            }
        } else {
            for (int i = queue.left; i < queue.elements.length; i++) {
                if (queue.elements[i].equals(element)) {
                    return i - queue.left;
                }
            }
            for (int i = 0; i < queue.right; i++) {
                if (queue.elements[i].equals(element)) {
                    return i + queue.elements.length - queue.left;
                }
            }
        }
        return -1;
    }

    // Pred: element != null
    // Post: n' == n && immutable(n) && (a[R] == element && R - max available || R == -1 && element not in a)
    public static int lastIndexOf(ArrayQueueADT queue, Object element) {
        assert element != null;
        if (queue.left <= queue.right) {
            for (int i = queue.right - 1; i >= queue.left; i--) {
                if (queue.elements[i] != null && queue.elements[i].equals(element)) {
                    return i - queue.left;
                }
            }
        } else {
            for (int i = queue.right; i >= 0; i--) {
                if (queue.elements[i] != null && queue.elements[i].equals(element)) {
                    return i + queue.elements.length - queue.left;
                }
            }
            for (int i = queue.elements.length - 1; i >= queue.left; i--) {
                if (queue.elements[i] != null && queue.elements[i].equals(element)) {
                    return i - queue.left;
                }
            }
        }
        return -1;
    }
}
