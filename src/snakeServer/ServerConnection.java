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
	private World world;
	private char splitter = (char)007;
	private String split = "";

	public ServerConnection(ClientHandler client,World world, ConnectionHandler connectionHandler) {
		this.userSocket = client.getSocket();
		this.client = client;
		this.world = world;
	}

	public void run() {
		try {
			confirmClient(userSocket);
		} catch (NullPointerException e) {
			for (Snake snake : world.getSnakeList()){
				if(client.getUserID() == snake.getId()){
					world.getSnakeList().remove(snake);
				}
			}
			client.close();
		}
	}

	public void confirmClient(Socket userSocket) {
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

				switch (requestParam[0]) {
				case "":
					break;

				case "LOGIN":
					login();
					break;

				case "NEW":
					switch (requestParam[1]) {
					case "SNAKE":
						int snakeId = Integer.parseInt(requestParam[2]);
						float snakeAngle = Float.parseFloat(requestParam[3]);
						float snakeX = Float.parseFloat(requestParam[4]);
						float snakeY = Float.parseFloat(requestParam[5]);
						Snake snake = new Snake(snakeId, snakeAngle, snakeX, snakeY);
						createNewSnake(snake);
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
					int snakeId;
					switch (requestParam[1]) {
					case "FOOD":
						//System.out.println(request);
						snakeId = Integer.parseInt(requestParam[2]);
						int foodId = Integer.parseInt(requestParam[3]);
						deleteFood(snakeId,foodId);
						break;

					case "SNAKE":
						snakeId = Integer.parseInt(requestParam[2]);
						deleteSnake(snakeId);
						break;
					}
					break;

				case "CLOSE":
					int id = Integer.parseInt(requestParam[1]);
					closeConn(id);
					break;

				case "SET":
					switch (requestParam[1]) {
					case "SNAKE":
						snakeId = Integer.parseInt(requestParam[2]);
						float snakeAngle = Float.parseFloat(requestParam[3]);
						float snakeX = Float.parseFloat(requestParam[4]);
						float snakeY = Float.parseFloat(requestParam[5]);
						setSnake(snakeId, snakeX, snakeY, snakeAngle);
						break;

					case "BODY":
						int parentSnakeId = Integer.parseInt(requestParam[2]);
						int bodyId = Integer.parseInt(requestParam[3]);
						float bodyX = Float.parseFloat(requestParam[4]);
						float bodyY = Float.parseFloat(requestParam[5]);
						setBody(parentSnakeId, bodyId, bodyX, bodyY);

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
						Food food = new Food(world.getFoodId(), foodX, foodY);
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

	private void closeConn(int id) {
		for (ClientHandler client : ConnectionHandler.allUsers) {
			if (client.getUserID() == id)
				client.setInGame(false);
		}

	}

	private void setBody(int parentSnakeId, int bodyId, float bodyX, float bodyY) {
		for (ClientHandler client : ConnectionHandler.allUsers) {
			if (client.getUserID() != parentSnakeId) {
				client.writeToClient("SET" + split + "BODY" + split + parentSnakeId + split + bodyId + split + bodyX
						+ split + bodyY + "\n");
			}
		}

		for (Snake snake : world.getSnakeList()) {
			if (snake.getId() == parentSnakeId) {
				ArrayList<Body> listOfBodyParts = snake.getBodyList();
				for (Body body : listOfBodyParts) {
					if (body.getId() == bodyId) {
						body.setX(bodyX);
						body.setY(bodyY);
						return;
					}
				}
			}
		}

	}

	public void setSnake(int id, float x, float y, float angle) {
		for (ClientHandler client : ConnectionHandler.allUsers) {
			if (client.getUserID() != id) {
				client.writeToClient(
						"SET" + split + "SNAKE" + split + id + split + angle + split + x + split + y + "\n");
			}
		}

		for (Snake snake : world.getSnakeList()) {
			if (snake.getId() == id) {
				snake.setX(x);
				snake.setY(y);
				snake.setAngle(angle);
				return;
			}
		}
	}

	public void spawnFood(Food food) {
		for (ClientHandler client : ConnectionHandler.allUsers) {
			client.writeToClient(
					"NEW" + split + "FOOD" + split + world.getFoodId() + split + food.getX() + split + food.getY() + "\n");
		}
		world.getFoodList().add(food);
		world.setFoodId(world.getFoodId()+1);
		System.out.println("to client: SPAWN|"+client.getUserID()+"|"+world.getFoodId() + "|");
	}

	public void createNewBody(Body body) {
		for (ClientHandler client : ConnectionHandler.allUsers) {
			if (client.getUserID() != body.getParentSnakeId()) {
				client.writeToClient("NEW" + split + "BODY" + split + body.getParentSnakeId() + split + body.getId()
						+ split + body.getX() + split + body.getY() + "\n");
			}
		}

		for (Snake snake : world.getSnakeList()) {
			if (snake.getId() == body.getParentSnakeId()) {
				snake.getBodyList().add(body);
				return;
			}
		}
	}

	public void createNewSnake(Snake snake) {
		world.getSnakeList().add(snake);
		for(Snake s : world.getSnakeList()){
			System.out.println(s.getId());
		}
		
		for (ClientHandler client : ConnectionHandler.allUsers) {
			if (client.getUserID() != snake.getId()) {

				client.writeToClient("NEW" + split + "SNAKE" + split + snake.getId() + split + snake.getAngle() + split
						+ snake.getX() + split + snake.getY() + '\n');
			} else {
				sendWorld(client.getUserID());
			}
		}
	}

	public void sendWorld(int id) {
		for (ClientHandler client : ConnectionHandler.allUsers) {
			if (client.getUserID() == id) {
				client.setInGame(true);

				for (Food food : world.getFoodList()) {
					client.writeToClient(
							"NEW" + split + "FOOD" + split + world.getFoodId() + split + food.getX() + split + food.getY() + "\n");
					System.out.println("food id :" +food.getId());
				}
				for (Snake s : world.getSnakeList()) {
					if(s.getId() != id){
					client.writeToClient("NEW" + split + "SNAKE" + split + s.getId() + split + s.getAngle() + split
							+ s.getX() + split + s.getY() + '\n');
					System.out.println("snake id :" +s.getId());

					ArrayList<Body> bodyParts = s.getBodyList();
					for (Body b : bodyParts) {
						client.writeToClient("NEW" + split + "BODY" + split + s.getId() + split + b.getId() + split
								+ b.getX() + split + b.getY() + "\n");
						System.out.println("Body id :" + b.getId());
						}
					}
				}
			}
		}
	}

	public void deleteSnake(int snakeId) {
		for (Snake snake : world.getSnakeList()) {
			if (snakeId == snake.getId()) {
				world.getSnakeList().remove(snake);
				System.out.println("when am i called?");
				for (ClientHandler client : ConnectionHandler.allUsers) {
					client.writeToClient("DEL" + split + "SNAKE" + split + snakeId + '\n');
				}
				return;
			}
		}
	}

	public void deleteFood(int snakeId, int currFoodId) {
		for (ClientHandler client : ConnectionHandler.allUsers) {
			client.writeToClient("DEL" + split + "FOOD" + split + currFoodId + '\n');
			System.out.println("to client: DEL|"+client.getUserID()+"|"+currFoodId);
		}

		for (Food food : world.getFoodList()) {
			if (food.id == currFoodId) {
				world.getFoodList().remove(food);
				return;
			}
		}
	}

	public void login() {
		client.writeToClient("CONFIRM" + split + client.getUserID() + '\n', true);
	}
}