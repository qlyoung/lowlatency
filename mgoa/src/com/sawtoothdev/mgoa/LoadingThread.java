package com.sawtoothdev.mgoa;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.audioanalysis.BeatsProcessor;
import com.sawtoothdev.audioanalysis.FastBeatDetector;

public class LoadingThread extends Thread {

	@Override
	public void run() {

		float sensitivity = FastBeatDetector.SENSITIVITY_AGGRESSIVE - .5f;

		ArrayList<Beat> rawbeats = null;

		try {
			rawbeats = FastBeatDetector.detectBeats(GameConfiguration.song.getHandle(), sensitivity);
		} catch (IOException e) {
			Gdx.app.log("Load Screen", e.getMessage());
			return;
		}

		// maybe process the beats somehow so they
		// have a minimum energy level?

		ArrayList<Beat> beatmap = 
				BeatsProcessor.removeCloseBeats(rawbeats, GameConfiguration.difficulty.minBeatSpace);
		
		GameConfiguration.rawmap = rawbeats;
		GameConfiguration.beatmap = beatmap;
	}
}