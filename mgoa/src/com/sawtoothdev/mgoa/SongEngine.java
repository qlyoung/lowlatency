package com.sawtoothdev.mgoa;

import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.sawtoothdev.audioanalysis.Beat;

public class SongEngine implements GameObject {

	long startTime;

	private long delayMs;
	private long engineTimer;
	private MgoaMusic music;

	private ArrayList<Beat> map;
	int index = 0;

	private boolean running = false;

	private final ArrayList<SongEventListener> listeners;

	public SongEngine(ArrayList<Beat> map, long delayMs, FileHandle audioFile) {
		this.map = map;
		this.delayMs = delayMs;
		this.music = new MgoaMusic(audioFile);
		
		listeners = new ArrayList<SongEventListener>();
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
		System.out.println("Beat time: " + mo.timeMs + "  Realtime: "
				+ music.getPosition());

		for (SongEventListener l : listeners)
			l.onBeat(mo);
	}

	public void addListener(SongEventListener l) {
		this.listeners.add(l);
	}

	public void start() {
		this.running = true;
		startTime = System.currentTimeMillis();
	}
}
