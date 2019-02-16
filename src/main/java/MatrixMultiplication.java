import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MatrixMultiplication {
    private static final Logger logger = LoggerFactory.getLogger(MatrixMultiplication.class);

    private volatile Integer result[][]; // {{5,1},{2,0},{1,1}}

    public static void main(String[] args) {
        int[][] aMatrix = {{5, 3}, {2, 1}, {1, 1}};
        int[][] bMatrix = {{1, -1}, {0, 2}};

        (new MatrixMultiplication()).multiply(aMatrix, bMatrix);

    }

    Integer[][] multiply(int[][] aMatrix, int[][] bMatrix) {
        int aLength = aMatrix[0].length;
        int bLength = bMatrix.length;

        if (!(aLength == bLength)) throw new IllegalArgumentException("These vectors can not be multiplied!");

        int[][] transpBMatrix = transp(bMatrix);
        result = new Integer[aMatrix.length][transpBMatrix.length];
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < aMatrix.length; i++) {
            for (int j = 0; j < transpBMatrix.length; j++) {
                VectorMultiplicationThread thread = new VectorMultiplicationThread(i, j, aMatrix[i], transpBMatrix[j]);
                threads.add(thread);
                thread.start();
                logger.info("{} started", thread.getName());
            }
        }
        threads.forEach(t -> {
            try {
                t.join(10000);
                logger.info("{} joined", t.getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        logger.info("result = {}", printMatrix(result));
        return result;
    }

    class VectorMultiplicationThread extends Thread {
        int row;
        int col;
        int[] a;
        int[] b;


        VectorMultiplicationThread(int row, int col, int[] a, int[] b) {
            super("Thread " + row + col);
            this.row = row;
            this.col = col;
            this.a = a;
            this.b = b;
            logger.info("New thread: {}", "Thread " + row + col);
        }

        @Override
        public void run() {
            Integer m = (new VectorMultiplication()).multiply(a, b);
            synchronized (result) {
                result[row][col] = m;
            }
        }
    }

    int[][] transp(int[][] bMatrix) {
        int[][] result = new int[bMatrix[0].length][bMatrix.length];
        for (int i = 0; i < bMatrix.length; i++) {
            for (int j = 0; j < bMatrix[0].length; j++)
                result[j][i] = bMatrix[i][j];
        }
        return result;
    }

    String printMatrix(Integer[][] matrix) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (int i = 0; i < matrix.length; i++) {
            sb.append('{');
            for (int j = 0; j < matrix[i].length; j++)
                sb.append(matrix[i][j]).append(' ');
            sb.append('}');
        }
        sb.append('}');
        return sb.toString();
    }

}
