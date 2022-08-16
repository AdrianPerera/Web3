
import java.util.Arrays;
import java.util.concurrent.Phaser;

import static junit.framework.Assert.assertEquals;

public class FuzzyBarrier {
    final static private int niterations =40000;
    final static private int N = 1000;
    final static private int ntasks = 12;

    public static void main(String[] args) {
        double[] myNew = createArray(N, niterations);
        double[] myVal = createArray(N, niterations);
        final double[] myNewRef = createArray(N, niterations);
        final double[] myValRef = createArray(N, niterations);
        long barrierTotalTime = 0;
        long fuzzyTotalTime = 0;

        final long barrierStartTime = System.currentTimeMillis();
        runParallelBarrier(niterations, myNew, myVal, N, ntasks);
        final long barrierEndTime = System.currentTimeMillis();

        final long fuzzyStartTime = System.currentTimeMillis();
        runParallelFuzzyBarrier(niterations, myNewRef, myValRef, N, ntasks);
        final long fuzzyEndTime = System.currentTimeMillis();

//        barrierTotalTime += (barrierEndTime - barrierStartTime);
//        fuzzyTotalTime += (fuzzyEndTime - fuzzyStartTime);

//        Arrays.stream(myVal).sequential().forEach(e-> System.out.printf(e+" "));
//        System.out.println();
//        Arrays.stream(myValRef).sequential().forEach(e-> System.out.printf(e+" "));
//



//        System.out.println();
//        System.out.println( "bar/fuzzy " + (double)barrierTotalTime / (double)fuzzyTotalTime);

        if (niterations % 2 == 0) {
            checkResult(myNew, myNewRef);
        } else {
            checkResult(myVal, myValRef);
        }
    }

    private static void checkResult(final double[] ref, final double[] output) {
        for (int i = 0; i < ref.length; i++) {
            String msg = "Mismatch on output at element " + i;
            assertEquals(msg, ref[i], output[i]);
        }
    }

    private static double[] createArray(final int N, final int iterations) {
        final double[] input = new double[N + 2];
        int index = N + 1;
        while (index > 0) {
            input[index] = 1.0;
            index -= (iterations / 4);
        }
        return input;
    }

    public static void runSequential(final int iterations, final double[] myNew, final double[] myVal, final int n) {
        double[] next = myNew;
        double[] curr = myVal;

        for (int iter = 0; iter < iterations; iter++) {
            for (int j = 1; j <= n; j++) {
                next[j] = (curr[j - 1] + curr[j + 1]) / 2.0;
            }
            double[] tmp = curr;
            curr = next;
            next = tmp;
            System.out.println();
            Arrays.stream(curr).sequential().forEach(e-> System.out.printf(e+" "));
            System.out.println();
            Arrays.stream(next).sequential().forEach(e-> System.out.printf(e+" "));
            System.out.println();

        }
    }

    private static void runParallelBarrier(final int iterations, final double[] myNew, final double[] myVal,
        final int n, final int tasks) {
        Phaser ph = new Phaser(0);
        ph.bulkRegister(tasks);

        Thread[] threads = new Thread[tasks];

        for (int ii = 0; ii < tasks; ii++) {
            final int i = ii;

            threads[ii] = new Thread(() -> {
                double[] threadPrivateMyVal = myVal;
                double[] threadPrivateMyNew = myNew;

                final int chunkSize = (n + tasks - 1) / tasks;
                final int left = (i * chunkSize) + 1;
                int right = (left + chunkSize) - 1;
                if (right > n) right = n;

                for (int iter = 0; iter < iterations; iter++) {

                    for (int j = left; j <= right; j++) {
                        threadPrivateMyNew[j] = (threadPrivateMyVal[j - 1] + threadPrivateMyVal[j + 1]) / 2.0;
                    }
                    ph.arriveAndAwaitAdvance();

                    double[] temp = threadPrivateMyNew;
                    threadPrivateMyNew = threadPrivateMyVal;
                    threadPrivateMyVal = temp;
                }
            });
            threads[ii].start();
        }

        for (int ii = 0; ii < tasks; ii++) {
            try {
                threads[ii].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void runParallelFuzzyBarrier(final int iterations, final double[] myNew, final double[] myVal, final int n, final int tasks) {
        Phaser ph = new Phaser(0);
        ph.bulkRegister(tasks);

        Thread[] threads = new Thread[tasks];

        for (int ii = 0; ii < tasks; ii++) {
            final int i = ii;

            threads[ii] = new Thread(() -> {
                double[] threadPrivateMyVal = myVal;
                double[] threadPrivateMyNew = myNew;

                for (int iter = 0; iter < iterations; iter++) {
                    final int chunkSize = (n + tasks - 1) / tasks;
                    final int left = (i * chunkSize) + 1;
                    int right = (left + chunkSize) - 1;
                    if (right > n)
                        right = n;

                    threadPrivateMyNew[left] = (threadPrivateMyVal[left-1]+threadPrivateMyVal[left+1])/2.0;
                    threadPrivateMyNew[right] = (threadPrivateMyVal[right-1]+threadPrivateMyVal[right+1])/2.0;
                    int cp = ph.arrive();

                    for (int j = left+1; j <= right-1; j++) {
                        threadPrivateMyNew[j] = (threadPrivateMyVal[j - 1] + threadPrivateMyVal[j + 1]) / 2.0;
                    }
                    ph.awaitAdvance(cp);
                    double[] temp = threadPrivateMyNew;
                    threadPrivateMyNew = threadPrivateMyVal;
                    threadPrivateMyVal = temp;
                }
            });
            threads[ii].start();
        }

        for (int ii = 0; ii < tasks; ii++) {
            try {
                threads[ii].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
