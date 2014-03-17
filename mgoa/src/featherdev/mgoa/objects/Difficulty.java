package featherdev.mgoa.objects;

public class Difficulty {
	
	public final long ringTimeMs;	
	public final float minBeatEnergy;
	public final String name;
	public final int scoreMultiplier;
	
	public Difficulty(long ringTimeMs, float minBeatEnergy, String name, int scoreMultiplier){
		this.name = name;
		this.ringTimeMs = ringTimeMs;
		this.minBeatEnergy = minBeatEnergy;
		this.scoreMultiplier = scoreMultiplier;
	}
}
