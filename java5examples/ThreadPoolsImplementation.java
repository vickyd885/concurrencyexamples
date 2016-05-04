/* How to implement a executor pools...lol what */ 

// fuck this til i learn how to do this

public class ThreadPoolsImplementation {

	public static void main(final String[] args){

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

class ExecutorPool {

	private int limit = limit;

	private ArrayList<Thread> list = new ArrayList<Thread>();

	public ExecutorPool(int limit){
		this.limit = limit;
	}

	public void submit(Thread thread){
		list.add(thread);
	}

	public void begin(){
		CountingSemaphore semaphore = new CountingSemaphore(limit);
		for(Threads i : list){
			semaphore.down();
			i.start();
		}
	}	
}

class CountingSemaphore{
	private int value;
	public CountingSemaphore(int initial){
		this.value = initial;
	}

	public synchronized void up() throws InterruptedException {
		value++; 
		notify();
	}

	public synchronized void down() throws InterruptedException {
		while(value==0)
			wait();
		value--;
	}

	public int getValue(){
		return this.value;
	}
}

