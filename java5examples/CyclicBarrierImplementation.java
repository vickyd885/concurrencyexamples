import java.util.concurrent.locks.*;
import java.util.concurrent.locks.Condition;

public class CyclicBarrierImplementation {
	public static void main(final String args[]){

		// EXAMPLE WITH OLD JAVA MECHANISM 
		CyclicBarrier barrier = new CyclicBarrier(2);
		RunAThread t1 = new RunAThread(1, barrier);
		RunAThread t2 = new RunAThread(2, barrier);

		// EXAMPLE WITH NEW JAVA PACKAGE
		CyclicBarrierNew barrierNew = new CyclicBarrierNew(2);
		RunAThreadNew t3 = new RunAThreadNew(3,barrierNew);
		RunAThreadNew t4 = new RunAThreadNew(4,barrierNew);
		try{
			// t1.start();
			// t2.start();
			t3.start();
			t4.start();
		} catch (Exception e ){

		}
		//System.out.println("I can continue!");
	}
}

// OLD JAVA SYNC  //
class RunAThread extends Thread{
	private int id;
	private CyclicBarrier barrier;
	public RunAThread(int id,CyclicBarrier barrier){
		this.id = id;
		this.barrier = barrier;
	}

	public void run(){
		try{
			System.out.println("I'm running! "+id);
			Thread.sleep(100);
			barrier.await();
			System.out.println("I'm running again!");
		}catch (InterruptedException e){
			//
		}
	}
}

// the actual class
class CyclicBarrier {
	private int threadCount;
	private int initialCount;

	public CyclicBarrier(int threadCount){
		this.threadCount = threadCount;
		this.initialCount = threadCount;
	}

	public synchronized void await() throws InterruptedException {
		threadCount--;
		if(threadCount > 0){
			this.wait();
		} else {
			threadCount = initialCount; 
			System.out.println("I have passed the barrier lol ");
			notifyAll();
		}
	}
}

// NEW JAVA SYNC // 
class RunAThreadNew extends Thread{
	private int id;
	private CyclicBarrierNew barrier;
	public RunAThreadNew(int id,CyclicBarrierNew barrier){
		this.id = id;
		this.barrier = barrier;
	}

	public void run(){
		try{
			System.out.println("I'm running! "+id);
			Thread.sleep(100);
			barrier.await();
			System.out.println("I'm running again!");

		}catch (InterruptedException e){
			//
		}
	}
}

// the actual class
class CyclicBarrierNew {
	private int threadCount;
	private int initialCount;

	private ReentrantLock lock = new ReentrantLock();
	private Condition cond = lock.newCondition();

	public CyclicBarrierNew(int threadCount){
		this.threadCount = threadCount;
		this.initialCount = threadCount;
	}

	public void await() throws InterruptedException {
		
		lock.lock();
		try{
			threadCount--;
			if(threadCount > 0){
				cond.await();
			} else {
				threadCount = initialCount; 
				System.out.println("I have passed the barrier lol ");
				cond.signalAll();
			}
		} catch (Exception e){
			//
		} finally {
			lock.unlock();
		}

	}
}