package chaosSimulatorPlotter;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import simulation.World;

public class MainClient {
	public static Socket socket;
	public static String remoteHost = "localhost"; //default
	public static int port = 42028; //default
	public static boolean connectionAlive;
	private static boolean running = true;
	
	private static int delay = 5000; //delay in ms for main connection loop
	private static int numThreads = 2; //default
	
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
		//read data
		try {
			File file = new File("config.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			
			remoteHost = doc.getElementsByTagName("ip").item(0).getTextContent();
			port = Integer.parseInt(doc.getElementsByTagName("port").item(0).getTextContent());
			numThreads = Integer.parseInt(doc.getElementsByTagName("numThreads").item(0).getTextContent());
			
			System.out.println("Attempting to connect to "+remoteHost+" on port "+port+" with "+numThreads+" threads");
		} catch (Exception e) {
			System.out.println("error in file reading");
		}
				
		connectionAlive = false;
		while(running) {
			if(connectionAlive) {
					World world;
					double[][] points;
					try {
						ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());
						ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
						points = (double[][]) objIn.readObject();
						System.out.println("batch received of size "+points.length);
						world  = (World) objIn.readObject();
						objOut.writeObject(runSim(world, points));
						System.out.println("batch sent to server");
					} catch (Exception e) {
						System.out.println("connecion lost");
						connectionAlive = false;
					}
			}
			else {
				connectionAlive = establishConnection();
				if (connectionAlive) {
					continue;
				}
			}
			
			Thread.sleep(delay);
		}
		
		
		
	}
	public static boolean establishConnection() {
		try {
			socket = new Socket(remoteHost, port);
		} catch (UnknownHostException e) {
			System.out.println("error unknown host exception");
			return false;
		} catch (IOException e) {
			System.out.println("connection failed");
			return false;
		} 
		System.out.println("connection successful to "+remoteHost);
		return true;
	}
	
	public static double[][] runSim(World world, double[][] points) {
		//set plot vars
		int fps = 60;
		int maxTicks = world.getMaxTicks();
		double[][] output;		
		
		//setup output
		int numPoints = points.length;
		output = new double[numPoints][4];
		
		//start timer
		long startTime = System.currentTimeMillis();
		
		//plot points
		double[][] totalPoints = points;
		//divide points
		double threadPoints[][][] = new double[numThreads][2][2];
		double pointsPerThread = (double)numPoints/numThreads;
		for(int i = 0; i < numThreads; i++) {
			int startIndex = (int)(pointsPerThread*i);
			int endIndex = (int)(pointsPerThread*(i+1));
			threadPoints[i] = Arrays.copyOfRange(totalPoints, startIndex, endIndex);
		}
		
		//create threads 
		Generate threadArray[] = new Generate[numThreads];
		for (int i = 0; i < numThreads; i++) {
			threadArray[i] = new Generate(threadPoints[i],world,i);
			threadArray[i].start();
		}		
		
		//wait for threads to finish
		boolean active = true;
		while (active) {
			active = false;
			for (int i = 0; i < numThreads; i++) {
				if (!threadArray[i].finished) {
					active = true;
				}
			}
			//sleep to use less cpu
			int sleepTime = 100; //in ms
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				System.out.println("thread sleep error");
			}
		}
		
		//collect data from threads
		int index = 0;
		for (int i = 0; i < numThreads; i++) {
			for (int j = 0; j < threadArray[i].getOutput().length; j++) {
				output[index][0] = threadArray[i].getOutput()[j][0];
				output[index][1] = threadArray[i].getOutput()[j][1];
				output[index][2] = threadArray[i].getOutput()[j][2];
				output[index][3] = threadArray[i].getOutput()[j][3];
				index++;
			}
		}
		/*Generate g = new Generate(totalPoints,world,1);
		g.start();
		try {
			g.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		output = g.getOutput();*/
		
		//end timer and print
		long endTime = System.currentTimeMillis();
		double execTime = (endTime - startTime)/(double)1000;
		double timePerPoint = execTime/numPoints;
		System.out.println("batch took: "+execTime+" s, "+timePerPoint+" per point");
		
		return output;
	}
	
	public static void setConnectionAlive(boolean val) {connectionAlive = val;}
	
	public static Socket getConnection() {return socket;}
}
