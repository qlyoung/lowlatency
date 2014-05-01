package featherdev.mgoa.game;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

import featherdev.mgoa.IDrawable;
import featherdev.mgoa.IUpdateable;

public class EffectsManager implements IUpdateable, IDrawable, Disposable {

	private class EffectsPool extends Pool<ParticleEffect> {
		@Override
		protected ParticleEffect newObject() {
			return new ParticleEffect();
		}
	}
	
	private EffectsPool pool = new EffectsPool();
	private LinkedList<ParticleEffect> effects = new LinkedList<ParticleEffect>();
	private OrthographicCamera cam = new OrthographicCamera(10, 6);
	
	@Override
	public void update(float delta) {
		

		for (int i = 0; i < effects.size(); i++){
			ParticleEffect effect = effects.get(i);
			
			if (effect.isComplete()) {
				effect.reset();
				effects.remove(effect);
				pool.free(effect);
			}
			else
				effect.update(delta);
		}
		 
		
	}
	@Override
	public void draw(SpriteBatch batch) {

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        {
            for (ParticleEffect effect : effects)
            	effect.draw(batch);
        }
        batch.end();

	}
	public void render(float delta, SpriteBatch batch){
		/*
		 * Unfortunately, calling update() and draw() on a ParticleEffect
		 * instead of calling the combined update/draw method (an overload
		 * of draw()) seems to create undefined behavior; in this case
		 * a white box renders for a split second. This render() method
		 * is a workaround that utilizes the draw() overload.
		 */ 
		
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
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        {
            for (ParticleEffect effect : effects)
            	effect.draw(batch, delta);
        }
        batch.end();

	}
	
	public void makeExplosion(Vector2 position, Color color){
		
		ParticleEffect effect = pool.obtain();
		
		String path = "effects/white-explosion.p";
		
		if (color.toIntBits() == Color.MAGENTA.toIntBits())
			path = "effects/purple.p";
		else if (color.toIntBits() == Color.BLUE.toIntBits())
			path = "effects/blue.p";
		else if (color.toIntBits() == Color.GREEN.toIntBits())
			path = "effects/green.p";
		else if (color.toIntBits() == Color.YELLOW.toIntBits())
			path = "effects/yellow.p";
		else if (color.toIntBits() == Color.ORANGE.toIntBits())
			path = "effects/orange.p";
		else if (color.toIntBits() == Color.RED.toIntBits())
			path = "effects/red.p";
		
		effect.load(Gdx.files.internal(path), Gdx.files.internal("effects/"));
		
		effect.setPosition(position.x, position.y);
		effect.allowCompletion();
		
		effects.add(effect);
		effect.start();
		
	}
	
	@Override
	public void dispose() {
		for (ParticleEffect e : effects)
			e.dispose();
	}
	
}