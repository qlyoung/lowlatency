package featherdev.mgoa.objects;

public class Stopwatch {
	private long starttime, stoptime;
	private boolean running, paused;
	
	public Stopwatch(){
		reset();
	}
	
	public void start(){
		if (running)
			return;
		if (paused)
			starttime += (System.currentTimeMillis() - stoptime);
		if (!running && !paused)
			starttime = System.currentTimeMillis();
		
		running = true;
		paused = false;
	}
	public void pause(){
		stoptime = System.currentTimeMillis();
		running = false;
		paused = true;
	}
	public void reset(){
		starttime = 0;
		stoptime = 0;
		running = false;
		paused = false;
	}
	public long time(){
		if (running)
			return System.currentTimeMillis() - starttime;
		else if (paused)
			return stoptime - starttime;
		else
			return 0l;
	}
}