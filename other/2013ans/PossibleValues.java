public class PossibleValues{
	public static void main(final String[] args){
		ThreadA a = new ThreadA();
		ThreadB b = new ThreadB();

		int x;
		a.start(x);
		b.start(x);
	}
}

class ThreadA extends Thread{
	public void run(int x){
		x = 4;
		x = 2-x;
		System.out.println("A finished with: "+x);
	}
}

class ThreadB extends Thread{
	public void run(int x){
		x = 1;
		x = x-1;
		System.out.println("B finished with: "+x);
	}
}