/*
 * Java 5 ReentrantLock Example 3 - a fair lock.
 */
import java.util.concurrent.locks.*;

public class LockExample3 {

	/***** A FAIR REENTRANT LOCK *****/	
	private static Lock lock = new ReentrantLock(true);
	
	public static void main(final String[] args) {

		Thread[] threads = new Thread[10];
		System.out.println("Creating Threads.");
		
		for (int i = 0; i < 10; i++) {
			threads[i] = new LockExampleThread3(i, lock);
		}

		System.out.println("Started Threads.");

		for (int i = 0; i < 10; i++) {
			threads[i].start();
		}

		System.out.println("All Threads Started.");
		
	}
}

/*
 * ExampleThread encapsulates an integer id and a Java 5 Lock.
 * It initially sleeps for a number of seconds determined by its id value.
 * It then tries to lock before sleeping for 5 seconds within the critical section.
 */
class LockExampleThread3 extends Thread {
	
	private int id;
	private Lock lock;
	
	public LockExampleThread3(final int id, final Lock lock) {
		this.id = id;
		this.lock = lock;
	}
	
	public void run() {

		System.out.println("Thread " + id + " running.");
		
		try {
			// Sleep for number of seconds determined by id value.
			sleep(id * 5);

			lock.lock();
			System.out.println("Thread " + id + " entered critical section.");
			sleep(5);
		}
		catch (InterruptedException except){
			System.out.println("Thread " + id + " was interrupted.");
		}
		finally {
			//System.out.println("Thread " + id + " exiting critical section.");
			lock.unlock();
		}
	}
}
