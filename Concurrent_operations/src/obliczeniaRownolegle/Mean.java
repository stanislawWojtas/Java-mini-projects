package obliczeniaRownolegle;

import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mean {
    static double[] array;
    static BlockingQueue<Double> results = new ArrayBlockingQueue<Double>(130);


    static void initArray(int size){
        array = new double[size];
        for(int i=0;i<size;i++){
            array[i]= Math.random()*size/(i+1);
        }
    }

    static class MeanCalc extends Thread{
        private final int start;
        private final int end;
        double mean = 0;

        MeanCalc(int start, int end){
            this.start = start;
            this.end=end;
        }
        public void run(){
            for(int i = start; i < end; i++){
                mean += array[i];
            }
            mean /= (end - start);
            try {
                results.put(mean);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //System.out.printf(Locale.US,"%d-%d mean=%f\n",start,end,mean);
        }
    }

    //metoda join() może wyrzucic wyjątek
    static void parallelMeanV1(int cnt) throws InterruptedException {
        // utwórz tablicę wątków
        MeanCalc threads[]=new MeanCalc[cnt];

        int blockSize = array.length/cnt;
        for(int i = 0; i < cnt; i++){
            //wyliczam granice dla każdego bloku (start i end)
            int start = i * blockSize;
            int end = start + blockSize;
            threads[i] = new MeanCalc(start, end);
        }
        double t1 = System.nanoTime()/1e6;
        //uruchomienie wątków
        for(MeanCalc thread : threads){
            thread.start();
        }
        double t2 = System.nanoTime()/1e6;
        // czekanie na zakończenie za pomocą metody join()
        for(MeanCalc mc:threads) {
                mc.join();
        }
        // obliczenie średniej ze średnich
        double mean = 0;
        for(MeanCalc thread : threads){
            mean += thread.mean;
        }
        mean /= cnt;
        double t3 = System.nanoTime()/1e6;
        System.out.printf(Locale.US,"size = %d cnt=%d >  t2-t1=%f t3-t1=%f mean=%f\n",
            array.length,
            cnt,
            t2-t1,
            t3-t1,
            mean);
    }

    static void parallelMeanV2(int cnt) throws InterruptedException {
        double t1 = System.nanoTime()/1e6;
        int blockSize = array.length/cnt;
        for(int i = 0; i < cnt; i++){
            int start = i * blockSize;
            int end = start + blockSize;
            MeanCalc thread = new MeanCalc(start, end);
            thread.start();
        }
        double t2 = System.nanoTime()/1e6;
        double mean = 0;
        for(int i = 0; i < cnt; i++) {
            mean += results.take();
        }
        mean /= cnt;
        double t3 = System.nanoTime()/1e6;
        System.out.printf(Locale.US,"size = %d cnt=%d >  t2-t1=%f t3-t1=%f mean=%f\n",
            array.length,
            cnt,
            t2-t1,
            t3-t1,
            mean);
    }

    static void parallelMeanV3(int cnt) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(cnt);
        int blockSize = array.length/cnt;
        for(int i = 0; i < cnt; i++){
            int start = i * blockSize;
            int end = start + blockSize;
            executor.execute(new MeanCalc(start, end));
        }

        double t1 = System.nanoTime()/1e6;
        double mean = 0;
        for(int i = 0; i < cnt; i++){
            mean += results.take();
        }
        mean /= cnt;
        double t2 = System.nanoTime()/1e6;
        System.out.printf(Locale.US, "size = %d cnt=%d >  t2-t1=%f  mean=%f\n",
                array.length,
                cnt,
                t2-t1,
                mean);
        executor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        initArray(128000000);
        //trzy różne sposoby

        //parallelMeanV1(16);
        //parallelMeanV2(16);
        //parallelMeanV3(16);

        //sprawdzenie dla ilu wątków czas będzie najmniejszy - Odpowiedź: dla 64 wątków (na moim urządzeniu)
        for(int cnt:new int[]{1,2,4,8,16,32,64,128}){
            parallelMeanV3(cnt);
        }

    }
}
