package com.sawtoothdev.mgoa.game;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.mgoa.Drawable;
import com.sawtoothdev.mgoa.Updateable;
import com.sawtoothdev.mgoa.objects.LightBox;
import com.sawtoothdev.mgoa.objects.OneShotMusicPlayer;

/**
 * Wet layer that interfaces PrettyLights to mgoa
 * 
 * @author snowdrift
 * 
 */
public class LightboxManager implements Drawable, Updateable {

	private final Iterator<Beat> events;
	private final OneShotMusicPlayer music;
	Beat nextBeat;
	LinkedList<Color> window = new LinkedList<Color>();
	Random random = new Random();
	// decrease for more alive color changes, increase
	// for more relaxed changes
	private int WINDOW_SIZE = 3;

	private LightBox lightbox;

	public LightboxManager(LinkedList<Beat> beatevents,
			OneShotMusicPlayer musc, LightBox lights) {
		music = musc;
		events = beatevents.iterator();
		lightbox = lights;

		lightbox.setMode(LightBox.Mode.REACT);
		nextBeat = events.next();
	}

	@Override
	public void update(float delta) {

		while (nextBeat != null && music.currentTime() >= nextBeat.timeMs) {
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

	public void flourish() {
	}
}