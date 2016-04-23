public class QuestionTwoCountingSemaphore {
	public static void main(final String[] args){
		Student[] studentList = new Student[45];
		ComputerLabMonitor comp = new ComputerLabMonitor();
		for(int i = 0; i< studentList.length ; i++){
			studentList[i] = new Student(i,comp);
			studentList[i].start();
		}

	}
}

class ComputerLabMonitor{

	private int rejectStudentCount = 0;


	private CountingSemaphore computerSemaphore = new CountingSemaphore(30);
	private CountingSemaphore waitingSemaphore = new CountingSemaphore(5);

	public ComputerLabMonitor(){
	}

	public synchronized void enterLab(Student student) throws InterruptedException {
		System.out.println("Entering Lab: "+student.id);

		while(computerSemaphore.getValue() == 0){
			System.out.println("computers used "+ computerSemaphore.getValue());
			waitInLabs(student);
		}
		computerSemaphore.down();
	}

	public synchronized void exitLab(Student student) throws InterruptedException { 
		System.out.println("Exiting Lab: "+student.id);
		computerSemaphore.up();
		waitingSemaphore.up();
	}

	public synchronized void waitInLabs(Student student) throws InterruptedException {
		System.out.println("Waiting positions left: "+ waitingSemaphore.getValue() );
		if(waitingSemaphore.getValue() == 0){
			rejectStudent(student);
		}
		System.out.println("Student is waiting INSIDE the lab "+student.id);
		waitingSemaphore.down();


	}

	public synchronized void rejectStudent(Student student) throws InterruptedException {
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
		this.sleep(200);
		comp.enterLab(this);
		this.sleep(1000);
		comp.exitLab(this);
		} catch (Exception e) {}
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