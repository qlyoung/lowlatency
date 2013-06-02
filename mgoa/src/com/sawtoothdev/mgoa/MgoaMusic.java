package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

/**
 * Music class...because LibGDX's just doesn't cut it sometimes
 * 
 * @author albatross
 * 
 */

public class MgoaMusic {

	private Music music;
	private long startTime;
	
	private boolean paused = false;

	
	public MgoaMusic(FileHandle audioFile) {
		this.music = Gdx.audio.newMusic(audioFile);

		// prepare the music player
		music.play();
		music.pause();
		music.setLooping(false);
	}
	
	public void play() {
		paused = false;
		music.play();
		startTime = System.currentTimeMillis();
	}
	public void pause() {
		paused = true;
		music.pause();
	}
	public void stop() {
		music.stop();
	}
	public void setVolume(float volume){
		music.setVolume(volume);
	}

	/**
	 * @return playback position in milliseconds
	 */
	public long getPosition() {
		return System.currentTimeMillis() - startTime;
	}
	public boolean isPlaying() {
		return music.isPlaying();
	}
	public boolean isPaused() {
		return paused;
	}

}
