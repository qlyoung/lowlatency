package com.sawtoothdev.mgoa;

import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.sawtoothdev.audioanalysis.Beat;

/**
 * 
 * Plays a song and provides song event notifications in real time
 * 
 * @author albatross
 *
 */

public class SongEngine implements IGameObject {
	
	public interface ISongEventListener {
		
		public void onBeatWarning(Beat b);
		public void onBeat(Beat b);
	}

	private enum EngineState { INITIALIZED, RUNNING, DONE };

	
	// misc
	private MgoaMusic music;

	// delay
	private long delayedStartTime, delayMs, delayedEngineTimer;
	private final ArrayList<Beat> delayMap;
	private int delayIndex = 0;
	
	// realtime
	private long realtimeStartTime, realtimeEngineTimer;
	private final ArrayList<Beat> realtimeMap;
	private int realtimeIndex = 0;

	// notify
	private ArrayList<ISongEventListener> listeners;
	
	// state
	private EngineState state;
	

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
		
		state = EngineState.INITIALIZED;
	}

	//core
	public void start() {
		state = EngineState.RUNNING;
		delayedStartTime = System.currentTimeMillis();
	}
	
	@Override
	public void render(float delta) {
		
		// state update
		if (!music.isPlaying() && delayIndex == delayMap.size())
			state = EngineState.DONE;
		
		
		
		if (state == EngineState.RUNNING) {
			
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

	public void addListener(ISongEventListener l) {
		this.listeners.add(l);
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
	public long getSongTime() {
		return music.getPosition();
	}
	public boolean isDone(){
		return state == EngineState.DONE;
	}
}
