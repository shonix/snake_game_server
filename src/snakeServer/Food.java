package snakeServer;

public class Food {
	float x;
	float y;
	int id;
	
	public Food(int id, float x, float y){
		this.x = x;
		this.y = y;
		this.id = id;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
