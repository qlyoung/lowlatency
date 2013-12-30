package com.sawtoothdev.mgoa.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.MGOA;

/**
 * background visuals manager
 * 
 * @author snowdrift
 * 
 */

public class Visuals implements IUpdateable, IDrawable {

	public final Visualizer visualizer;
	public final FxBox box;
	
	private final GameWorld GW;

	public Visuals(GameWorld gw) {
		this.GW = gw;
		
		visualizer = new Visualizer(MGOA.temporals.rawmap, GW.music);
		box = new FxBox();

	}

	@Override
	public void update(float delta) {
		visualizer.update(delta);
		box.update(delta);
	}

	@Override
	public void draw(SpriteBatch batch) {
		visualizer.draw(batch);
		box.draw(batch);
	}

}
