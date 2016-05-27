package snakeServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnection extends Thread {
	private Socket userSocket;
	private Boolean connected = true;
	private ClientHandler client;
	private BufferedReader inFromClient;
	private Boolean connOpen = true;
	private int foodId = 0;
	private ArrayList<Food> foodList = new ArrayList<>();
	private ArrayList<Snake> snakeList = new ArrayList<>();
	private char splitter = (char) 007;
	private String split = "";

	public ServerConnection(ClientHandler client, ConnectionHandler connectionHandler) {
		this.userSocket = client.getSocket();
		this.client = client;
	}

	public void run() {
		try {
			confirmClient(userSocket);
		} catch (NullPointerException e) {
			client.close();
		}
	}

	public void confirmClient(Socket userSocket) {
		connOpen = true;
		String[] requestParam;

		try {
			inFromClient = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while (connected) {
				String request;
				request = inFromClient.readLine();
				split = String.valueOf(splitter);
				requestParam = request.split(split);
				System.out.println(request);

				switch (requestParam[0]) {
				case "":
					System.out.println("Waiting for input");
					break;

				case "LOGIN":
					login();
					break;

				case "NEW":
					switch (requestParam[1]) {
					case "SNAKE":
						System.out.println("We are snake now");
						int snakeId = Integer.parseInt(requestParam[2]);
						float snakeAngle = Float.parseFloat(requestParam[3]);
						float snakeX = Float.parseFloat(requestParam[4]);
						float snakeY = Float.parseFloat(requestParam[5]);
						Snake snake = new Snake(snakeId, snakeAngle, snakeX, snakeY);
						snakeList.add(snake);
						System.out.println("are we still snake though?");
						break;

					case "BODY":
						int parentSnakeId = Integer.parseInt(requestParam[2]);
						int bodyId = Integer.parseInt(requestParam[3]);
						float bodyX = Float.parseFloat(requestParam[4]);
						float bodyY = Float.parseFloat(requestParam[5]);
						Body body = new Body(bodyId, parentSnakeId, bodyX, bodyY);

						createNewBody(body);

						break;
					}
					break;
					
				case "DEL":
					switch(requestParam[1]){
					case "FOOD":
						int foodId = Integer.parseInt(requestParam[2]);
						deleteFood(foodId);
						break;
					}

				case "SET":
					switch (requestParam[1]) {
					case "SNAKE":
						int snakeId;
						float snakeX;
						float snakeY;
						float snakeAngle;
						break;
					// breaks out of SNAKE

					case "BODY":
						break;
					// breaks out of BODY

					case "FOOD":
						break;
					// breaks out of FOOD
					}
					break;
				// breaks out of SET

				case "SPAWN":
					switch (requestParam[1]) {
					case "FOOD":
						float foodX = Float.parseFloat(requestParam[2]);
						float foodY = Float.parseFloat(requestParam[3]);
						Food food = new Food(foodId, foodX, foodY);
						spawnFood(food);
						break;
					}
					break;
					
				}
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void spawnFood(Food food) {
		for (ClientHandler client : ConnectionHandler.allUsers) {
			client.writeToClient(
					"NEW" + split + "FOOD" + split + foodId + split + food.getX() + split + food.getY() + "\n");
		}
		foodList.add(food);
		foodId++;
	}

	public void createNewBody(Body body) {
		for (ClientHandler client : ConnectionHandler.allUsers) {
			if (client.getUserID() != body.getId()) {
				client.writeToClient("NEW" + split + "BODY" + split + body.getParentSnakeId() + split + body.getId()
						+ split + body.getX() + split + body.getY() + "\n");
			}
		}

		for (Snake snake : snakeList) {
			if (snake.getId() == body.getParentSnakeId()) {
				snake.getBodyList().add(body);
			}
		}
	}

	public void createNewSnake(Snake snake) {
		for (ClientHandler client : ConnectionHandler.allUsers) {
			if (client.getUserID() != snake.getId()) {
				System.out.println(snake.getId() + ", " + client.getUserID() + " Test");
				client.writeToClient("NEW" + split + "SNAKE" + split + snake.getId() + split + snake.getAngle() + split
						+ snake.getX() + split + snake.getY() + '\n');
			} else {
				client.writeToClient("NEW" + split + "SNAKE" + split + snake.getId() + split + snake.getAngle() + split
						+ snake.getX() + split + snake.getY() + '\n');
				
				for (Food food : foodList) {
					client.writeToClient(
							"NEW" + split + "FOOD" + split + foodId + split + food.getX() + split + food.getY() + "\n");
				}
				for (Snake s : snakeList) {
					client.writeToClient("NEW" + split + "SNAKE" + split + s.getId() + split + s.getAngle()
							+ split + s.getX() + split + s.getY() + '\n');
										for(Body b : s.getBodyList()){
						client.writeToClient("NEW" + split + "BODY" + split + b.getParentSnakeId() + split + b.getId()
						+ split + b.getX() + split + b.getY() + "\n");
					}
				}
			}
		}
	}

	public void deleteFood(int id){
		for(Food f : foodList){
			if(f.id == id)
				foodList.remove(f);
			break;
		}
		for(ClientHandler client : ConnectionHandler.allUsers){
			client.writeToClient("DEL" + split + "FOOD" + split + id + '\n');
		}
	}
	public void login() {
		client.writeToClient("CONFIRM" + split + client.getUserID() + '\n');
	}
}