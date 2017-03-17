// 2013 paper attempt with Semaphores

import java.util.concurrent.Semaphore;

public class QuestionOneSemaphoresB { 

	public static void main(final String[] args){
		System.out.println("Working");
		Shop shop = new Shop();
		
		Adult[] adults = new Adult[10];

		for(int i = 0; i < adults.length ; i++){
			adults[i] = new Adult(i,shop);
			adults[i].start();
		}
	}

}


class Shop {
	private int adultCount = 0; 
	private Semaphore semaphore = new Semaphore(10);
	public Shop(){
		this.adultCount = 0;
	}

	public synchronized void enterShop(Adult adult) throws InterruptedException{
		System.out.println("Available spaces: " + semaphore.availablePermits());
		semaphore.acquire();
		if(adultCount >= 10){
			System.out.println("Waiting for adults to decrease");
		}
		adultCount++;
		semaphore.release();
	}

	public synchronized void exitShop() throws InterruptedException {
		semaphore.acquire();
		if(adultCount < 0){
			System.out.println("Not enough adults somehow");
		}
		adultCount--;
		semaphore.release();
	}
}

class Adult extends Thread {
	private int id;
	private Shop shop;
	public Adult(int id,Shop shop){
		this.id = id;
		this.shop = shop;
	}

	// public int getPermits(){
	// 	return semaphore.availablePermits();
	// }

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
