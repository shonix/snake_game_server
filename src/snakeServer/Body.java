package snakeServer;

public class Body {
	float x;
	float y;
	int id;
	int parentSnakeId;
	
	public Body(int id, int parentSnakeId, float x, float y){
		this.x = x;
		this.y = y;
		this.id = id;
		this.parentSnakeId = parentSnakeId;
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

	public int getParentSnakeId() {
		return parentSnakeId;
	}

	public void setParentSnakeId(int parentSnakeId) {
		this.parentSnakeId = parentSnakeId;
	}
	
	public String toString(){
		return id + ", " + x + ", " + y;
		
		
	}
	
	
}
