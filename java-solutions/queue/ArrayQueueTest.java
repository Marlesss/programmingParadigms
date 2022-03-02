package queue;

public class ArrayQueueTest {
    public static void main(String[] args) {
        ArrayQueue queue1 = new ArrayQueue();
        ArrayQueue queue2 = new ArrayQueue();
        for (int i = 0; i < 5; i++) {
            queue1.enqueue("e_1_" + i);
            queue2.enqueue("e_2_" + i);
        }
        dump(queue1, "e_1_");
        dump(queue2, "e_2_");
    }

    private static void dump(ArrayQueue queue, String prefix) {
        while (!queue.isEmpty()) {
            for (int i = 0; i < 5; i++) {
                System.out.println(i + " " + queue.indexOf(prefix + i) + " " + queue.lastIndexOf(prefix + i));
            }
            System.out.println(queue.size() + " " + queue.dequeue());
        }
    }
}
