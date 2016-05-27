package snakeServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
	private Socket userSocket;
	private int userID = 0;
	private DataOutputStream outToClient;
	
	public ClientHandler(Socket userSocket, int userID){
		this.userSocket = userSocket;
		this.userID = userID;
	}
	
	public void writeToClient(String output){
		try{
		outToClient = new DataOutputStream(userSocket.getOutputStream());
		outToClient.writeBytes(output);
		} catch (Exception e){
			try {
				userSocket.close();
				this.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void close(){
	ConnectionHandler.allUsers.remove(this);
	}
	public Socket getSocket(){
		return userSocket;
	}
	public void setUsername(int userID){
		this.userID = userID;
	}
	public int getUserID(){
		return userID;
	}
}