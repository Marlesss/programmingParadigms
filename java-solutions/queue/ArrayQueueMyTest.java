package queue;

import java.util.function.Predicate;

public class ArrayQueueMyTest {
    public static void main(String[] args) {
        Queue queue1 = new ArrayQueue();
        Queue queue2 = new LinkedQueue();
        for (int i = 0; i < 5; i++) {
            queue1.enqueue("e_" + i);
            queue2.enqueue("e_" + i);
        }
        queue1.dequeue();
        queue2.dequeue();
        for (int i = 3; i < 4; i++) {
            queue1.enqueue("e_" + i);
            queue2.enqueue("e_" + i);
        }
        Predicate<Object> equalsFirst = o -> o.equals("e_2");
        dump(queue1, "e_", equalsFirst);
        dump(queue2, "e_", equalsFirst);
    }

    private static void dump(Queue queue, String prefix, Predicate<Object> predicate) {
//        while (!queue.isEmpty()) {
//            for (int i = 0; i < 5; i++) {
//                System.out.println(i + " " +
//                        queue.indexOf(prefix + i) + " " +
//            }
//    }
        System.out.println(
                queue.indexIf(predicate) + " " +
                        queue.lastIndexIf(predicate)

        );
        System.out.println(queue.size() + " " + queue.dequeue());
    }
}
