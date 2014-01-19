package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

public class Audio {
	
	public final Music menuMusic;
	
	public Audio(){
		
		FileHandle musicpath;
		
		// work around Music's inability to load 'internal' files on Android
		switch (Gdx.app.getType()){
		case Android:
			Gdx.files.internal("data/audio/title.mp3").copyTo(Gdx.files.local("title.mp3"));
			musicpath = Gdx.files.local("title.mp3");
			break;
		case Desktop:
			musicpath = Gdx.files.internal("title.mp3");
			break;
		default:
			musicpath = null;
		}
		
		menuMusic = Gdx.audio.newMusic(musicpath);
		menuMusic.setLooping(true);
		menuMusic.setVolume(1f);
		
		if (!MainGame.settings.contains("bgmusic") || MainGame.settings.getBoolean("bgmusic"))
			menuMusic.play();
	}

}
