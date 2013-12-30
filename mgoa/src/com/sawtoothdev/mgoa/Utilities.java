package com.sawtoothdev.mgoa;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Heavyweights, globals and misfit objects
 * 
 * @author albatross
 */

public class Utilities {
	
	public Utilities(){
		
		difficulties = new Difficulty[3];
		difficulties[0] = new Difficulty(800, 250, "Relaxed", 1);
		difficulties[1] = new Difficulty(650, 150, "Normal", 2);
		difficulties[2] = new Difficulty(600, 120, "Altered", 3);
		
		random = new Random();
		defaultFont = new BitmapFont();
		debugOverlay = new DebugOverlay();
		
	}
	
	// miscellaneous
	public final Random random;
	public final BitmapFont defaultFont;
	public final DebugOverlay debugOverlay;
	public final Difficulty difficulties[];



}
