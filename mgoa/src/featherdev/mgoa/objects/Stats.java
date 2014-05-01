package featherdev.mgoa.objects;

public class Stats {

	private Stats() { }
	
	private static Stats instance;
	public static Stats instance(){
		if (instance == null)
			instance = new Stats();
		
		return instance;
	}
	
	public int numBeats, numBeatsHit, points;
	
}
