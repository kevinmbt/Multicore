package q6.ReentrantLock;

import java.util.concurrent.locks.*;

public class PIncrement implements Runnable{
    private static void spinupReentrant(Integer c, int incrementNumber, ReentrantLock lock, PIncrement[] threadList, int listLoc){
        threadList[listLoc] = new PIncrement(c, incrementNumber, lock);
        threadList[listLoc].run();
    }

    public static int parallelIncrement(int c, int numThreads){
        int incrementPerThread = c/numThreads;
        int remainder = c%numThreads;

        final ReentrantLock lock = new ReentrantLock();
        Integer reentrantInt = new Integer(0);
        PIncrement[] threadListReentrant =
                new PIncrement[numThreads];

        // Start Time Reentrant Lock
        long reentrantstart = System.nanoTime();
        for(int j = 0; j < numThreads; j++){
            int incrementBy = (j < remainder)? incrementPerThread+1 :
                    incrementPerThread;
            spinupReentrant(reentrantInt, incrementBy,
                    lock, threadListReentrant, j);
        }

        while (c != 1200000){}

        long reentrantend = System.nanoTime();

        // End Time Reentrant Lock
//        return (int)(reentrantend - reentrantstart);
        return c;
    }
    private Integer c;
    private int incrementNumber;
    private ReentrantLock lock;

    public PIncrement(Integer c, int incrementNumber, ReentrantLock lock){
        this.c = c;
        this.incrementNumber = incrementNumber;
        this.lock = lock;
    }

    @Override
    public void run(){
        for(int i = 0; i < incrementNumber; i++){
            lock.lock();
            try{
                c++;
            }
            finally{
                lock.unlock();
            }
        }
    }
}