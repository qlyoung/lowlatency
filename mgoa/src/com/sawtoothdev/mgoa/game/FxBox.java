package com.sawtoothdev.mgoa.game;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.Resources;

public class FxBox implements IUpdateable, IDrawable {

	private EffectsPool pool = new EffectsPool();
	private LinkedList<ParticleEffect> effects = new LinkedList<ParticleEffect>();
	
	@Override
	public void update(float delta) {
		
		/*
		 * Unfortunately, calling update() and draw() on a ParticleEffect
		 * instead of calling the combined update/draw method (an overload
		 * of draw()) seems to create undefined behavior; in this case
		 * a white box renders for a split second. As a workaround the update
		 * code has been moved to this class's draw(), with delta time 
		 * supplied by Gdx.graphics.getDeltaTime().
		 */
		
	}
	@Override
	public void draw(SpriteBatch batch) {
		
		// update
		for (int i = 0; i < effects.size(); i++){
			ParticleEffect effect = effects.get(i);
			
			if (effect.isComplete()) {
				effect.reset();
				effects.remove(effect);
				pool.free(effect);
			}
		}
		
		// draw
        batch.setProjectionMatrix(Resources.worldCam.combined);
        batch.begin();
        {
        	for (ParticleEffect effect : effects)
        		effect.draw(batch, Gdx.graphics.getDeltaTime());
        }
        batch.end();
        
	}
	
	public void makeExplosion(Vector2 position, Color color){
		
		ParticleEffect effect = pool.obtain();
		
		String path = "data/effects/white-explosion.p";
		
		if (color.toIntBits() == Color.MAGENTA.toIntBits())
			path = "data/effects/purple.p";
		else if (color.toIntBits() == Color.BLUE.toIntBits())
			path = "data/effects/blue.p";
		else if (color.toIntBits() == Color.GREEN.toIntBits())
			path = "data/effects/green.p";
		else if (color.toIntBits() == Color.YELLOW.toIntBits())
			path = "data/effects/yellow.p";
		else if (color.toIntBits() == Color.ORANGE.toIntBits())
			path = "data/effects/orange.p";
		else if (color.toIntBits() == Color.RED.toIntBits())
			path = "data/effects/red.p";
		
		effect.load(Gdx.files.internal(path), Gdx.files.internal("data/effects/"));
		
		effect.setPosition(position.x, position.y);
		effect.allowCompletion();
		
		effects.add(effect);
		effect.start();
		
	}
	
	private class EffectsPool extends Pool<ParticleEffect> {
		@Override
		protected ParticleEffect newObject() {
			return new ParticleEffect();
		}
	}
	
}
