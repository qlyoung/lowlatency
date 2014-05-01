package featherdev.mgoa.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

/**
 * Wrapper for Music that keeps correct time
 * 
 * @author snowdrift
 * 
 */

public class MusicPlayer implements Disposable {
	
	class Stopwatch {
		private long starttime, stoptime;
		private boolean running, paused;
		
		public Stopwatch(){
			starttime = 0;
			stoptime = 0;
			running = false;
		}
		
		public void start(){
			if (paused)
				starttime += (System.currentTimeMillis() - stoptime);
			else if (!running)
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
	enum PlayerState { STOPPED, PLAYING, PAUSED }
	
	private static MusicPlayer instance;
	public static MusicPlayer instance() {
		if (instance == null)
			instance = new MusicPlayer();
		
		return instance;
	}
	

	Music music;
	Stopwatch stopwatch;
	PlayerState state;
	
	private MusicPlayer() { 
		stopwatch = new Stopwatch();
	}
	
	public void load(FileHandle audio){
		if (music != null){
			music.stop();
			music.dispose();
		}
		music = Gdx.audio.newMusic(audio);
		music.play();
		music.stop();
		state = PlayerState.STOPPED;
	}
	
	public void play() {
		switch (state){
			case STOPPED:
				music.play();
				stopwatch.start();
				break;
			case PLAYING:
				break;
			case PAUSED:
				music.play();
				stopwatch.start();
		}
		
		state = PlayerState.PLAYING;
	}
	public void pause() {
		if (!music.isPlaying())
			return;
		
		music.pause();
		stopwatch.pause();
		state = PlayerState.PAUSED;
	}
	public void stop() {
		music.stop();
		stopwatch.reset();
		state = PlayerState.STOPPED;
	}
	public void setVolume(float volume){
		music.setVolume(volume);
	}
	public void setLooping(boolean val) {
		music.setLooping(val);
	}
	public boolean isPlaying() {
		return music.isPlaying();
	}
	public long time(){
		return stopwatch.time();
	}

	@Override
	public void dispose() {
		music.dispose();
	}
	

}