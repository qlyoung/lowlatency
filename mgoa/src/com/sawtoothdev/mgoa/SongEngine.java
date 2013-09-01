package com.sawtoothdev.mgoa;

import com.badlogic.gdx.files.FileHandle;


/**
 * 
 * Plays a song and provides song event notifications in real time
 * 
 * @author albatross
 * 
 */

public class SongEngine implements IGameObject {

	public enum EngineState {
		INITIALIZED, RUNNING, DONE
	};

	private MusicPlayer music;
	private EngineState state;
	
	private long songTimer = 0;

	public SongEngine(BeatMap beatMap, FileHandle audioFile) {

		this.music = new MusicPlayer(audioFile);

		state = EngineState.INITIALIZED;
	}

	// core
	public void start() {
		state = EngineState.RUNNING;
		music.play();
	}

	@Override
	public void update(float delta) {

		// state update

		switch (state) {

		case INITIALIZED:
			break;

		case RUNNING:
			songTimer += (long)(delta * 1000f);
			
			if (!music.isPlaying())
				state = EngineState.DONE;
			
			break;

		case DONE:
			break;

		}

	}

	public long getSongTime() {
		return songTimer;
	}

	public EngineState getState() {
		return state;
	}

	public MusicPlayer getMusic() {
		return music;
	}
}
