package com.sawtoothdev.mgoa;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class EffectMaker implements IGameObject {

	private class EffectsPool extends Pool<ParticleEffect> {

		@Override
		protected ParticleEffect newObject() {
			return new ParticleEffect();
		}
		
	}
	
	private EffectsPool pool = new EffectsPool();
	private LinkedList<ParticleEffect> effects = new LinkedList<ParticleEffect>();
	
	@Override
	public void render(float delta) {
		
		ParticleEffect effect;
		
		Resources.worldBatch.begin();
		{
			for (int i = 0; i < effects.size(); i++){
				effect = effects.get(i);
				
				if (effect.isComplete()) {
					effect.reset();
					effects.remove(effect);
					pool.free(effect);
				} 
				else
					effect.draw(Resources.worldBatch, delta);
			}
		}
		Resources.worldBatch.end();
		
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
}
