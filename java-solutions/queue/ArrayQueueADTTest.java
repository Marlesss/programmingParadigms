package queue;

public class ArrayQueueADTTest {
    public static void main(String[] args) {
        ArrayQueueADT queue1 = new ArrayQueueADT();
        ArrayQueueADT queue2 = new ArrayQueueADT();
        for (int i = 0; i < 5; i++) {
            ArrayQueueADT.enqueue(queue1, "e_1_" + i);
            ArrayQueueADT.enqueue(queue2, "e_2_" + i);
        }
        dump(queue1);
        dump(queue2);
    }

    private static void dump(ArrayQueueADT queue) {
        while (!ArrayQueueADT.isEmpty(queue)) {
            System.out.println(ArrayQueueADT.size(queue) + " " + ArrayQueueADT.dequeue(queue));
        }
    }
}
