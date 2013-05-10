package com.sawtoothdev.mgoa;

import com.sawtoothdev.audioanalysis.Beat;

public interface ISongEventListener {
	
	public void onBeatWarning(Beat b);
	public void onBeat(Beat b);
}
