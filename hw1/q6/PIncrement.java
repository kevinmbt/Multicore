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

//			AtomicInteger atomic = new AtomicInteger(0);
//			AtomicIncrement[] threadListAtomic = new AtomicIncrement[numThreads];
//
//			// Start Timer Atomic
//			long atomicstart = System.nanoTime();
//			for(int j = 0; j < numThreads; j++){
//				int incrementBy = (j < remainder)?  incrementPerThread+1 :
//															 incrementPerThread;
//				spinupAtomic(atomic, incrementBy, threadListAtomic, j);
//			}
//
//			for(int j = 0; j < numThreads; j++){
//				try{
//					threadListAtomic[j].join();
//				}
//				catch(Exception e){
//
//				}
//			}
//			long atomicend = System.nanoTime();
//			// End Time Atomic

//			Integer synchronizedInt = new Integer(0);
//			SynchronizedIncrement[] threadListSynchronized =
//							new SynchronizedIncrement[numThreads];
//
//			// Start Timer Synchronized
//			long	 synchronizedstart = System.nanoTime();
//			for(int j = 0; j < numThreads; j++){
//				int incrementBy = (j < remainder)? incrementPerThread+1 :
//															 incrementPerThread;
//				spinupSynchronized(synchronizedInt, incrementBy,
//											threadListSynchronized, j);
//
//			}
//
//			for(int j = 0; j < numThreads; j++){
//				try{
//					threadListSynchronized[j].join();
//				}
//				catch(Exception e){
//
//				}
//			}
//			long synchronizedend = System.nanoTime();
//			// End Timer Synchronized
//			final ReentrantLock lock = new ReentrantLock();
//			Integer reentrantInt = new Integer(0);
//			ReentrantIncrement[] threadListReentrant =
//							new ReentrantIncrement[numThreads];
//
//			// Start Time Reentrant Lock
//			long reentrantstart = System.nanoTime();
//			for(int j = 0; j < numThreads; j++){
//				int incrementBy = (j < remainder)? incrementPerThread+1 :
//															 incrementPerThread;
//				spinupReentrant(reentrantInt, incrementBy,
//									lock, threadListReentrant, j);
//			}
//
//			for(int j = 0; j < numThreads; j++){
//				try{
//					threadListReentrant[j].join();
//				}
//				catch(Exception e){
//
//				}
//			}
//			long reentrantend = System.nanoTime();
//
//			// End Time Reentrant Lock
			final Tournament t = new Tournament();
			Integer tournamentInt = new Integer(0);
			TournamentIncrement[] threadListTournament = new
							TournamentIncrement[numThreads];
			long tournamentstart = System.nanoTime();
			for(int j = 0; j < numThreads;j++){
				int incrementBy = (j < remainder) ? incrementPerThread+1 :
																incrementPerThread;
				spinupTournament(tournamentInt, incrementBy, t, 
										threadListTournament, j);
			}
			for(int j = 0; j < numThreads; j++){
				try{
					threadListTournament[j].join();
				}
				catch(Exception e){
					
				}
			}
			long tournamentend = System.nanoTime();
			
		return 0;

	}

//	private static void spinupAtomic(AtomicInteger c,
//												int incrementNumber,
//												AtomicIncrement[] threadList,
//												int listLoc){
//		threadList[listLoc] = new AtomicIncrement(c, incrementNumber);
//		threadList[listLoc].start();
//	}

//	private static void spinupSynchronized(Integer c,
//														int incrementNumber,
//														SynchronizedIncrement[] threadList,
//														int listLoc){
//		threadList[listLoc] = new SynchronizedIncrement(c, incrementNumber);
//		threadList[listLoc].start();
//	}

//	private static void spinupReentrant(Integer c,
//													int incrementNumber,
//													ReentrantLock lock,
//													ReentrantIncrement[] threadList,
//													int listLoc){
//		threadList[listLoc] = new ReentrantIncrement(c, incrementNumber, lock);
//		threadList[listLoc].start();
//	}

	private static void spinupTournament(Integer c,
													int incrementNumber,
													Tournament t,
													TournamentIncrement[] threadList,
													int listLoc){
		threadList[listLoc] = new TournamentIncrement(c,incrementNumber,t,listLoc);
		threadList[listLoc].start();
	}

}

//class AtomicIncrement extends Thread{
//	private AtomicInteger c;
//	private int incrementNumber;
//
//	public AtomicIncrement(AtomicInteger c, int incrementNumber){
//		this.c = c;
//		this.incrementNumber = incrementNumber;
//	}
//
//	@Override
//	public void run(){
//		for(int i = 0; i < incrementNumber; i++){
//			int expected = c.get();
//			boolean prev = c.compareAndSet(expected, expected+1);
//			if(!prev){
//				i--;
//			}
//		}
//	}
//}

//class SynchronizedIncrement extends Thread{
//	private Integer c;
//	private int incrementNumber;
//
//	public SynchronizedIncrement(Integer c, int incrementNumber){
//		this.c = c;
//		this.incrementNumber = incrementNumber;
//	}
//
//	@Override
//	public void run(){
//		for(int i = 0; i < incrementNumber; i++){
//			synchronized(this){
//				c++;
//			}
//		}
//	}
//}
//
//class ReentrantIncrement extends Thread{
//	private Integer c;
//	private int incrementNumber;
//	private ReentrantLock lock;
//
//	public ReentrantIncrement(Integer c, int incrementNumber, ReentrantLock lock){
//		this.c = c;
//		this.incrementNumber = incrementNumber;
//		this.lock = lock;
//	}
//
//	@Override
//	public void run(){
//		for(int i = 0; i < incrementNumber; i++){
//			lock.lock();
//			try{
//				c++;
//			}
//			finally{
//				lock.unlock();
//			}
//		}
//	}
//}

class TournamentIncrement extends Thread{
	private Integer c;
	private volatile int incrementNumber;
	private Tournament tournament;
	private volatile int myThread;

	public TournamentIncrement(Integer c, int incrementNumber, Tournament tournament, int myThread){
		this.c = c;
		this.incrementNumber = incrementNumber;
		this.tournament = tournament;
		this.myThread = myThread;
	}

	@Override
	public void run(){
		for(int i = 0; i < incrementNumber; i++){
			try{	
			tournament.lock1(myThread);
			tournament.lock2(myThread);
			tournament.lock3(myThread);
			c++;
			}
			finally{
				tournament.unlock(myThread);
			}
		}
	}
}

class Tournament{
	private volatile int[] locks = new int[]{-1,-1,-1,-1,-1,-1,-1};

	private volatile boolean[] flag = new boolean[8];
	private volatile int[] victim = new int[7];
	public void lock1(int i) {
		int j;
		if(i%2 == 1){
			j = i-1;
		}
		else{
			j = i+1;
		}
		flag[i] = true;
		// I’m interested
		victim[i/2] = i;
		// you go first
		while( ( j != -1 && flag[j] && victim[i/2] == i)) {
			}; // wai	t
		locks[i/2] = i;
	}
	public void lock2(int i){
		int j;
		if((i/2)%2 == 1){
			j = locks[(i/2)-1];
		}
		else{
			j = locks[(i/2)+1];
		}
		flag[i] = true;
		victim[4+(i/4)] = i;
		while(j!=-1 && flag[j] && victim[4+(i/4)] == i){
		};
		locks[4+(i/4)] = i;
	}
	public void lock3(int i){
		int j;
		if((i/4) == 0){
			j = locks[5];
		}
		else{
			j = locks[4];
		}
		flag[i] = true;
		victim[6] = i;
		while(j!= -1 && flag[j] && victim[6] == i){
		};
		locks[6] = i;
	}
	public void unlock(int i) {	
		locks[i/2] = -1;
		locks[4+(i/4)] = -1;
		locks[6] = -1;
		flag[i] = false;
		
		// I’m not interested
	}
}
