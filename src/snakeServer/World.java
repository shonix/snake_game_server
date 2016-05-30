package snakeServer;

import java.util.ArrayList;

public class World {
	private ArrayList<Food> foodList = new ArrayList<>();
	private ArrayList<Snake> snakeList = new ArrayList<>();
	private int foodId = 0;
	
	
	
	public int getFoodId() {
		return foodId;
	}
	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}
	public ArrayList<Food> getFoodList() {
		return foodList;
	}
	public void setFoodList(ArrayList<Food> foodList) {
		this.foodList = foodList;
	}
	public ArrayList<Snake> getSnakeList() {
		return snakeList;
	}
	public void setSnakeList(ArrayList<Snake> snakeList) {
		this.snakeList = snakeList;
	}
	
	
}
