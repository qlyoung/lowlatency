package com.sawtoothdev.mgoa;

import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.sawtoothdev.audioanalysis.Beat;

public class SongEngine implements IGameObject {

	// misc
	private ArrayList<ISongEventListener> listeners;
	private MgoaMusic music;

	// delay functionality
	private long delayedStartTime, delayMs, delayedEngineTimer;
	private final ArrayList<Beat> delayMap;
	private int delayIndex = 0;
	
	// realtime functionality
	private long realtimeStartTime, realtimeEngineTimer;
	private final ArrayList<Beat> realtimeMap;
	private int realtimeIndex = 0;

	
	// state
	private boolean running = false;
	

	public SongEngine(BeatMap beatMap, FileHandle audioFile) {
		
		// set the map
		switch (Resources.difficulty.name) {
		case EASY:
			delayMap = beatMap.easy;
			break;
		case NORMAL:
			delayMap = beatMap.medium;
			break;
		case HARD:
			delayMap = beatMap.hard;
			break;
		default:
		case ORIGINAL:
			delayMap = beatMap.ORIGINAL;
		}
		
		realtimeMap = beatMap.ORIGINAL;
		
		this.delayMs = Resources.difficulty.ringTimeMs;
		this.music = new MgoaMusic(audioFile);
		this.music.setVolume(1f);
		
		listeners = new ArrayList<ISongEventListener>();
	}

	//core
	public void start() {
		this.running = true;
		delayedStartTime = System.currentTimeMillis();
	}
	
	@Override
	public void render(float delta) {
		
		if (running) {
			
			{// delayed
				delayedEngineTimer = System.currentTimeMillis() - delayedStartTime;
				
				if (delayedEngineTimer >= delayMs && !music.isPlaying()){
					music.play();
					realtimeStartTime = System.currentTimeMillis();
				}
				
				if (delayIndex <= delayMap.size() - 1 && delayMap.get(delayIndex).timeMs < delayedEngineTimer) {
					onBeatWarning(delayMap.get(delayIndex));
					delayIndex++;
				}
			}
			
			{// realtime
				realtimeEngineTimer = System.currentTimeMillis() - realtimeStartTime;
				
				if (realtimeIndex <= realtimeMap.size() - 1 && realtimeMap.get(realtimeIndex).timeMs < realtimeEngineTimer){
					onBeat(realtimeMap.get(realtimeIndex));
					realtimeIndex++;
				}
			}
			
		}
		
	}

	private void onBeatWarning(Beat beat) {

		for (ISongEventListener l : listeners)
			l.onBeatWarning(beat);
	}
	private void onBeat(Beat beat){
		for (ISongEventListener l : listeners)
			l.onBeat(beat);
	}

	//extraneous
	public void addListener(ISongEventListener l) {
		this.listeners.add(l);
	}

	public long getSongTime() {
		return music.getPosition();
	}
	
	public boolean isDone(){
		
		if (!music.isPlaying() && delayIndex == delayMap.size())
			return true;
		
		//System.out.println(delayIndex + " " + delayMap.size());
		//System.out.println(music.isPlaying());
		return false;
	}
}
