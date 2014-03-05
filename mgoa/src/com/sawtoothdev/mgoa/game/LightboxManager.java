package com.sawtoothdev.mgoa.game;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.objects.LightBox;

/**
 * Wet layer that interfaces PrettyLights to mgoa
 * 
 * @author snowdrift
 * 
 */
public class LightboxManager implements IDrawable, IUpdateable {

	private final Iterator<Beat> events;
	private Beat nextBeat;
	private LinkedList<Color> window = new LinkedList<Color>();
	private LightBox lightbox;
	
	// decrease for more alive color changes
	private int WINDOW_SIZE = 3;
	
	private long songtime;
	

	public LightboxManager(LinkedList<Beat> beatevents, LightBox lights) {
		events = beatevents.iterator();
		lightbox = lights;

		lightbox.setMode(LightBox.Mode.REACT);
		nextBeat = events.next();
	}

	@Override
	public void update(float delta) {

		while (nextBeat != null && songtime >= nextBeat.timeMs) {
			Color ec = CoreManager.getEnergyColor(nextBeat.energy);
			
			// calculate the color average of the last 5 beats
			if (window.size() == WINDOW_SIZE)
				window.removeFirst();
			if (ec.toIntBits() != Color.MAGENTA.toIntBits())
				window.addLast(ec);
			

			float r = 0, g = 0, b = 0;
			if (window.size() > 0){
				for (Color c : window) {
					r += c.r;
					g += c.g;
					b += c.b;
				}
				r /= window.size();
				g /= window.size();
				b /= window.size();
			}
			Color c = new Color(r, g, b, 1);
			
			lightbox.setAllLightsColor(c);
			lightbox.jerkLights(nextBeat.energy * 30);
			lightbox.pulseLights(nextBeat.energy * 10);

			nextBeat = events.hasNext() ? events.next() : null;
		}


		lightbox.update(delta);
	}

	@Override
	public void draw(SpriteBatch batch) {
		lightbox.draw(null);
	}
	public void setSongTime(long millis){
		this.songtime = millis;
	}
}