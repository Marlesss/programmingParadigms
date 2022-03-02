package queue;

public class ArrayQueueADTTest {
    public static void main(String[] args) {
        ArrayQueueADT queue1 = new ArrayQueueADT();
        ArrayQueueADT queue2 = new ArrayQueueADT();
        for (int i = 0; i < 5; i++) {
            ArrayQueueADT.enqueue(queue1, "e_1_" + i);
            ArrayQueueADT.enqueue(queue2, "e_2_" + i);
        }
        dump(queue1, "e_1_");
        dump(queue2, "e_2_");
    }

    private static void dump(ArrayQueueADT queue, String prefix) {
        while (!ArrayQueueADT.isEmpty(queue)) {
            for (int i = 0; i < 5; i++) {
                System.out.println(i + " " + ArrayQueueADT.indexOf(queue, prefix + i) + " " + ArrayQueueADT.lastIndexOf(queue, prefix + i));
            }
            System.out.println(ArrayQueueADT.size(queue) + " " + ArrayQueueADT.dequeue(queue));
        }
    }
}
