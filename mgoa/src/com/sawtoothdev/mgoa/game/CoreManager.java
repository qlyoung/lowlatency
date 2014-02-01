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
import com.sawtoothdev.mgoa.Drawable;
import com.sawtoothdev.mgoa.Mgoa;
import com.sawtoothdev.mgoa.Updateable;
import com.sawtoothdev.mgoa.objects.Difficulty;
import com.sawtoothdev.mgoa.objects.OneShotMusicPlayer;
import com.sawtoothdev.mgoa.objects.Stats;

public class CoreManager implements Updateable, Drawable {

	private static final Texture ringtex = new Texture("textures/ring.png");
	private static final Texture coretex = new Texture("textures/core.png");
	class BeatCore implements Updateable, Drawable, Poolable {

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
			
			ring = new Sprite(ringtex);
			core = new Sprite(coretex);
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
			System.out.println(ring.getWidth());
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
			return (int) (beat.energy * 100);
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
		
		ringtex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		coretex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
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
				&& music.currentTime() >= events.peek().timeMs - diff.ringTimeMs) {
			spawnCore(events.poll());
		}


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

		float[] xset = { -4, 2, 0, 2, 4 };
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