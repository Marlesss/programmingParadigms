package queue;

public class ArrayQueueModuleTest {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            ArrayQueueModule.enqueue("e" + i);
        }
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(ArrayQueueModule.size() + " " + ArrayQueueModule.dequeue());
        }
    }
}
