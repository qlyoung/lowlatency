package com.sawtoothdev.mgoa.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.sawtoothdev.mgoa.IPausable;

public class OneShotMusicPlayer implements IPausable {

	Music music;
	private long start = 0, mark = 0;
	private boolean active = false;

	public OneShotMusicPlayer(FileHandle audio){
		music = Gdx.audio.newMusic(audio);
	}

	public void play() {
		if (active)
			unpause();
		else {
			music.play();
			start = System.currentTimeMillis();
			active = true;
		}
	}

	public void stop() {
		music.stop();
		start = 0;
		mark = 0;
		active = false;
	}
	
	@Override
	public void pause() {
		music.pause();
		mark = System.currentTimeMillis();
	}
	
	/**
	 * call play() instead
	 */
	@Override
	public void unpause() {
		start += System.currentTimeMillis() - mark;
		music.play();
	}

	public boolean isPlaying() {
		return music.isPlaying();
	}
	
	public void setVolume(float volume){
		music.setVolume(volume);
	}
	
	public long currentTime(){
		if (active)
			if (music.isPlaying())
				return System.currentTimeMillis() - start;
			else
				return mark - start;
		else
			return 0;
	}
	
}