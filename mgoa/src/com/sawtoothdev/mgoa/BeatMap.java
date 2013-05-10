package com.sawtoothdev.mgoa;

import java.util.ArrayList;

import com.sawtoothdev.audioanalysis.Beat;

public class BeatMap {

	public ArrayList<Beat> easy, medium, hard, ORIGINAL;
	
	public BeatMap(ArrayList<Beat> easy, ArrayList<Beat> medium, ArrayList<Beat> hard, ArrayList<Beat> original){
		this.easy = easy;
		this.medium = medium;
		this.hard = hard;
		this.ORIGINAL = original;
	}
	
}
