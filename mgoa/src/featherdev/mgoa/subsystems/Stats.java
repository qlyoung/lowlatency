package featherdev.mgoa.subsystems;

public class Stats {

	private static Stats instance;
	public static Stats instance(){
		if (instance == null)
			instance = new Stats();
		
		return instance;
	}
	
	public int numBeats, numBeatsHit, points;
	private Stats() {
		numBeats = 0;
		numBeatsHit = 0;
		points = 0;
	}
	public void clear(){
		numBeats = 0;
		numBeatsHit = 0;
		points = 0;
	}
	
}
