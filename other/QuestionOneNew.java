// 2013 paper attempt with reentrant locks

import java.util.concurrent.locks.*;
import java.util.concurrent.locks.Condition;

public class QuestionOneNew { 

	public static void main(final String[] args){
		System.out.println("Working");
		Shop shop = new Shop();

		Adult[] adults = new Adult[30];

		for(int i = 0; i < adults.length ; i++){
			adults[i] = new Adult(i,shop);
			adults[i].start();
		}
	}

}


class Shop {
	private int adultCount = 0; 
	private Lock lock = new ReentrantLock();
	private Condition cond = lock.newCondition();

	public Shop(){
		this.adultCount = 0;
	}

	public void enterShop(Adult adult) throws InterruptedException{
		lock.lock();

		try {
			while(adultCount >= 10){
				cond.await();
				System.out.println("Waiting for adults to decrease");				
			}
			adultCount++;
			cond.signal();
		} finally {
			System.out.println("Space for more adults: " + adultCount);
			System.out.println( adult.getIdOfAdult() + " has just entered");
			
			
			lock.unlock();
		}
	}
	
	public void exitShop() throws InterruptedException {
		lock.lock();
		try{
			while(adultCount < 0){
				System.out.println("Not enough adults somehow");
				cond.await();
			}
			adultCount--;
			cond.signal();
		}finally{
			System.out.println("Exiting! Space for more adults: " + adultCount);			
			lock.unlock();
		}
	}
}

class Adult extends Thread {
	private int id;
	private Shop shop;
	public Adult(int id,Shop shop){
		this.id = id;
		this.shop = shop;
	}

	public int getIdOfAdult(){
		return this.id;
	}
	public void run(){
		System.out.println("New adult entering of id, "+id+" - going to sleep randomly");
		try{
			shop.enterShop(this);
			Thread.sleep(1000); // 1 second for demo sake
			shop.exitShop();
		} catch (InterruptedException e) {
			// wtf am i doing
		}
		System.out.println("Finished now I'm going to exit");
	}
}
