package com.sawtoothdev.mgoa.game;

import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.sawtoothdev.mgoa.ISongTimeListener;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.objects.OneShotMusicPlayer;

public class MusicManager implements IUpdateable, Disposable {
	
	private OneShotMusicPlayer music;
	private ArrayList<ISongTimeListener> listeners;
	
	public MusicManager(FileHandle musichandle){
		music = new OneShotMusicPlayer(musichandle);
		listeners = new ArrayList<ISongTimeListener>();
	}
	
	@Override
	public void update(float delta) {
		for (ISongTimeListener l : listeners)
			l.songtime(music.currentTime());
	}
	
	public void pause(){
		music.pause();
	}
	public void play(){
		music.play();
	}
	public void reset(){
		
	}
	public boolean isPlaying(){
		return music.isPlaying();
	}
	
	public void addListener(ISongTimeListener l){
		listeners.add(l);
	}

	@Override
	public void dispose() {
		music.dispose();
	}
	
}
