package com.sawtoothdev.mgoa;

import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.sawtoothdev.audioanalysis.Beat;

public class SongEngine implements IGameObject {

	private long startTime;

	private long delayMs;
	private long engineTimer;
	private MgoaMusic music;

	private ArrayList<Beat> map;
	private int index = 0;
	private final ArrayList<ISongEventListener> listeners;

	private boolean running = false;
	

	public SongEngine(ArrayList<Beat> map, long delayMs, FileHandle audioFile) {
		this.map = map;
		this.delayMs = delayMs;
		this.music = new MgoaMusic(audioFile);
		
		listeners = new ArrayList<ISongEventListener>();
	}

	//core
	public void start() {
		this.running = true;
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public void render(float delta) {
		
		if (running) {
			
			engineTimer = System.currentTimeMillis() - startTime;

			if (engineTimer >= delayMs && !music.isPlaying())
				music.play();
				

			if (map.get(index) != null && map.get(index).timeMs < engineTimer) {
				onBeat(map.get(index));
				index++;
			}
		}
		
	}

	private void onBeat(Beat mo) {

		for (ISongEventListener l : listeners)
			l.onBeat(mo);
	}

	//extraneous
	public void addListener(ISongEventListener l) {
		this.listeners.add(l);
	}

	public long getSongTime() {
		return music.getPosition();
	}
}
