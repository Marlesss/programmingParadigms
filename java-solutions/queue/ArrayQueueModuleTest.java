package queue;

public class ArrayQueueModuleTest {
    public static void main(String[] args) {
//        for (int i = 0; i < 2; i++) {
//            ArrayQueueModule.enqueue("e" + i);
//        }
//        while (!ArrayQueueModule.isEmpty()) {
////            for (int i = 0; i < 5; i++) {
////                System.out.println(i + " " + ArrayQueueModule.indexOf("e" + i) + " " + ArrayQueueModule.lastIndexOf("e" + i));
////            }
//            System.out.println(ArrayQueueModule.size() + " " + ArrayQueueModule.dequeue());
//        }
//        System.out.println(ArrayQueueModule.size());
        ArrayQueueModule.enqueue("e" + 1);
        print();
        System.err.println("deq = " + ArrayQueueModule.dequeue());
        print();
        ArrayQueueModule.enqueue("e" + 2);
        print();
        System.err.println("deq = " + ArrayQueueModule.dequeue());
        print();
        ArrayQueueModule.enqueue("e" + 3);
        print();
        ArrayQueueModule.enqueue("e" + 4);
        print();
    }

    private static void print() {
        System.err.println("Size = " + ArrayQueueModule.size());
    }
}
