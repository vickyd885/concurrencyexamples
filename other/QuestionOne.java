// java 2013 q1 shop, adult question 
import java.util.concurrent.ExecutorService;

public class QuestionOne {

	public static void main(final String[] args){
		System.out.println("I'm running");
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
	private Object lock = new Object();
	
	public Shop(){
		this.adultCount = 0;
	}

	public void enterShop(Adult adult) throws InterruptedException {
		synchronized (lock){
			while( adultCount >= 10 ){
				lock.wait();
				System.out.println("Waiting for adults to decrease");
			}

			System.out.println("Space for more adults: " + adultCount);
			System.out.println( adult.getIdOfAdult() + " has just entered");
			adultCount++;
			lock.notify();
		}
	}

	public void exitShop() throws InterruptedException {
		synchronized (lock){
			while(adultCount <= 0){
				System.out.println("Not enough adults somehow");
				lock.wait();
			}
			adultCount--;
			lock.notify();
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

