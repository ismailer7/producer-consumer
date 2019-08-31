import java.util.*;

public class ProducerConsumerExample {


    public static void main(String[] args) {
        
        Queue<Integer> buffer = new LinkedList<>();
        int maxSize = 10;

        Thread producer = new Producer(buffer, maxSize, "PRODUCER");
        Thread consumer = new Consumer(buffer, maxSize, "CONSUMER");

        producer.start();
        consumer.start();

        // we can use Runnable interface to create threads.

        
        Thread th = new Thread(new Runnable() { // annonymous class
            @Override
            public void run() {
                System.out.println("Thread running..");
            }
        }); 

        th.start();
    }


}

class Producer extends Thread {
    private Queue<Integer> queue;
    private int maxSize;

    public Producer(Queue<Integer> queue, int maxSize, String name) {
        super(name);
        this.queue = queue;
        this.maxSize = maxSize;
    }

    @Override
    public void run() {
        System.out.println("producer started");
        while(true) {
            synchronized (queue) {
                while(!queue.isEmpty()) {
                    try {
                        System.out.println("Queue has something inside Consumer thread can consume it..");
                        queue.wait();
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Random random = new Random();
                int i = random.nextInt();
                System.out.println("Producing value : " + i);
                queue.add(i);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {}
                queue.notify();
            }
        }
    }
}


class Consumer extends Thread {
    private Queue<Integer> queue;
    private int maxSize;

    public Consumer(Queue<Integer> queue, int maxSize, String name) {
        super(name);
        this.queue = queue;
        this.maxSize = maxSize;
    }

    @Override
    public void run() {
        System.out.println("consumer started..");
        while(true) {
            synchronized (queue) {
                while(queue.isEmpty()) {
                    System.out.println("Queue is empty, consumer thread is waiting for producer to put something..");
                    try {
                        queue.wait();
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Consume value : " + queue.remove());
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {}
                
                queue.notify();
            }
        }
    }
}