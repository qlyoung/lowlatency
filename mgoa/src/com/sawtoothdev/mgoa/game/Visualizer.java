package com.sawtoothdev.mgoa.game;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.MGOA;
import com.sawtoothdev.mgoa.OneShotMusicPlayer;
import com.sawtoothdev.mgoa.PrettyLights;

/**
 * Wet layer that interfaces PrettyLights to mgoa
 * @author snowdrift
 *
 */
public class Visualizer implements IDrawable, IUpdateable {

	private final Iterator<Beat> events;
	private final OneShotMusicPlayer music;
	Beat nextBeat;
	
	private PrettyLights pl;
	
	public Visualizer(LinkedList<Beat> beatevents, OneShotMusicPlayer music){
		pl = MGOA.temporals.lights;
		pl.setMode(PrettyLights.Mode.REACT);
		this.music = music;
		
		events = beatevents.iterator();
		nextBeat = events.next();
	}
	
	@Override
	public void update(float delta) {
		
		if (nextBeat != null)
			if (music.currentTime() >= nextBeat.timeMs) {
				pl.react(nextBeat);
				nextBeat = events.hasNext() ? events.next() : null;
			}

		pl.update(delta);
		
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		pl.draw(null);
	}

}