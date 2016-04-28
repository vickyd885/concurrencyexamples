// Section A: question 2 answer to Bridge, Car question

/* 

Here is a solution to a more complex problem! Kind of worth going through

Rules: 

Car can't enter if total weight > 5000
car can't enter lane a if lane a weight > 2000
car can't enter lane b if lane b weight > 2000 

useful for practising lock scoping

Lazy code follows

Note: this only came about cos i misread the question. fml.

*/

public class BridgeCar { 

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

	/* 
	I used three different object locks for each scenario that can take place.
	This is because if you lock on the Bridge object, it's kinda of buggy to implement conditional
	synchronisation for the 2 lanes. If you know how to do this, let me know! 
	 */
	private Object laneALock = new Object();
	private Object laneBLock = new Object();
	private Object bridgeLock = new Object();


	// enterBridge, enterLane and exitLane are the only methods you need to write for the exam
	public  void enterBridge(Car car){
		synchronized(bridgeLock){
			System.out.println("Total weight on the bridge: "+totalWeight);
			try {
				while(totalWeight > 5000){
					System.out.println("Total weight is too high, waiting!");
					bridgeLock.wait();
				}
			} catch (InterruptedException e){
				//
			}
			bridgeLock.notifyAll();
		}
	}

	public void enterLaneA(Car car){
		System.out.println("Going to enter lane A");
		
		synchronized(laneALock){
			try{
				while(laneAtotalWeight > 2000) {
					System.out.println("Lane A weight is too high, waiting!");
					laneALock.wait();
				}
			} catch (InterruptedException e){
				//
			}
			totalWeight += car.weight;
			laneAtotalWeight += car.weight;
			laneALock.notify();
		}
	}

	public void enterLaneB(Car car){
		System.out.println("Going to enter lane B");
		synchronized(laneBLock){
			try{
				while(laneBtotalWeight > 2000) {
					System.out.println("Lane B weight is too high, waiting!");
					laneBLock.wait();
				}
			} catch (InterruptedException e){
				//
			}
			totalWeight += car.weight;
			laneBtotalWeight += car.weight;
			laneBLock.notify();
		}
	}

	public void exitLaneA(Car car){
		System.out.println("Car is exiting lane A with id "+car.id);
		synchronized(laneALock){
			laneAtotalWeight -= car.weight;
			laneALock.notify();
		}
	}

	public void exitLaneB(Car car){
		System.out.println("Car is exiting lane Bwith id "+car.id);
		synchronized(laneBLock){
			laneBtotalWeight -= car.weight;
			laneBLock.notify();
		}
	}

	public void exitBridge(Car car){
		System.out.println("Off the bridge!");
		synchronized(bridgeLock){
			totalWeight -= car.weight;
			bridgeLock.notify();
		}
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
			// Yeah, this can be refactored, but the code is easy to follow atleast 
			bridge.enterBridge(this);
			if(this.lane == 'A'){
				bridge.enterLaneA(this);
				Thread.sleep(1000);
				bridge.exitLaneA(this);
			}
			if( this.lane == 'B'){
				bridge.enterLaneB(this);
				Thread.sleep(1000);
				bridge.exitLaneB(this);
			}
			bridge.exitBridge(this);
		} catch ( InterruptedException e ){
			// 
		}

	}
}