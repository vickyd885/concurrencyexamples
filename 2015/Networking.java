import java.net.*;
import java.io.*;

public class Networking {
	public static void main(final String[] args){
		Server server = new Server();
		Client client = new Client();
		try{
			server.start();
			client.start();	
		} catch (Exception e) {
			//
		}
	}
}

class Server extends Thread { 
	private int portNumber = 8888; 

	public Server(){

	}

	public void run() {
		while(true){
			try{
				// make a serverSocket and assign a port
				ServerSocket serverSocket = new ServerSocket(portNumber);
				System.out.println("Server is running!");

				// assuming a client connects...
				Socket clientSocket = serverSocket.accept(); //..accept the connection 

				// get the input stream
				DataInputStream in = new DataInputStream(clientSocket.getInputStream());

				// read the input
				int number = in.readInt();
				System.out.println(number);
				String check = checkPrime( number ); // check prime
				//System.out.println(check);

				// set up a Output stream and link this to the client socket 
				DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
				
				// write a string back 
				out.writeUTF(check);
				
			} catch (Exception e){
				//
			}
		}
	}

	public String checkPrime(int number){
		for( int i = 2 ; i < Math.pow(number,0.5) ; i++){
			if(number % i == 0){
				return "No";
			}
		}
		return "Yes";
	}

}

class Client extends Thread {

	public Client(){
	}

	public void run()  {
		try{

			// set up connection 
			Socket serverConnection = new Socket("localhost", 8888);

			// set up a output stream
			OutputStream toServer = serverConnection.getOutputStream();
			DataOutputStream output = new DataOutputStream(toServer);
			
			int number = 17;

			// write to output stream
			output.writeInt(number);
			
			// set up an input stream to get the message back
			InputStream fromServer = serverConnection.getInputStream();
			DataInputStream in = new DataInputStream(fromServer);
			String message = in.readUTF(); // read the string
			System.out.println(message);

			//close connection
			serverConnection.close();

		} catch (Exception e){
			//
		}

	}
}