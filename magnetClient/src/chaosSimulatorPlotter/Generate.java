package chaosSimulatorPlotter;

import java.util.Arrays;

import simulation.Logic;
import simulation.World;

public class Generate extends Thread{
	private int threadNum;
	private double[][] points;
	
	public boolean finished = false;
	public double[][] output;
	
	public Generate(double[][] points, int threadNum) {
		//super();
		this.points = points;
		this.threadNum = threadNum;
	}
	
	public void run(){
		//setup vars
		int width = 800;
		int height = 800;
		int maxTicks = 100000;
		
		int posArraySize = 1000;
		
		World world = new World(posArraySize);
		
		//set world vars
		Logic.maxForce = 1000;
		world.setHomeX(400);
		world.setHomeY(400);
		world.setDefaultCoef(10);
		world.setHomeCoef(10);
		world.setFricition(.95);
		world.setMaxStopDist(15);
		world.setHomeX(width/2);
		world.setHomeY(height/2);
		
		//set plot vars
		int fps = 60;
		
		//open file
		//PrintWriter writer = new PrintWriter("output.txt","UTF-8");
		//PrintWriter logWriter = new PrintWriter("log.txt","UTF-8");

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
					System.out.println("error: max ticks hit");
					System.out.println(world.getArmX());
					System.out.println(world.getArmX());
					System.out.println(Arrays.toString(world.getPosArrayX()));
					System.out.println(Arrays.toString(world.getPosArrayY()));
					//logWriter.println("error: max ticks hit");
					//logWriter.println(world.getArmX());
					//logWriter.println(world.getArmX());
					//logWriter.println(Arrays.toString(world.getPosArrayX()));
					//logWriter.println(Arrays.toString(world.getPosArrayY()));
					
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
			
			//print
			//System.out.println("point " +i+": "+Arrays.toString(points[i]) + " took "+tickCounter[i]+" ticks: "+Arrays.toString(finalPos[i]));
			//logWriter.println("point " +i+": "+Arrays.toString(points[i]) + " took "+tickCounter[i]+" ticks: "+Arrays.toString(finalPos[i]));
			
			//write to file
			//writer.println("["+points[i][0]+", "+points[i][1]+", "+finalPos[i][0]+", "+finalPos[i][1]+"]");
			
			//write to output
			output[i][0] = points[i][0];
			output[i][1] = points[i][1];
			output[i][2] = finalPos[i][0];
			output[i][3] = finalPos[i][1];
		}
		finished = true;
		System.out.println("Thread "+threadNum+" finished");
	}
}