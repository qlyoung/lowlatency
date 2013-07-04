package com.sawtoothdev.mgoa;

import java.util.ArrayList;

import com.sawtoothdev.audioanalysis.Beat;

public class BeatMap {

	public final ArrayList<Beat> EASY, NORMAL, HARD, ORIGINAL;
	
	public BeatMap(ArrayList<Beat> easy, ArrayList<Beat> normal, ArrayList<Beat> hard, ArrayList<Beat> original){
		this.EASY = easy;
		this.NORMAL = normal;
		this.HARD = hard;
		this.ORIGINAL = original;
	}
	
}
