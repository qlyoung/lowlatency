package com.sawtoothdev.mgoa.game;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.OneShotMusicPlayer;
import com.sawtoothdev.mgoa.PrettyLights;

/**
 * Makes pretty graphics
 * 
 * @author snowdrift
 * 
 */

public class VisualsManager implements IDrawable {

	private final OrthographicCamera CAMERA;

	private final LinkedList<Beat> BEAT_EVENTS = new LinkedList<Beat>();
	private final OneShotMusicPlayer MUSIC;

	private PrettyLights lv;

	public VisualsManager(ArrayList<Beat> beatEvents, OneShotMusicPlayer music,
			OrthographicCamera camera) {

		for (Beat b : beatEvents)
			this.BEAT_EVENTS.add(b);

		this.MUSIC = music;
		this.CAMERA = camera;

		lv = new PrettyLights(CAMERA, 7);

	}

	@Override
	public void update(float delta) {
		while (BEAT_EVENTS.peek() != null
				&& MUSIC.currentTime() >= BEAT_EVENTS.peek().timeMs)
			react(BEAT_EVENTS.poll());

		lv.update(delta);
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		lv.draw(null);

	}

	public void react(Beat beat) {
		lv.react(beat);
	}

}
