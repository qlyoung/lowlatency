package com.sawtoothdev.mgoa;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class FxBox implements IDrawableGameObject {

	private OrthographicCamera camera;
	private EffectsPool pool = new EffectsPool();
	private LinkedList<ParticleEffect> effects = new LinkedList<ParticleEffect>();
	
	public FxBox(OrthographicCamera camera){
		this.camera = camera;
	}
	
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
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        {
        	for (ParticleEffect effect : effects)
        		effect.draw(batch, Gdx.graphics.getDeltaTime());
        }
        batch.end();
        
	}
	
	public void makeExplosion(Vector2 position, Color color){
		
		ParticleEffect effect = pool.obtain();
		
		if (color.toIntBits() == Color.MAGENTA.toIntBits())
			effect.load(Gdx.files.internal("data/effects/purple-explosion.p"), Gdx.files.internal("data/effects/"));
		else if (color.toIntBits() == Color.BLUE.toIntBits())
			effect.load(Gdx.files.internal("data/effects/blue-explosion.p"), Gdx.files.internal("data/effects/"));
		else if (color.toIntBits() == Color.GREEN.toIntBits())
			effect.load(Gdx.files.internal("data/effects/green-explosion.p"), Gdx.files.internal("data/effects/"));
		else if (color.toIntBits() == Color.YELLOW.toIntBits())
			effect.load(Gdx.files.internal("data/effects/yellow-explosion.p"), Gdx.files.internal("data/effects/"));
		else if (color.toIntBits() == Color.ORANGE.toIntBits())
			effect.load(Gdx.files.internal("data/effects/orange-explosion.p"), Gdx.files.internal("data/effects/"));
		else if (color.toIntBits() == Color.RED.toIntBits())
			effect.load(Gdx.files.internal("data/effects/red-explosion.p"), Gdx.files.internal("data/effects/"));
		
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
