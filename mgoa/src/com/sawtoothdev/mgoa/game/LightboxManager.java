package com.sawtoothdev.mgoa.game;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.objects.LightBox;
import com.sawtoothdev.mgoa.objects.OneShotMusicPlayer;

/**
 * Wet layer that interfaces PrettyLights to mgoa
 * @author snowdrift
 *
 */
public class LightboxManager implements IDrawable, IUpdateable {

	private final Iterator<Beat> events;
	private final OneShotMusicPlayer music;
	Beat nextBeat;
	
	private LightBox lightbox;
	
	public LightboxManager(LinkedList<Beat> beatevents, OneShotMusicPlayer musc, LightBox lights){
		music = musc;
		events = beatevents.iterator();
		lightbox = lights;
		
		lightbox.setMode(LightBox.Mode.REACT);
		nextBeat = events.next();
	}
	
	@Override
	public void update(float delta) {
		
		while (nextBeat != null && music.currentTime() >= nextBeat.timeMs){
			Color color = CoreManager.getEnergyColor(nextBeat.energy);
			lightbox.setAllLightsColor(color);
			lightbox.jerkLights(nextBeat.energy * 30);
			lightbox.pulseLights(nextBeat.energy);
			
			nextBeat = events.hasNext() ? events.next() : null;
		}

		lightbox.update(delta);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		lightbox.draw(null);
	}
	
	public void flourish(){
		lightbox.turnOnGravity();
	}

}