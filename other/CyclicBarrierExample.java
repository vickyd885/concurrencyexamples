/*
 * Java 5 Cyclic Barrier Example.
 */

import java.util.concurrent.*;

public class CyclicBarrierExample {
	
	/***** A CYCLIC BARRIER RELEASING 4 THREADS AT A TIME *****/
	private static CyclicBarrier barrier = new CyclicBarrier(4);
	
	public static void main(final String[] args) {
		Thread[] threads = new Thread[10];
		
		System.out.println("Creating Threads.");
		
		
		for (int i = 0; i < 10; i++) {
			threads[i] = new CyclicBarrierExampleThread(i, barrier);
		}

		System.out.println("Started Threads.");
		for (int i = 0; i < 10; i++) {
			threads[i].start();
		}
		System.out.println("All Threads Started.");	
	}
}

/*
 * ExampleThread encapsulates an integer id and a Java 5 Cyclic Barrier.
 * It initially sleeps for a number of seconds determined by its id value.
 */
class CyclicBarrierExampleThread extends Thread {
	
	private int id;
	private CyclicBarrier barrier;
	
	public CyclicBarrierExampleThread(final int id, final CyclicBarrier barrier) {
		this.id = id;
		this.barrier = barrier;
	}
	
	public void run() {
		
		System.out.println("Thread " + id + " running.");
		
		try {
			// Sleep for number of seconds determined by id value.
			sleep(id * 1000);

			System.out.println("Thread " + id + " hitting barrier.");
			barrier.await();
			System.out.println("Thread " + id + " released from barrier.");
			sleep(5000);
		}
		catch (InterruptedException except){
			System.out.println("Thread " + id + " was interrupted.");
		}
		catch (BrokenBarrierException except){
			System.out.println("Thread " + id + " was interrupted.");
		}
	}    
}