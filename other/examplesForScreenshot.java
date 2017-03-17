// When you can't be asked to format rows 

synchronized void method() {
	// this made atomic
}

/* --- OR --- */ 
Object lock = new Object(); 
void method(){
	synchronized(lock){
		// this made atomic 
	}
}

/* --- New Java Package --- */ 

Lock lock = new ReentrantLock(); 
void method(){
	lock.lock(); 
	// everything that follows is atomic
	try{
	}catch(){
	} finally {
		lock.unlock(); // this must be unlocked in finally 
	}
}