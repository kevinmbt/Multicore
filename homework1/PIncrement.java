import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

public class PIncrement{
	public static void main(String args[]){
		for(int i = 1; i <=8; i++){
			parallelIncrement(12000000, i);	
		}
	}
	public static int parallelIncrement(int c, int numThreads){
			int incrementPerThread = c/numThreads;
			int remainder = c%numThreads;

			AtomicInteger atomic = new AtomicInteger(0);
			AtomicIncrement[] threadListAtomic = new AtomicIncrement[numThreads];
			
			// Start Timer Atomic
			long atomicstart = System.nanoTime();
			for(int j = 0; j < numThreads; j++){
				int incrementBy = (j < remainder)?  incrementPerThread+1 :
															 incrementPerThread;
				spinupAtomic(atomic, incrementBy, threadListAtomic, j); 	
			}

			for(int j = 0; j < numThreads; j++){
				try{
					threadListAtomic[j].join();
				}
				catch(Exception e){
					
				}
			}
			long atomicend = System.nanoTime();
			// End Time Atomic
			System.out.println("Atomic Time for " +numThreads+ " threads:: " +
								(atomicend-atomicstart));
			
			Integer synchronizedInt = new Integer(0);
			SynchronizedIncrement[] threadListSynchronized =
							new SynchronizedIncrement[numThreads];

			// Start Timer Synchronized
			long	 synchronizedstart = System.nanoTime();
			for(int j = 0; j < numThreads; j++){
				int incrementBy = (j < remainder)? incrementPerThread+1 :
															 incrementPerThread;
				spinupSynchronized(synchronizedInt, incrementBy,
											threadListSynchronized, j);

			}

			for(int j = 0; j < numThreads; j++){
				try{	
					threadListSynchronized[j].join();	
				}
				catch(Exception e){
					
				}
			}
			long synchronizedend = System.nanoTime();
			// End Timer Synchronized
			System.out.println("Synchronized Time for " +numThreads+ " threads:: " +
								(synchronizedend-synchronizedstart));
			final ReentrantLock lock = new ReentrantLock();
			Integer reentrantInt = new Integer(0);
			ReentrantIncrement[] threadListReentrant = 
							new ReentrantIncrement[numThreads];

			// Start Time Reentrant Lock
			long reentrantstart = System.nanoTime();
			for(int j = 0; j < numThreads; j++){
				int incrementBy = (j < remainder)? incrementPerThread+1 :
															 incrementPerThread;
				spinupReentrant(reentrantInt, incrementBy,
									lock, threadListReentrant, j);
			}

			for(int j = 0; j < numThreads; j++){
				try{
					threadListReentrant[j].join();
				}
				catch(Exception e){
						
				}
			}
			long reentrantend = System.nanoTime();
			System.out.println("Reentrant Time for " +numThreads+ " threads:: " +
								(reentrantend-reentrantstart));

			// End Time Reentrant Lock
		return 0;

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

	private static void spinupReentrant(Integer c,
													int incrementNumber,
													ReentrantLock lock,
													ReentrantIncrement[] threadList,
													int listLoc){
		threadList[listLoc] = new ReentrantIncrement(c, incrementNumber, lock);
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
	public void run(){
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

class ReentrantIncrement extends Thread{
	private Integer c;
	private int incrementNumber;
	private ReentrantLock lock;

	public ReentrantIncrement(Integer c, int incrementNumber, ReentrantLock lock){
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
