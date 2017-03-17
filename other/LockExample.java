/*
 * Java 5 ReentrantLock Example 1.
 */
import java.util.concurrent.locks.*;

public class LockExample {

	/***** A REENTRANT LOCK *****/	
	private static Lock lock = new ReentrantLock();
	
	public static void main(final String[] args) {

		Thread[] threads = new Thread[10];
		System.out.println("Creating Threads.");
		
		for (int i = 0; i < 10; i++) {
			threads[i] = new LockExampleThread(i, lock);
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
class LockExampleThread extends Thread {
	
	private int id;
	private Lock lock;
	
	public LockExampleThread(final int id, final Lock lock) {
		this.id = id;
		this.lock = lock;
	}
	
	public void run() {

		System.out.println("Thread " + id + " running.");
		
		try {
			// Sleep for number of seconds determined by id value.
			sleep(id * 1000);

			lock.lock();
			System.out.println("Thread " + id + " entered critical section.");
			sleep(5000);
		}
		catch (InterruptedException except){
			System.out.println("Thread " + id + " was interrupted.");
		}
		finally {
			lock.unlock();
			System.out.println("Thread " + id + " exiting critical section.");
		}
	}
}
