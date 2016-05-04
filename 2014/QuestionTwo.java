import java.util.Random;

public class QuestionTwo {
	public static void main(final String[] args){
		Student[] studentList = new Student[15];
		ComputerLabMonitor comp = new ComputerLabMonitor();
		for(int i = 0; i< studentList.length ; i++){
			studentList[i] = new Student(i,comp);
			studentList[i].start();
		}

	}
}

class ComputerLabMonitor{
	private int freeComputers = 10;
	private int waiting = 0;
	private int rejectStudentCount = 0;

	public ComputerLabMonitor(){

	}

	public synchronized void enterLab(Student student) throws InterruptedException {
		System.out.println("Entering Lab: "+student.id);
		while(freeComputers == 0){
			System.out.println("Spaces free: "+ freeComputers);
			waitInLabs(student);
		}
		freeComputers--;
		notify();
	}

	public synchronized void exitLab(Student student) throws InterruptedException { 
		System.out.println("Exiting Lab: "+student.id);
		freeComputers++;
		notify();
	}

	public void waitInLabs(Student student) throws InterruptedException {
		System.out.println("Number waiting: "+waiting );
		if(waiting >= 5){
			rejectStudent(student);
		}
		System.out.println("Student is waiting INSIDE the lab "+student.id);

		waiting++;
		while(freeComputers <= 0){
			wait();
		}
		notify();
	}

	public void rejectStudent(Student student) throws InterruptedException {
		student.interrupt();
		System.out.println("student is gone: "+student.id);
		rejectStudentCount++;
		System.out.println("Reject student count"+ rejectStudentCount);
	}
}

class Student extends Thread {
	public int id;
	private ComputerLabMonitor comp;

	public Student(int id, ComputerLabMonitor comp){
		this.id = id;
		this.comp = comp;
	}

	public void run(){
		try{
		Random waitingOutside = new Random(200);
		this.sleep(200);
		comp.enterLab(this);
		this.sleep(1000);
		comp.exitLab(this);
		} catch (Exception e) {}
	}
}