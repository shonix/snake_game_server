package snakeServer;

import java.util.ArrayList;

public class Snake {
	private float x;
	private float y;
	private float angle;
	private int id;
	private ArrayList<Body> bodyList = new ArrayList<>();
	
	public Snake(int id, float angle, float x, float y, ArrayList<Body> bodyList){
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.id = id;
		this.bodyList = bodyList;
	}
	
	public Snake(int id, float angle, float x, float y){
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.id = id;
	}

	public ArrayList<Body> getBodyList(){
		return bodyList;
	}

	public void setBodyList(ArrayList<Body>bodyList){
		this.bodyList = bodyList;
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

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
