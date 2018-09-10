
public class PIncrement{
	public static int parallelIncrement(int c, int numThreads){
		for(int i = 1; i <= 8; i++){
			int incrementPerThread = c/numThreads;
			int remainder = c%numThreads;

			AtomicInteger atomic = new AtomicInteger(0);
			AtomicIncrement[] threadListAtomic = new AtomicIncrement[i];
			
			// Start Timer Atomic
			for(int j = 0; j < i; j++){
				int incrementBy = (j < remainder) incrementPerThread+1 :
															 incrementPerThread;
				spinupAtomic(atomic, incrementBy, threadListAtomic, j); 	
			}

			for(int j = 0; j < i; j++){
				threadListAtomic[listLoc].join();
			}
			// End Time Atomic

			Integer synchronizedInt = new Integer(0);
			SynchronizedIncrement[] threadListSynchronized =
							new SynchronizedIncrement[i];

			for(int j = 0; j < i; j++){
				int incrementBy = (j < remainder) incrementPerThread+1 :
															 incrementPerThread;
				spinupSynchronized(synchronizedInt, incrementBy,
											threadListSynchronized, j);

			}

			for(int j = 0; j < i; j++){
				threadListSynchronized[j].join();	
			}

		}
	}

	private static void spinupAtomic(AtomicInteger c,
												int incrementNumber,
												AtomicIncrement[] threadList,
												int listLoc){
		threadList[listLoc] = new AtomicIncrement(c, incrementNumber);
		threadList[listLoc].start();
	}

	private static void spinupSynchronized(Integer c,
														int incrementNumber,
														SynchronizedIncrement[] threadList,
														int listLoc){
		threadList[listLoc] = new SynchronizedIncrement(c, incrementNumber);
		threadList[listLoc].start();
	}
}

class AtomicIncrement extends Thread{
	private AtomicInteger c;
	private int incrementNumber;

	public AtomicIncrement(AtomicInteger c, int incrementNumber){
		this.c = c;
		this.incrementNumber = incrementNumber;
	}

	@Override
	public void run{
		for(int i = 0; i < incrementNumber; i++){
			int expected = c.get();
			c.compareAndSet(expected, expected+1);
		}
	}
}

class SynchronizedIncrement extends Thread{
	private Integer c;
	private int incrementNumber;

	public SynchronizedIncrement(Integer c, int incrementNumber){
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
