package simulation;

public class DefaultSetups {
	public DefaultSetups() {
		
	}
	
	public static void setup1(World world) {
		world.addMagnet(new Magnet(350,350,world.getDefaultCoef()));
		world.addMagnet(new Magnet(450,350,world.getDefaultCoef()));
		world.addMagnet(new Magnet(450,450,world.getDefaultCoef()));
		world.addMagnet(new Magnet(350,450,world.getDefaultCoef()));
		
		world.addMagnet(new Magnet(350,400,-world.getDefaultCoef()));
		world.addMagnet(new Magnet(450,400,-world.getDefaultCoef()));
		world.addMagnet(new Magnet(400,350,-world.getDefaultCoef()));
		world.addMagnet(new Magnet(400,450,-world.getDefaultCoef()));
	}
	
	public static void setup2(World world) {
		world.addMagnet(new Magnet(325,350,world.getDefaultCoef()));
		world.addMagnet(new Magnet(375,350,world.getDefaultCoef()));
		world.addMagnet(new Magnet(425,350,world.getDefaultCoef()));
		world.addMagnet(new Magnet(475,350,world.getDefaultCoef()));
		
		world.addMagnet(new Magnet(325,450,-world.getDefaultCoef()));
		world.addMagnet(new Magnet(375,450,-world.getDefaultCoef()));
		world.addMagnet(new Magnet(425,450,-world.getDefaultCoef()));
		world.addMagnet(new Magnet(475,450,-world.getDefaultCoef()));
	}
	
	public static void setup3(World world) {
		world.addMagnet(new Magnet(300,400,-world.getDefaultCoef()));
		world.addMagnet(new Magnet(350,450,-world.getDefaultCoef()));
		world.addMagnet(new Magnet(400,500,-world.getDefaultCoef()));
		world.addMagnet(new Magnet(450,450,-world.getDefaultCoef()));
		world.addMagnet(new Magnet(500,400,-world.getDefaultCoef()));
		world.addMagnet(new Magnet(450,350,-world.getDefaultCoef()));
		world.addMagnet(new Magnet(400,300,-world.getDefaultCoef()));
		world.addMagnet(new Magnet(350,350,-world.getDefaultCoef()));
	}
}
