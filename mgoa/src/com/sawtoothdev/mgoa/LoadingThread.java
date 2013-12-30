package com.sawtoothdev.mgoa;

import java.io.IOException;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.audioanalysis.BeatsProcessor;
import com.sawtoothdev.audioanalysis.FastBeatDetector;

public class LoadingThread extends Thread {

	@Override
	public void run() {

		float sensitivity = FastBeatDetector.SENSITIVITY_AGGRESSIVE - .5f;

		LinkedList<Beat> rawbeats = null;

		try {
			rawbeats = FastBeatDetector.detectBeats(MGOA.temporals.song.getHandle(), sensitivity);
		} catch (IOException e) {
			Gdx.app.log("Load Screen", e.getMessage());
			return;
		}

		// drop beats under .01 energiez
		LinkedList<Beat> beatmap = BeatsProcessor.dropLowBeats(rawbeats, .01f);
		beatmap = BeatsProcessor.removeCloseBeats(rawbeats, MGOA.temporals.difficulty.minBeatSpace);
		
		MGOA.temporals.rawmap = rawbeats;
		MGOA.temporals.beatmap = beatmap;
	}
}