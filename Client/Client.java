import java.io.*;
import java.net.*;
import java.util.*;

class Client {
	
	//driver code
	public static void main(String[]args)
	{
		try (Socket socket = new Socket("localhost", 1234))
		{
			//writing to server
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			
			//reading from server
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			Scanner sc = new Scanner(System.in);
			String line = null;
			
			System.out.println("please input the name of the file containing basic math operations");
			String fileName = sc.nextLine();
			File myFile = new File (fileName);
			
			if(myFile.exists() && !myFile.isDirectory()){
				//reading from file
				
				Scanner fl = new Scanner(myFile);

				//while (!"exit".equalsIgnoreCase(line)){
				while(fl.hasNextLine()){
					//read line from user
					//line = sc.nextLine();
					line = fl.nextLine();
					
					// sending the user input to server
					out.println(line);
					out.flush();
					
					//displaying server reply
					System.out.println("User Input: " + line);
					System.out.println("Answer: " + in.readLine());
				}
			}
			else{
				System.err.println("Error: File not found");
			}
			
			//close scanner object
			sc.close();
		}
		catch (IOException e ){
			e.printStackTrace();
		}
	}
}