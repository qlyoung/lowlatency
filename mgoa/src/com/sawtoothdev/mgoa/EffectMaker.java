package com.sawtoothdev.mgoa;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
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
		
		Resources.screenBatch.begin();
		{
			for (int i = 0; i < effects.size(); i++){
				effect = effects.get(i);
				
				if (effect.isComplete()) {
					effect.reset();
					effects.remove(effect);
					pool.free(effect);
				} 
				else
					effect.draw(Resources.screenBatch, delta);
			}
		}
		Resources.screenBatch.end();
		
	}
	
	public void makeBlueExplosion(Vector2 position){
		
		position = Resources.projectToScreen(position);
		
		ParticleEffect effect = pool.obtain();
		
		effect.load(Gdx.files.internal("data/effects/blue-explosion.p"), Gdx.files.internal("data/effects/"));
		
		effect.setPosition(position.x, position.y);
		effect.allowCompletion();
		
		effects.add(effect);
		effect.start();
		
		Gdx.app.log("fx", "Effect made");
		Gdx.app.log("fx", String.valueOf(effects.size()));
	}
	

}
