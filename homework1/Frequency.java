import java.util.concurrent.*;
import java.util.Arrays;

public class Frequency {

	public static void main(String args[]){
		System.out.println(parallelFreq(4,new int[] {4,4,4,4,4,1,2,3,4,4,12,3,2,3,4},3));
	}

	public static int parallelFreq(int x, int[] A, int numThreads){
		int remainder = A.length % numThreads;
		int divide = A.length / numThreads;
		int current_loc = 0;
		int resulting_count = 0;
		FutureTask[] future_task_array = new FutureTask[numThreads];
		
		for(int i = 0; i < numThreads; i++){
			int array_portion = (i < remainder) ? divide + 1: divide;
			int end_portion = current_loc + array_portion;
			int[] chopped_array = Arrays.copyOfRange(A, current_loc, end_portion);
			current_loc = end_portion;
			Callable<Integer> freqcall = new FreqCall(x, chopped_array);	
			future_task_array[i] = new FutureTask(freqcall);
			future_task_array[i].run();
		}

		for(FutureTask f : future_task_array){
			try{
				resulting_count +=(int) f.get();		
			}
			catch(Exception e){}
		}
		
		return resulting_count;
	}
}

class FreqCall implements Callable<Integer>{
	private int x;
	private int[] A;

	public FreqCall(int x, int[] A){
		this.x = x;
		this.A = A;
	}

	@Override
	public Integer call() throws Exception {
		int[] A = this.A;
		int x = this.x;
		int count = 0;
		for(int a: A){
			if(a == x){
				count++;	
			}
		}
		return count;
	}
}
