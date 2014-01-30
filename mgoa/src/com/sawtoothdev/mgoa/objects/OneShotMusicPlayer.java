package com.sawtoothdev.mgoa.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.sawtoothdev.mgoa.Pausable;

/**
 * Wrapper for Music that keeps correct time
 * 
 * @author snowdrift
 * 
 */

public class OneShotMusicPlayer implements Pausable, Disposable {

	Music music;
	private long start = 0, mark = 0;
	private boolean active = false;

	public OneShotMusicPlayer(FileHandle audio){
		music = Gdx.audio.newMusic(audio);
	}

	public void play() {
		if (active) {
			// unpause
			start += System.currentTimeMillis() - mark;
			music.play();
		}
		else {
			music.play();
			start = System.currentTimeMillis();
			active = true;
		}
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
	public void unpause() { }

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

	@Override
	public void dispose() {
		music.dispose();
	}
	
}