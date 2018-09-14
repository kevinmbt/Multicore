package q6.Synchronized;

public class PIncrement implements Runnable{
    private static void spinupSynchronized(Integer c,
                                           int incrementNumber,
                                           PIncrement[] threadList,
                                           int ix){
        threadList[ix] = new PIncrement(c, incrementNumber);
        threadList[ix].run();
    }
    public static int parallelIncrement(int c, int numThreads){
        int incrementPerThread = c / numThreads;
        int remainder = c % numThreads;

        Integer synchronizedInt = new Integer(0);
        PIncrement[] threadListSynchronized =
                new PIncrement[numThreads];

        // Start Timer Synchronized
        long	 synchronizedstart = System.nanoTime();
        for(int j = 0; j < numThreads; j++){
            int incrementBy = (j < remainder)? incrementPerThread+1 :
                    incrementPerThread;
            spinupSynchronized(synchronizedInt, incrementBy,
                    threadListSynchronized, j);

        }

        while (c != 1200000){}

        long synchronizedend = System.nanoTime();
//        return (int)(synchronizedend - synchronizedstart);
        return c;
    }
    private Integer c;
    private int incrementNumber;

    public PIncrement(Integer c, int incrementNumber){
        this.c = c;
        this.incrementNumber = incrementNumber;
    }

    @Override
    public void run(){
        for(int i = 0; i < incrementNumber; i++){
            synchronized(this){
                c++;
            }
        }
    }
}