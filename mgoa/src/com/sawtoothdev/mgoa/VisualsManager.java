package com.sawtoothdev.mgoa;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sawtoothdev.audioanalysis.Beat;

/**
 * Makes pretty graphics
 * 
 * @author snowdrift
 * 
 */

public class VisualsManager implements IDrawableGameObject {

	private final OrthographicCamera CAMERA;
	
	private final LinkedList<Beat> BEATEVENTS = new LinkedList<Beat>();
	private final OneShotMusicPlayer MUSIC;

	private PrettyLights lv;
	

	public VisualsManager(ArrayList<Beat> BEATEVENTS, OneShotMusicPlayer MUSIC, OrthographicCamera camera) {
		
		for (Beat b : BEATEVENTS)
			this.BEATEVENTS.add(b);

		this.MUSIC = MUSIC;
		this.CAMERA = camera;
		
		lv = new PrettyLights(CAMERA);
		
	}

	@Override
	public void update(float delta) {
			while (BEATEVENTS.peek() != null && MUSIC.currentTime() >= BEATEVENTS.peek().timeMs)
				react(BEATEVENTS.poll());

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
