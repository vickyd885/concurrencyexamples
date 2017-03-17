/*
 * Java 5 Read/Write Lock Example.
 */

import java.util.concurrent.locks.*;

public class ReadWriteLockExample {

	/***** A READ WRITE LOCK *****/	
	private static ReadWriteLock rwlock = new ReentrantReadWriteLock();
	
	public static void main(final String[] args) {
		Thread[] threads = new Thread[10];
		
		System.out.println("Creating Threads.");
		
		
		for (int i = 0; i < 10; i++) {
			threads[i] = new ReadWriteLockExampleThread(i, rwlock);
		}

		System.out.println("Started Threads.");

		for (int i = 0; i < 10; i++) {
			threads[i].start();
		}

		System.out.println("All Threads Started.");
		
	}

}






































/*
 * ExampleThread encapsulates an integer id and a Java 5 ReadWriteLock.
 * It initially sleeps for a number of seconds determined by its id value.
 * Threads with even ids try to read lock the 'object' -
 * Threads with odd ids try to write lock the 'object'.
 */
class ReadWriteLockExampleThread extends Thread {
	
	private int id;
	private ReadWriteLock rwlock;
	
	public ReadWriteLockExampleThread(final int id, final ReadWriteLock rwlock) {
		this.id = id;
		this.rwlock = rwlock;
	}

	public void run() {
		
		Lock   my_lock;
		String description;

		if (id%4 == 0) {
			my_lock = rwlock.writeLock();
			description = "Writing ";
		} else {
			my_lock = rwlock.readLock();
			description = "Reading ";
		}

		System.out.println(description + "Thread " + id + " running.");
		
		try {
			// Sleep for number of seconds determined by id value.
			sleep(id * 1000);
			
			my_lock.lock();
			System.out.println(description + "Thread " + id + " entered critical section.");
			sleep(5000);
		}
		catch (InterruptedException except){
			System.out.println(description + "Thread " + id + " was interrupted.");
		}
		finally {
			System.out.println(description + "Thread " + id + " exiting critical section.");
			my_lock.unlock();
		}
	}
}
