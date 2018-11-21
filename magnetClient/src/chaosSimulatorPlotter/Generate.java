package chaosSimulatorPlotter;

import java.util.Arrays;

import simulation.World;

public class Generate extends Thread{
	private int threadNum;
	private double[][] points;
	private double[][] output;
	private World defaultWorld;
	
	public boolean finished = false;
	
	public Generate(double[][] points, World world, int threadNum) {
		this.points = points;
		this.threadNum = threadNum;
		this.defaultWorld = world;
	}
	
	public void run(){
		World world = new World(defaultWorld.getPosArraySize());
		//setup world
		world.setMaxForce(defaultWorld.getMaxForce());
		world.setHomeX(defaultWorld.getHomeX());
		world.setHomeY(defaultWorld.getHomeY());
		world.setDefaultCoef(defaultWorld.getDefaultCoef());
		world.setHomeCoef(defaultWorld.getHomeCoef());
		world.setFriction(defaultWorld.getFriction());
		world.setMaxStopDist(defaultWorld.getMaxStopDist());
		world.setHomeX(defaultWorld.getHomeX());
		world.setHomeY(defaultWorld.getHomeY());
		world.setMaxTicks(defaultWorld.getMaxTicks());
		
		//set plot vars
		int fps = 60;
		int maxTicks = defaultWorld.getMaxTicks();		
		
		//setup output
		int numPoints = points.length;
		output = new double[numPoints][4];
		
		//plot points
		double[][] finalPos = new double[numPoints][2];
		int[] tickCounter = new int[numPoints];
		boolean stopped;
		for (int i = 0; i < numPoints; i++) {
			
			//for each point...
			//reset world
			world.resetWorld();
			
			//set up world
			world.setVelX(0);
			world.setVelY(0);
			world.setArmX(points[i][0]);
			world.setArmY(points[i][1]);
			
			tickCounter[i] = 0;
			
			stopped = false;
			while (!stopped) {
				world.tick(fps);
				
				tickCounter[i]++;
				
				if (tickCounter[i] > maxTicks) {
					finalPos[i][0] = -100;
					finalPos[i][1] = -100;
					stopped = true;
				}
				
				if (world.getStopped() == true) {
					stopped = true;
					finalPos[i][0] = world.getArmX();
					finalPos[i][1] = world.getArmY();
				}
			}

			//write to output
			output[i][0] = points[i][0];
			output[i][1] = points[i][1];
			output[i][2] = finalPos[i][0];
			output[i][3] = finalPos[i][1];
		}
		finished = true;
	}
	
	public double[][] getOutput() {return output;}
}