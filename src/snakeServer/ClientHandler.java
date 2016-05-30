package snakeServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
	private Socket userSocket;
	private int userID = 0;
	private DataOutputStream outToClient;
	private boolean inGame = false;

	public ClientHandler(Socket userSocket, int userID) {
		this.userSocket = userSocket;
		this.userID = userID;
	}

	public void writeToClient(String output) {
		if (inGame) {
			try {
				outToClient = new DataOutputStream(userSocket.getOutputStream());
				outToClient.writeBytes(output);
			} catch (Exception e) {
				
				try {
					userSocket.close();
					this.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	public void writeToClient(String output, Boolean force) {
		if (inGame || force) {
			try {
				outToClient = new DataOutputStream(userSocket.getOutputStream());
				outToClient.writeBytes(output);
			} catch (Exception e) {
				try {
					userSocket.close();
					this.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void close() {
		ConnectionHandler.allUsers.remove(this);
	}

	public Socket getSocket() {
		return userSocket;
	}

	public void setUsername(int userID) {
		this.userID = userID;
	}

	public int getUserID() {
		return userID;
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
	
}