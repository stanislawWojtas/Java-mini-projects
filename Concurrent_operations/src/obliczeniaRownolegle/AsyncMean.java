package obliczeniaRownolegle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class AsyncMean {
    static double[] array;

    static void initArray(int size) {
        array = new double[size];
        for (int i = 0; i < size; i++) {
            array[i] = Math.random() * size / (i + 1);
        }
    }

    static class MeanCalcSupplier implements Supplier<Double> {
        private final int start;
        private final int end;

        MeanCalcSupplier(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public Double get() {
            double mean = 0;
            for(int i = start; i < end; i++) {
                mean += array[i];
            }
            mean /= (end - start);
            System.out.printf(Locale.US,"%d-%d mean=%f\n",start,end,mean);
            return mean;
        }
    }

    public static void asyncMeanv1(){
        int size = 100_000_000;
        initArray(size);
        double t1 = System.nanoTime()/1e6;
        ExecutorService executor = Executors.newFixedThreadPool(16);
        int n=16;
        // Utwórz listę future
        List<CompletableFuture<Double>> partialResults = new ArrayList<>();
        int blockSize = size / n;
        for(int i=0;i<n;i++){
            int start = i * blockSize;
            int end = start + blockSize;
            CompletableFuture<Double> partialMean = CompletableFuture.supplyAsync(
                    new MeanCalcSupplier(start, end),executor);
            partialResults.add(partialMean);
        }
        // Agregacja wyników
        double mean=0;
        for(var pr:partialResults){
            // join() zawiesza wątek wołający
            mean += pr.join();
        }
        mean /= n;
        double t2 = System.nanoTime()/1e6;
        System.out.printf(Locale.US,"mean=%f    time needed: %f\n", mean, t2-t1);

        executor.shutdown();
    }

    public static void asyncMeanv2() throws InterruptedException {
        int size = 100_000_000;
        initArray(size);
        ExecutorService executor = Executors.newFixedThreadPool(16);
        int n=16;
        double t1 = System.nanoTime()/1e6;
        BlockingQueue<Double> queue = new ArrayBlockingQueue<>(n);
        int blockSize = size / n;
        for (int i = 0; i < n; i++) {
            int start = i * blockSize;
            int end = start + blockSize;
            CompletableFuture.supplyAsync(
                    new MeanCalcSupplier(start, end), executor)
            .thenApply(d -> queue.offer(d));
        }

        double mean=0;
        for(int i = 0; i < n; i++){
            mean += queue.take();
        }
        mean /= n;

        double t2 = System.nanoTime()/1e6;
        System.out.printf(Locale.US,"mean=%f    time needed: %f\n", mean, t2-t1);

        executor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Running Async Mean v1");
        asyncMeanv1();
        System.out.println("Running Async Mean v2");
        asyncMeanv2();
        //czasy są zbliżone do siebie, ale asyncMeanv2() jest zwykle nieco szybsza
    }
}
