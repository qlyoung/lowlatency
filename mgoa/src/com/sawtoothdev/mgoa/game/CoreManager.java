package com.sawtoothdev.mgoa.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.Mgoa;
import com.sawtoothdev.mgoa.objects.Difficulty;
import com.sawtoothdev.mgoa.objects.OneShotMusicPlayer;
import com.sawtoothdev.mgoa.objects.Stats;

public class CoreManager implements IUpdateable, IDrawable {

	class BeatCore implements IUpdateable, IDrawable, Poolable {

		// animation
		private final float shrinkRate;
		private final float SYNCH_SIZE = .45f;
		
		final float lifeLength;

		// Gfx
		private final Sprite ring, core;
		private Color corecolor;
		private float alpha = 1;

		// mechanical
		private Vector2 position;
		private Beat beat;

		// state
		private CoreState state;
		private boolean hit = false;

		public BeatCore(float lifeLength) {
			this.lifeLength = lifeLength;
			
			// delta size / delta time
			shrinkRate = -( (1 - SYNCH_SIZE) / lifeLength );

			Texture t = new Texture("textures/ring.png");
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			ring = new Sprite(t);
			ring.setSize(1.55f, 1.55f * ring.getHeight() / ring.getWidth());

			Texture y = new Texture("textures/core.png");
			y.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			core = new Sprite(y);

			state = CoreState.alive;
			
			reset();
		}

		@Override
		public void update(float delta) {

			Color c = new Color();
			
			switch (state){
			case alive:
				c = ring.getColor();
				if (c.a < .95f){
					float alpha = c.a + (delta * 3) > 1 ? 1 : c.a + (delta * 3);
					ring.setColor(c.r, c.g, c.b, alpha);
				}

				if (ring.getScaleX() > SYNCH_SIZE)
					ring.scale(delta * shrinkRate);
				else
					state = CoreState.dying;
				
				break;
			case dying:
				// fade out
				if (alpha > .05) {
					alpha -= (delta * 2);

					alpha = alpha < 0 ? 0 : alpha;
					
					c = core.getColor();
					core.setColor(c.r, c.g, c.b, alpha);
					c = ring.getColor();
					ring.setColor(c.r, c.g, c.b, alpha);
				}
				else {
					c = ring.getColor();
					state = CoreState.dead;
				}
				break;
			case dead:
			default:
			}

			core.rotate(delta * 360);

		}
		
		public void draw(SpriteBatch batch){
			core.draw(batch);
			ring.draw(batch);
		}

		// modifiers
		@Override
		public void reset() {

			ring.setOrigin(ring.getWidth() / 2f, ring.getHeight() / 2f);
			ring.setScale(1);

			core.setOrigin(core.getWidth() / 2f, core.getHeight() / 2f);
			core.setRotation(0f);
			core.setSize(.8f, .8f * core.getHeight() / core.getWidth());

			ring.setColor(1, 1, 1, 0f);
			beat = null;
			alpha = 1;
			hit = false;
			state = CoreState.alive;
		}
		public void setBeat(Beat beat) {
			this.beat = beat;
			
			this.corecolor = getEnergyColor(beat.energy);
			
			this.core.setColor(corecolor);
			this.ring.setColor(corecolor);
		}
		public void setPosition(Vector2 worldPos) {

			this.position = worldPos;

			core.setOrigin(core.getWidth() / 2f, core.getHeight() / 2f);
			ring.setPosition(worldPos.x - ring.getWidth() / 2f, worldPos.y - ring.getHeight() / 2f);
			core.setPosition(worldPos.x - core.getWidth() / 2f, worldPos.y - core.getHeight() / 2f);

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
		
		// readers
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
			return (int) (beat.energy * 100);
		}
		public Vector2 getPosition() {
			return position;
		}
		public Color getColor() {
			return corecolor;
		}
	}
	class CorePool extends Pool<BeatCore> {
		
		@Override
		protected BeatCore newObject() {
			return new BeatCore(diff.ringTimeMs / 1000f);
		}

	}
	
	//-------------------------------------------------------------
	
	public static enum Accuracy { STELLAR, PERFECT, EXCELLENT, GOOD, ALMOST	};
	public enum CoreState { alive, dying, dead };
	
	// pools and cores
	final CorePool corePool;
	final ArrayList<BeatCore> activeCores;
	final LinkedList<Beat> events;
	public int combo = 0;
	final Camera cam;
	final OneShotMusicPlayer music;
	final Difficulty diff;
	final Random random;
	final Stats stats;
	final EffectsManager fx;
	
	public CoreManager(LinkedList<Beat> beatmap, Difficulty difficulty, Camera camera, OneShotMusicPlayer musicplayer, Stats stat, EffectsManager effects) {
		cam = camera;
		corePool = new CorePool();
		activeCores = new ArrayList<BeatCore>();
		events = new LinkedList<Beat>();
		diff = difficulty;
		music = musicplayer;
		random = new Random();
		stats = stat;
		fx = effects;
		
		for (Beat b : beatmap)
			this.events.add(b);

	}

	@Override
	public void update(float delta) {

		// hit detection
		if (Gdx.input.isTouched()) {

			Vector2 touchPos = Mgoa.screenToWorld(new Vector2(Gdx.input.getX(), Gdx.input.getY()), cam);

			for (BeatCore core : activeCores) {

				if (!core.beenHit())
					if (Mgoa.TESTING) {
						if (core.getState() == CoreState.dying)
							onCoreHit(core);
					} else if (core.checkCollision(touchPos))
						onCoreHit(core);
			}
		}

		// song events
		while (events.peek() != null
				&& music.currentTime() >= events.peek().timeMs - diff.ringTimeMs)
			spawnCore(events.poll());

		// active core management
		for (int i = 0; i < activeCores.size(); i++) {

			BeatCore c = activeCores.get(i);

			// free the dead ones
			if (c.getState() == CoreState.dead) {

				// check if the current combo has been broken
				if (!c.beenHit())
					combo = 0;

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
		
		for (BeatCore core : activeCores)
			core.draw(batch);

	}

	private void spawnCore(Beat beat) {

		BeatCore core = corePool.obtain();
		core.setBeat(beat);

		Vector2 position = new Vector2();
		position.set(random.nextInt(9) - 4, random.nextInt(5) - 2);

		if (activeCores.size() > 0) {

			boolean emptySpace = false;
			while (!emptySpace) {

				position.set(random.nextInt(9) - 4, random.nextInt(5) - 2);
				for (BeatCore c : activeCores) {
					emptySpace = !(c.getPosition().x == position.x && c.getPosition().y == position.y);
					if (!emptySpace) break;
				}
			}
		}

		core.setPosition(position);

		activeCores.add(core);

	}
	private void onCoreHit(BeatCore core) {
		// register a hit event with the beat and note the accuracy
		Accuracy accuracy = core.onHit(music.currentTime());

		// record in stats
		stats.numBeatsHit++;

		// calculate the score value based on accuracy
		int divisor = accuracy.ordinal() + 1;
		int points = (int) core.getScoreValue() / divisor;

		// statistics & scoring
		combo++;
		stats.numBeatsHit++;
		stats.points += points;

		// pretty lights
		fx.makeExplosion(core.getPosition(), core.getColor());
		
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
			color = Color.MAGENTA;
		
		return color;
	}
}