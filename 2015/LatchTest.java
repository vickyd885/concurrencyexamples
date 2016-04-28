// Section A, Q1g 

import java.util.concurrent.locks.*;
import java.util.concurrent.locks.Condition;
public class LatchTest {

	public static void main(final String[] args){
		Latch latch = new Latch(3);
		ChickenCounter chickenCount = new ChickenCounter(latch);
		ChickenShooter chickenShooter = new ChickenShooter(latch);

		chickenCount.start();
		chickenShooter.start();

	}

}

class Latch {
	private Lock lock = new ReentrantLock();
	private Condition cond = lock.newCondition();
	private int count = 0;

	public Latch(int threadCount){
		this.count = threadCount;
	}

	public void awaitZero() throws InterruptedException{
		try{
			lock.lock();
			while(count > 0)
				cond.await();
		}finally{
			lock.unlock();
		}
	}

	public void countDown(){
		try{
			lock.lock();
			if(--count <= 0){
				cond.signalAll();
			}
		}finally{
			lock.unlock();
		}
	}
}

// An incredibly strange example follows 

class ChickenCounter extends Thread{
	private Latch latch; 

	public ChickenCounter(Latch latch){
		this.latch = latch;
	}

	public void run(){
		try{
			System.out.println("I won't do anything unless chicken count is 0");
			latch.awaitZero();
			System.out.println("So the chicken count is now 0! I'm doing something");
		} catch (InterruptedException e){
			//
		}
	}
}


class ChickenShooter extends Thread {

	private Latch latch;

	public ChickenShooter(Latch latch){
		this.latch = latch;
	}
	public void run(){
		try{
			Thread.sleep(1000);
			this.latch.countDown();
			System.out.println("Shot one chicken");
			Thread.sleep(1000);
			this.latch.countDown();
			System.out.println("Shot one chicken");
			Thread.sleep(1000);
			this.latch.countDown();
				System.out.println("Shot one chicken");
		}catch (InterruptedException e){
			//
		}
	}
}