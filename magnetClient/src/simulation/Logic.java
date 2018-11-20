package simulation;

import java.util.ArrayList;

public class Logic {
	static double maxForce = 1000.0; //max force for a single object
	
	public Logic() {
		
	}
	
	public static double[] acceleration(double homeCoef, double armX, double homeX, double armY, double homeY, ArrayList<Magnet> magnets) {
		//calculate force in the x direction
		double[] sum = {0,0};
		//add for home
		double deltaX = Math.abs(armX-homeX);
		double deltaY = Math.abs(armY-homeY);
		double angle;
		if (deltaX != 0) {
			angle = Math.atan(deltaY/deltaX);
		} else {
			angle = 0;
		}
		double radius = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY,2));

		double force = Math.pow(radius/100, 2)*homeCoef;
		
		if(force > maxForce) {
			//System.out.println("hit max force: "+ force);
			force = maxForce;
		}
		
		double ax = force * Math.cos(angle);
		double ay = force * Math.sin(angle);
		
		if(armX < homeX) {
			sum[0] += ax; 
		} else {
			sum[0] -= ax;
		}
		if(armY < homeY) {
			sum[1] += ay;
		} else {
			sum[1] -= ay;
		}		
		
		//System.out.println(radius);
		
		//add for magnets
		for(int i = 0; i < magnets.size(); i++) {
			Magnet m = magnets.get(i);
			
			deltaX = Math.abs(m.getXPos()-armX);
			deltaY = Math.abs(m.getYPos()-armY);
			if (deltaX != 0) {
				angle = Math.atan(deltaY/deltaX);
			} else {
				angle = 0;
			}	
			radius = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY,2));
			force = Math.pow(radius/100, -2)*m.getCoef();
			
			if(force > maxForce) {
				//System.out.println("hit max force: "+ force);
				force = maxForce;
			}
			
			ax = force * Math.cos(angle);
			ay = force * Math.sin(angle);
			if(armX < m.getXPos()) {
				sum[0] += ax;
			} else {
				sum[0] -= ax;
			}
			if(armY < m.getYPos()) {
				sum[1] += ay;
			} else {
				sum[1] -= ay;
			}
		}
		
		return sum;
	}

	
	public static void setMaxForce(double maxForce) {Logic.maxForce = maxForce;}
}
