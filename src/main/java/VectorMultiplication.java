import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class VectorMultiplication {
    private static final Logger logger = LoggerFactory.getLogger(VectorMultiplication.class);

    private volatile Integer result = 0; // 12

    public static void main(String[] args) {
        int[] aVector = {5, 2, 1};
        int[] bVector = {1, 2, 3};

        (new VectorMultiplication()).multiply(aVector, bVector);

    }

    int multiply(int[] aVector, int[] bVector) {
        int aLength = aVector.length;
        int bLength = bVector.length;
        int num;

        if (!(aLength == bLength)) throw new IllegalArgumentException("These vectors can not be multiplied!");
        else num = aLength;

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            MultiplicationThread thread = new MultiplicationThread(i, aVector[i], bVector[i]);
            threads.add(thread);
            thread.start();
        }
        threads.forEach(t -> {
            try {
                t.join(10000);
                logger.info("{} joined", t.getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        logger.info("result = {}", result);
        return result;
    }

    class MultiplicationThread extends Thread {
        int index;
        int a;
        int b;


        MultiplicationThread(int index, int a, int b) {
            super("Thread " + index);
            this.index = index;
            this.a = a;
            this.b = b;
            logger.info("New thread: {}", "Thread " + index);
        }

        @Override
        public void run() {
            int m = a * b;
            synchronized (result) {
                result += m;
            }
            logger.info("result = {}", result);
        }
    }
}
