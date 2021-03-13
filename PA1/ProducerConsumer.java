import java.util.Arrays;

class SyncedArray {
    private final int[] arr;
    private int size;

    public SyncedArray(int[] arr, int size) {
        this.arr = arr;
        this.size = size;
    }

    public int[] getArray() {
        return arr;
    }

    public int getSize() {
        return size;
    }

    public int addElement() {
        arr[size] = size;
        size += 1;
        return size;
    }

    public int removeElement() {
        size -= 1;
        arr[size] = -1;
        return size;
    }
}

class ProducerThread implements Runnable {
    private final SyncedArray arr;
    private int productions;

    public ProducerThread(SyncedArray arr, int productions) {
        this.arr = arr;
        this.productions = productions;
    }

    public void run() {
        synchronized(arr) {
            boolean flag = true;
            while (flag) {
                if (arr.getSize() == arr.getArray().length) {
                    try {
                        System.out.println("Array is FULL, Producer is waiting");
                        arr.wait();
                        System.out.println("Producer has stopped waiting");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Contents of array before production: " + Arrays.toString(arr.getArray()));
                while (productions > 0) {
                    if (arr.getSize() == arr.getArray().length) {
                        System.out.println("Production has stopped, array is FULL");
                        break;
                    }
                    System.out.println("Produced: " + arr.addElement());
                    productions--;
                }
                if (productions == 0) {
                    System.out.println("Production has stopped, no productions remaining");
                    flag = false;
                }
                System.out.println("Contents of array after production: " + Arrays.toString(arr.getArray()));
                arr.notify();
            }
        }
    }
}

class ConsumerThread implements Runnable {
    private final SyncedArray arr;
    private int consumptions;

    public ConsumerThread(SyncedArray arr, int consumptions) {
        this.arr = arr;
        this.consumptions = consumptions;
    }

    public void run() {
        synchronized(arr) {
            boolean flag = true;
            while (flag) {
                if (arr.getSize() == 0) {
                    try {
                        System.out.println("Array is EMPTY, Consumer is waiting");
                        arr.wait();
                        System.out.println("Consumer has stopped waiting");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Contents of array before consumption: " + Arrays.toString(arr.getArray()));
                while (consumptions > 0) {
                    if (arr.getSize() == 0) {
                        System.out.println("Consumption has stopped, array is EMPTY");
                        break;
                    }
                    System.out.println("Consumed: " + arr.removeElement());
                    consumptions--;
                }
                if (consumptions == 0) {
                    System.out.println("Consumption has stopped, no consumptions remaining");
                    flag = false;
                }
                System.out.println("Contents of array after consumption: " + Arrays.toString(arr.getArray()));
                arr.notify();
            }
        }

    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        //the array here will be of length 5
        int[] arr = new int[5];
        Arrays.fill(arr, -1);
        SyncedArray synced = new SyncedArray(arr, 0);

        System.out.println();
        System.out.println("An element of the value '-1' represents an empty element.");
        System.out.println("Any other non-negative integer represents a full element.");
        System.out.println();

        //The number of productions and consumptions are length 5
        Thread producerThread = new Thread(new ProducerThread(synced, 5));
        Thread consumerThread = new Thread(new ConsumerThread(synced, 5));

        producerThread.start();
        consumerThread.start();
        try {
            producerThread.join();
            consumerThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
