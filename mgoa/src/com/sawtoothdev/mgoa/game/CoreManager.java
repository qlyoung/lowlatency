package com.sawtoothdev.mgoa.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.ISongTimeListener;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.Mgoa;
import com.sawtoothdev.mgoa.Utilities;
import com.sawtoothdev.mgoa.objects.Difficulty;

public class CoreManager implements IUpdateable, IDrawable, ISongTimeListener {

	class BeatCore implements IUpdateable, IDrawable, Poolable {

		private Sprite ring, core;
		private Beat beat;
		private Color color, ec;
		private float shrinkRate;
		private float lifelength;
		private float timeBeenAlive;
		private Vector2 position;
		private boolean hit = false;
		private CoreState state;
		
		public BeatCore() {
			
			ring = Mgoa.getInstance().textures.createSprite("game/ring");
			core = Mgoa.getInstance().textures.createSprite("game/ring");
			
			ring.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			core.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			color = new Color();
			ec = new Color();
			position = new Vector2();
			
			reset();
		}

		@Override
		public void update(float delta) {

			switch (state){
			case alive:
				
				if (color.a < 1){
					color.a += delta * 2;
					if (color.a > 1)
						color.a = 1;
				}
				
				timeBeenAlive += delta;
				
				if (timeBeenAlive >= lifelength)
					state = CoreState.dying;
				else
					ring.scale(delta * -shrinkRate);
				
				break;
			case dying:
				
				if (color.a > .05)
					color.a -= (delta * 2);
				else
					state = CoreState.dead;
				
				break;
			case dead:
				break;
			default:
			}
			
			core.setColor(color);
			ring.setColor(color);
			//core.rotate(delta * 360);

		}
		public void draw(SpriteBatch batch){
			ring.draw(batch);
			core.draw(batch);
		}

		@Override
		public void reset() {
			
			ring.setScale(1.8f);
			ring.setSize(1f, 1f);
			core.setSize(1f, 1f);
			
			beat = null;
			color.set(Color.WHITE);
			ec.set(Color.WHITE);
			shrinkRate = 0;
			lifelength = 0;
			timeBeenAlive = 0;
			position.set(0, 0);
			hit = false;
		}
		public void set(Beat b, Vector2 pos, float lifetime) {
			beat = b;
			ec.set(getEnergyColor(b.energy));
			color.set(ec.r, ec.g, ec.b, 0);
			
			shrinkRate = (ring.getScaleX() - core.getScaleX()) / lifetime;
			lifelength = lifetime;
			position.set(pos);
			Vector2 spritepos = new Vector2(pos.x - ring.getWidth() / 2f, pos.y - ring.getHeight() / 2f);
			ring.setPosition(spritepos.x, spritepos.y);
			core.setPosition(spritepos.x, spritepos.y);
			ring.setOrigin(ring.getWidth() / 2f, ring.getHeight() / 2f);
			core.setOrigin(core.getWidth() / 2f, core.getHeight() / 2f);
			
			state = CoreState.alive;
		}

		public boolean checkCollision(Vector2 point){
			if (this.getHitbox().contains(point.x, point.y))
				return true;
			else
				return false;
		}
		public Accuracy onHit(long songTimeMs) {
			long diff = songTimeMs - beat.timeMs;

			state = CoreState.dying;
			hit = true;

			if (diff < -210)
				return Accuracy.ALMOST;
			else if (diff < -150)
				return Accuracy.GOOD;
			else if (diff <= -90)
				return Accuracy.EXCELLENT;
			else if (diff <= -30)
				return Accuracy.PERFECT;
			else if (diff <= 40)
				return Accuracy.STELLAR;
			else if (diff <= 120)
				return Accuracy.PERFECT;
			else if (diff <= 200)
				return Accuracy.EXCELLENT;
			else if (diff <= 280)
				return Accuracy.GOOD;
			else
				return Accuracy.ALMOST;
		}
		
		public CoreState getState(){
			return state;
		}
		public boolean beenHit(){
			return hit;
		}
		public Rectangle getHitbox() {
			return new Rectangle(position.x - .5f, position.y - .5f, 1f, 1f);
		}
		public int getScoreValue() {
			double sv = beat.energy * 100;
			sv = Math.ceil(sv);
			return (int) (sv);
		}
		public Vector2 getPosition() {
			return position;
		}
		public Color getColor() {
			return ec;
		}

	}
	class CorePool extends Pool<BeatCore> {
		
		@Override
		protected BeatCore newObject() {
			return new BeatCore();
		}

	}
	
	public static enum Accuracy { STELLAR, PERFECT, EXCELLENT, GOOD, ALMOST	};
	public enum CoreState { alive, dying, dead };
	
	// pools and cores
	CorePool corePool;
	ArrayList<BeatCore> activeCores;
	LinkedList<Beat> events;
	OrthographicCamera cam;
	Difficulty diff;
	Random random;
	EffectsManager fx;
	HashMap<String, Integer> stats;
	long songtime = 0;
	
	public CoreManager(LinkedList<Beat> beatmap, Difficulty difficulty, EffectsManager effects) {
		cam = new OrthographicCamera(10, 6);
		corePool = new CorePool();
		activeCores = new ArrayList<BeatCore>();
		events = new LinkedList<Beat>();
		diff = difficulty;
		random = new Random();
		fx = effects;
		stats = new HashMap<String, Integer>();
		stats.put("points", 0);
		stats.put("combo", 0);
		
		for (Beat b : beatmap)
			this.events.add(b);

	}

	private void spawnCore(Beat beat) {

		float[] xset = { -4, -2, 0, 2, 4 };
		float[] yset = {-1.5f, 0, 1.5f };		
		float x = xset[random.nextInt(xset.length)];
		float y = yset[random.nextInt(yset.length)];
		
		Vector2 position = new Vector2(x, y);
		
		if (activeCores.size() > 0) {
			
			boolean emptySpace = false;
			while (!emptySpace) {
				
				x = xset[random.nextInt(xset.length)];
				y = yset[random.nextInt(yset.length)];
				position.set(x, y);
				
				for (BeatCore c : activeCores) {
					
					emptySpace = !(c.getPosition().x == position.x && c.getPosition().y == position.y);
					if (!emptySpace)
						break;
				}
			}
		}
		
		BeatCore core = corePool.obtain();
		core.set(beat, position, diff.ringTimeMs / 1000f);

		activeCores.add(core);

	}
	private void onCoreHit(BeatCore core) {
		Accuracy accuracy = core.onHit(songtime);
		int points = (int) (core.getScoreValue() / (accuracy.ordinal() + 1));
		stats.put("points", stats.get("points") + points);
		
		fx.makeExplosion(core.getPosition(), core.getColor());
	}
	
	@Override
	public void update(float delta) {

		// hit detection
		if (Gdx.input.isTouched()) {

			Vector2 touchPos = Utilities.getTouchInWorld(cam);

			for (BeatCore core : activeCores) {
				if (!core.beenHit())
					if (Mgoa.TESTING) {
						if (core.getState() == CoreState.dying)
							onCoreHit(core);
					} else if (core.checkCollision(touchPos))
						onCoreHit(core);
			}
		}

		// check if we need to spawn cores
		while (events.peek() != null && songtime >= events.peek().timeMs - diff.ringTimeMs) {
			spawnCore(events.poll());
		}


		// clean up dead cores
		for (int i = 0; i < activeCores.size(); i++) {

			BeatCore c = activeCores.get(i);

			// free the dead ones
			if (c.getState() == CoreState.dead) {
				// check if the current combo has been broken
				if (!c.beenHit())
					stats.put("combo", stats.get("combo") + 1);
				activeCores.remove(c);
				corePool.free(c);
			}
		}

		// update cores
		for (BeatCore core : activeCores)
			core.update(delta);
	}
	@Override
	public void draw(SpriteBatch batch) {
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		{
			for (BeatCore core : activeCores)
				core.draw(batch);
		}
		batch.end();

	}
	@Override
	public void songtime(long time) {
		songtime = time;
	}
	public static Color getEnergyColor(float energy){
		Color color = new Color();
		
		if (energy > .75f)
			color = Color.RED;
		else if (energy > .6f)
			color = Color.ORANGE;
		else if (energy > .45f)
			color = Color.YELLOW;
		else if (energy > .3f)
			color = Color.GREEN;
		else if (energy > .15f)
			color = Color.BLUE;
		else
			color = new Color(141, 0, 255, 1);
		
		return color;
	}
	public int getPoints(){
		return stats.get("points");
	}

}