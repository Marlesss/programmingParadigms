package queue;

public class LinkedQueueMyTest {
    public static void main(String[] args) {
        Queue queue1 = new ArrayQueue();
        Queue queue2 = new LinkedQueue();
        for (int i = 0; i < 5; i++) {
            queue1.enqueue("e_1_" + i);
            queue2.enqueue("e_2_" + i);
        }
        dump(queue1, "e_1_");
        dump(queue2, "e_2_");
    }

    private static void dump(Queue queue, String prefix) {
        while (!queue.isEmpty()) {
            for (int i = 0; i < 5; i++) {
                System.out.println(i + " " +
                        queue.indexOf(prefix + i) + " " +
                        queue.lastIndexOf(prefix + i));
            }
            System.out.println(queue.size() + " " + queue.dequeue());
        }
    }
}
