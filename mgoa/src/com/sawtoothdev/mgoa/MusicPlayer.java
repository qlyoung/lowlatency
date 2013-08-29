package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

public class MusicPlayer {

	Music music;
	
	public MusicPlayer(FileHandle audio){
		music = Gdx.audio.newMusic(audio);
	}

	public void play() {
		music.play();
	}

	public void stop() {
		music.stop();
	}

	public boolean isPlaying() {
		return music.isPlaying();
	}

}