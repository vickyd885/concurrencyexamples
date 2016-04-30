/* How to use executor pools? */ 

import java.util.concurrent.Executors; 
import java.util.concurrent.ExecutorService;


public class ThreadPools {

	public static void main(final String[] args){
		ExecutorService executor =  Executors.newFixedThreadPool(5);

		for(int i = 0; i < 20 ; i++){
			executor.submit(new Worker(i));
		}
		
		executor.shutdown();
	}

}

class Worker extends Thread {

	private int id = 0;
	public Worker(int id){
		this.id = id;
	}

	public void run(){
		try{
			Thread.sleep(1000);
		}catch (InterruptedException e){
			//
		}
		System.out.println("Finished executing!");
	}
}