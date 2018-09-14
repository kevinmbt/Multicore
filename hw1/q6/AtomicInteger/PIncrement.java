package q6.AtomicInteger;

import java.util.concurrent.atomic.*;

public class PIncrement implements Runnable{
    private AtomicInteger c;
    private int incrementNumber;

    private static void spinupAtomic(AtomicInteger c,
                                     int incrementNumber,
                                     PIncrement[] threadList,
                                     int listLoc){
        threadList[listLoc] = new PIncrement(c, incrementNumber);
        threadList[listLoc].run();
    }

    public PIncrement(AtomicInteger c, int incrementNumber){
        this.c = c;
        this.incrementNumber = incrementNumber;
    }

    @Override
    public void run(){
        for(int i = 0; i < incrementNumber; i++){
            int expected = c.get();
            boolean prev = c.compareAndSet(expected, expected+1);
            if(!prev){
                i--;
            }
        }
    }

    public static int parallelIncrement(int c, int numThreads){
        int incrementPerThread = c/numThreads;
        int remainder = c%numThreads;

        AtomicInteger atomic = new AtomicInteger(0);
        PIncrement[] threadListAtomic = new PIncrement[numThreads];

        // Start Timer Atomic
        long atomicstart = System.nanoTime();
        for(int j = 0; j < numThreads; j++){
            int incrementBy = (j < remainder)?  incrementPerThread+1 :
                    incrementPerThread;
            spinupAtomic(atomic, incrementBy, threadListAtomic, j);
        }

        while (c != 1200000){}
        long atomicend = System.nanoTime();
        // End Time Atomic
        return (int)(atomicend - atomicstart);
    }
}