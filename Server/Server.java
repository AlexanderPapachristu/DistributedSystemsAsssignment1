import java.io.*;
import java.net.*;

class Server {
	public static void main(String[]args)
	{
		ServerSocket server = null;
		
		try{
			//server listening on port 1234
			server = new ServerSocket(1234);
			server.setReuseAddress(true);
			
			//run infinite loop for getting client request
			while(true){
				
				//socket to accept incoming client requests
				Socket client = server.accept();
				
				//Display a new client is connected
				System.out.println("New Client Connected" + client.getInetAddress().getHostAddress());
				
				//create new thread object
				ClientHandler clientSocket = new ClientHandler(client);
				
				//This thread will handle the client seperately
				new Thread(clientSocket).start();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally {
			if(server != null){
				try{
					server.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
}

class ClientHandler implements Runnable {
	private final Socket clientSocket;
	
	//Constructor
	public ClientHandler(Socket socket)
	{
		this.clientSocket = socket;
	}
	
	public void run()
	{
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			
			//get the outstream of the client
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			//get input stream from the client
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			String line;
			while ((line = in.readLine()) != null) {
				
				//writing the recieved message from client
				System.out.printf("Sent from the client: %s\n", line);
				
				out.println(math(line));
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(out != null){
					out.close();
				}
				if(in != null){
					in.close();
					clientSocket.close();
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String math(String message){
		String sign = "";
		
		if(message.contains("+"))
			sign = "+";
		else if(message.contains("-"))
			sign = "-";
		else if(message.contains("*"))
			sign = "*";
		else if(message.contains("/"))
			sign = "/";
		else
			return ("Input is not a basic math operation");
		
		String [] msgParts = message.split("[+*/]|(?<=\\s)-"); //break the string into each part seperated by each operation
		
		double num1 = Double.parseDouble(msgParts[0]);
		double num2 = Double.parseDouble(msgParts[1]);
		
		double ans = 0;
		switch(sign){
			case "+":
				ans = num1 + num2;
				break;
			case "-":
				ans = num1 - num2;
				break;
			case "*":
				ans = num1 * num2;
				break;
			case "/":
				ans = num1 / num2;
				break;
			default:
				System.out.println("Input is not readable");
		}
		String answer = Double.toString(ans);
		return answer;
	}
}