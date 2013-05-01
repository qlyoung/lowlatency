package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.sawtoothdev.audioanalysis.Beat;

public class BeatCore implements IGameObject, Poolable {

	public static enum Accuracy {
		STELLAR, PERFECT, EXCELLENT, GOOD, ALMOST, MISS, INACTIVE
	};
	private static float SYNCH_SIZE = 1/3f;
	private static float SHRINK_RATE = (1 - SYNCH_SIZE) / (0 - (Resources.difficulty.ringTimeMs / 1000f));
	private static BitmapFont coreFont = new BitmapFont();
	
	private final Sprite ring, core;
	private Vector2 position;

	private Beat beat;
	private long time;
	private String text = "0";

	private boolean complete = false;
	private boolean beenHit = false;


	public BeatCore() {
		TextureRegion reggie = new TextureRegion(new Texture("data/textures/circ.png"), 175, 175);
		ring = new Sprite(reggie);
		core = new Sprite(new Texture("data/textures/innerorb.png"));
		
	}

	// lifecycle
	public void setup(Beat beat, long timeMs, String text) {
		this.beat = beat;
		this.time = timeMs + 500;
		this.text = text;
	}
	@Override
	public void render(float delta) {

		{// update
			if (ring.getScaleX() > SYNCH_SIZE)
				ring.scale(delta * SHRINK_RATE);
			else if (ring.getScaleX() <= SYNCH_SIZE)
				core.setColor(Color.GREEN);

			if (time > 0)
				time -= delta * 1000f;
			else
				complete = true;
		}

		{// draw
			Vector2 worldPos = Resources.projectToScreen(position);
			
			core.draw(Resources.spriteBatch);
			ring.draw(Resources.spriteBatch);
			coreFont.setColor(Color.BLACK);
			coreFont.draw(Resources.spriteBatch, text, worldPos.x - 5, worldPos.y + 5);
		}
	}
	@Override
	public void reset() {

		ring.setScale(1);
		ring.setColor(Color.WHITE);
		core.setColor(Color.WHITE);

		time = Resources.difficulty.ringTimeMs;

		complete = false;
		beenHit = false;
	}

	// modifiers
	public void setPosition(Vector2 worldPos) {

		this.position = worldPos;

		Vector2 spritePosition = Resources.projectToScreen(worldPos);
		ring.setPosition(spritePosition.x - ring.getWidth() / 2f,
				spritePosition.y - ring.getHeight() / 2f);
		core.setPosition(spritePosition.x - core.getWidth() / 2f, spritePosition.y - core.getHeight() / 2f);
	}
	public Accuracy onHit(long songTimeMs) {
		long diff = songTimeMs - beat.timeMs;

		Gdx.app.log("diff", String.valueOf(diff));

		if (beenHit)
			return Accuracy.INACTIVE;

		beenHit = true;
		
		if (diff < -300){
			beenHit = false;
			return Accuracy.INACTIVE;
		}
		else if (diff < -210)
			return Accuracy.ALMOST;
		else if (diff < -150)
			return Accuracy.GOOD;
		else if (diff < -90)
			return Accuracy.EXCELLENT;
		else if (diff < -30)
			return Accuracy.PERFECT;
		else if (diff < 40)
			return Accuracy.STELLAR;
		else if (diff < 120)
			return Accuracy.PERFECT;
		else if (diff < 200)
			return Accuracy.EXCELLENT;
		else if (diff < 280)
			return Accuracy.GOOD;
		else if (diff < 400)
			return Accuracy.ALMOST;
		else
			return Accuracy.MISS;

	}

	// readers
	public boolean isComplete() {
		return complete;
	}
	public Rectangle getBoundingRectangle() {

		return new Rectangle(position.x - .25f, position.y - .25f, .5f, .5f);

	}
}
