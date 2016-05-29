package snakeServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ConnectionHandler {
	ServerSocket socketListener = new ServerSocket(7777);
	protected static ArrayList<ClientHandler> allUsers = new ArrayList<>();
	int index = 0;
	int id = 0;

	public ConnectionHandler(World world) throws IOException {
		while (true) {
			Socket userSocket = socketListener.accept();
			
			if (userSocket != null) {
				System.out.println("Connecting...");
				id++;
				ClientHandler client = new ClientHandler(userSocket, id);
				allUsers.add(client);
				ServerConnection conn = new ServerConnection(client, world, this);
				index++;
				System.out.println("Connection established!");
				conn.start();
			}

		}
	}

}
