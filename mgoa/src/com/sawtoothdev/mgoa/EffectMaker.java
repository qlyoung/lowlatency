package com.sawtoothdev.mgoa;

import java.util.ArrayList;

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
	private ArrayList<ParticleEffect> effects = new ArrayList<ParticleEffect>();
	
	@Override
	public void render(float delta) {
		
		for (int i = 0; i < effects.size(); i++){
			ParticleEffect effect = effects.get(i);
			
			effect.draw(Resources.spriteBatch, delta);
			
			if (effect.isComplete()){
				pool.free(effect);
				effects.remove(effect);
			}
		}
		
	}
	
	public void makeRedExplosion(Vector2 position){
		ParticleEffect effect = pool.obtain();
		effect.load(Gdx.files.internal("data/effects/blue-explosion.p"), Gdx.files.internal("data/effects/"));
		effect.setPosition(position.x, position.y);
		effect.allowCompletion();
		effects.add(effect);
		
		Gdx.app.log("fx", "Effect made");
	}
	

}
