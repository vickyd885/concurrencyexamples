// 2013 paper attempt with Semaphores

import java.util.concurrent.Semaphore;

public class QuestionOneSemaphoresA { 

	public static void main(final String[] args){
		System.out.println("Working");
		Shop shop = new Shop();
		Semaphore customers = new Semaphore(10);
		Adult[] adults = new Adult[30];
		for(int i = 0; i < adults.length ; i++){
			adults[i] = new Adult(i,shop,customers);
			adults[i].start();
		}
	}

}


class Shop {
	private int adultCount = 0; 

	public Shop(){
		this.adultCount = 0;
	}

	public synchronized void enterShop(Adult adult) throws InterruptedException{
		System.out.println("Available spaces: " + adult.getPermits());
		while(adultCount >= 10){
			System.out.println("Waiting for adults to decrease");
		}
		adultCount++;
	}

	public synchronized void exitShop() throws InterruptedException {
		while(adultCount < 0){
			System.out.println("Not enough adults somehow");
		}
		adultCount--;
	}
}

class Adult extends Thread {
	private int id;
	private Shop shop;
	private Semaphore semaphore;
	public Adult(int id,Shop shop,Semaphore sema){
		this.id = id;
		this.shop = shop;
		this.semaphore = sema;
	}

	public int getPermits(){
		return semaphore.availablePermits();
	}

	public int getIdOfAdult(){
		return this.id;
	}
	public void run(){
		System.out.println("New adult entering of id, "+id+" - going to sleep randomly");
		try{
			semaphore.acquire();
			shop.enterShop(this);
			Thread.sleep(1000); // 1 second for demo sake
			shop.exitShop();
			semaphore.release();
		} catch (InterruptedException e) {
			// wtf am i doing
		}
		System.out.println("Finished now I'm going to exit");
	}
}
