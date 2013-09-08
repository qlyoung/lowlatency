package com.sawtoothdev.mgoa;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sawtoothdev.audioanalysis.Beat;

/**
 * Makes pretty graphics
 * @author snowdrift
 *
 */

public class VisualsManager implements IDrawableGameObject {

	private final LinkedList<Beat> beatEvents = new LinkedList<Beat>();
	private final OneShotMusicPlayer music;
	
	private PrettyLights lv = new PrettyLights(new OrthographicCamera(10, 6));
	
	public VisualsManager(ArrayList<Beat> beatEvents, OneShotMusicPlayer music){
		for (Beat b : beatEvents)
			this.beatEvents.add(b);
		
		this.music = music;
	}
	
	@Override
	public void update(float delta) {
		while (music.currentTime() >= beatEvents.peek().timeMs)
			react(beatEvents.poll());
		
		lv.update(delta);
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		lv.draw(null);

	}
	
	public void react(Beat beat){
		lv.react(beat);
	}

}
