package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Audio {
	
	public final Music menuMusic;
	
	public Audio(){
		Gdx.files.internal("data/audio/title.mp3").copyTo(Gdx.files.external(".tmp/title.mp3"));
		menuMusic = Gdx.audio.newMusic(Gdx.files.external(".tmp/title.mp3"));
		
		menuMusic.setLooping(true);
		menuMusic.setVolume(1f);
	}

}
