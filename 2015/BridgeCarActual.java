// Section A: question 2 answer to Bridge, Car question

/* 

Solution to the exam paper.

*/

public class BridgeCarActual { 

	public static void main(final String[] args){
		Car[] cars = new Car[50]; // have 50 chars
		Bridge bridge = new Bridge();

		char[] option = new char[2];
		option[0] = 'A';
		option[1] = 'B';
		for(int i = 0 ; i < cars.length ; i++) {
			cars[i] = new Car(1000,bridge,i, option[ ((Math.random()<0.5)?0:1) ]); // assign a random lane to the car
			cars[i].start(); // start thread
		}
	}
}

class Bridge {
	private int totalWeight = 0;
	private int laneAtotalWeight  = 0;
	private int laneBtotalWeight = 0;


	public synchronized void enterBridge(Car car) throws InterruptedException {
		System.out.println("Entering the bridge! with id "+car.id +"weight of "+car.weight);
		System.out.println("totalWeight is "+totalWeight+"laneA "+laneAtotalWeight+"lane B "+laneBtotalWeight);
		while(totalWeight > 5000 || isDifferenceInLanesTooHigh() ){
			System.out.println("Unable to enter, waiting");
			wait();
		}

		totalWeight += car.weight;

		if(car.lane == 'A')
			laneAtotalWeight += car.weight;
		if(car.lane == 'B')
			laneBtotalWeight += car.weight;

		notifyAll();
	}

	// not really required in the exam 
	public boolean isDifferenceInLanesTooHigh(){
		return ( Math.abs(laneAtotalWeight - laneBtotalWeight) > 2000);
	}


	public synchronized void exitBridge(Car car){
		if(car.lane == 'A')
			laneAtotalWeight -= car.weight;
		if(car.lane =='B')
			laneBtotalWeight -= car.weight;
		totalWeight -= car.weight;
		System.out.println("Exiting!");
		notifyAll();

	}
}

class Car extends Thread { 
	// Probably best to use private variables in the exam..i cba
	public int weight;
	private Bridge bridge; 
	public int id;
	public char lane;

	public Car(int weight, Bridge bridge, int id, char lane){
		this.weight = weight;
		this.bridge = bridge;
		this.id = id;
		this.lane = lane;
	}

	public void run(){
		System.out.println("Going to enter lane " +lane+", has id of "+id);
		try{ 
			bridge.enterBridge(this);
			Thread.sleep(100); // remember to have a random time 
			bridge.exitBridge(this);
		} catch ( InterruptedException e ){
			// 
		}

	}
}