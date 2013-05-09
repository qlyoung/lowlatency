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
	private boolean playing = false;

	
	public MgoaMusic(FileHandle audioFile) {
		this.music = Gdx.audio.newMusic(audioFile);

		// prepare the music player
		music.play();
		music.pause();
	}
	
	public void play() {
		playing = true;
		music.play();
		startTime = System.currentTimeMillis();
	}
	public void pause() {
		music.pause();
		playing = false;
	}
	public void stop() {
		music.stop();
		playing = false;
	}
	public void setVolume(float volume){
		this.music.setVolume(volume);
	}

	/**
	 * @return playback position in milliseconds
	 */
	public long getPosition() {
		return System.currentTimeMillis() - startTime;
	}
	public boolean isPlaying() {
		return playing;
	}
	public boolean isPaused() {
		return paused;
	}

}
