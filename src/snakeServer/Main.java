package snakeServer;

public class Main {
	private static World world = new World();
	public static void main(String[] args){
		try {
			new ConnectionHandler(world);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
