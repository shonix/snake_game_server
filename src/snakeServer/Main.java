package snakeServer;

public class Main {
	public static void main(String[] args){
		try {
			new ConnectionHandler();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
