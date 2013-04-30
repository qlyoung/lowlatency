package com.sawtoothdev.mgoa;

import java.util.ArrayList;

import com.sawtoothdev.audioanalysis.Beat;

public class BeatMap {

	public ArrayList<Beat> easy, medium, hard;
	
	public BeatMap(ArrayList<Beat> easy, ArrayList<Beat> medium, ArrayList<Beat> hard){
		this.easy = easy;
		this.medium = medium;
		this.hard = hard;
	}
	
}
