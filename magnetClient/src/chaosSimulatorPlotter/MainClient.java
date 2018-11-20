package chaosSimulatorPlotter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import simulation.World;

public class MainClient{
	public static Socket socket;
	public static String remoteHost = "10.2.22.159";
	public static int port = 42022;
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		System.out.println("Starting...");
		socket = new Socket(remoteHost, port);
		socket.setKeepAlive(true);
		World world;
		double[][] points;
		ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());
		ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
		points = (double[][]) objIn.readObject();
		world  = (World) objIn.readObject();
		objOut.writeObject(runSim(world, points));
		socket.close();
		
		
		
	}
	
	public static double[][] runSim(World world, double[][] points) {
		//set plot vars
		int maxTicks = 100000;
		int fps = 60;
		double[][] output;
		
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
		return output;
	}
}
