package simulation;

import java.io.Serializable;

public class Magnet implements Serializable{
	private int xPos;
	private int yPos;
	private double coef; //magnet coefficient 
	
	static int totalMagnets = 0;
	static int maxMagnets = 1000000;

	
	public Magnet(int xPos, int yPos, double coef) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.coef = coef;

		totalMagnets++;
	}
	
	//getters
	public int getXPos() { return this.xPos; }
	public int getYPos() { return this.yPos; }
	public double getCoef() { return this.coef; }
	
	//setters
	public void setxPos(int xPos) {	this.xPos = xPos;}
	public void setyPos(int yPos) {	this.yPos = yPos;}
}
