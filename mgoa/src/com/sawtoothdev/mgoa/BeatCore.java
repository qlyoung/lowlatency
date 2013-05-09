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

	// accuracy feedback
	public static enum Accuracy {
		STELLAR, PERFECT, EXCELLENT, GOOD, ALMOST, INACTIVE
	};

	// animation
	private final float shrinkRateSecs;
	private static final float SYNCH_SIZE = 1 / 3f;

	// gfx
	private final Sprite ring, core;
	private String text = null;
	private Color c;
	private static BitmapFont font = new BitmapFont();

	// mechanical
	private Vector2 position;
	private Beat beat;

	// state
	private boolean complete = false;
	private boolean beenHit = false;
	private boolean fading = false;

	public BeatCore() {
		// delta size / delta time
		shrinkRateSecs = (1 - SYNCH_SIZE) / (0 - (Resources.difficulty.ringTimeMs / 1000f));

		ring = new Sprite(new TextureRegion(new Texture("data/textures/circ.png"), 175, 175));
		core = new Sprite(new Texture("data/textures/innerorb.png"));

		reset();
	}

	// lifecycle
	public void setup(Beat beat, String text) {
		this.beat = beat;
		this.text = text;

		Color coreColor;

		if (beat.energy > .8)
			coreColor = Color.RED;
		else if (beat.energy > .6)
			coreColor = Color.ORANGE;
		else if (beat.energy > .4)
			coreColor = Color.YELLOW;
		else if (beat.energy > .2)
			coreColor = Color.BLUE;
		else
			coreColor = Color.MAGENTA;

		this.core.setColor(coreColor);
	}

	@Override
	public void render(float delta) {

		{// update

			// approach circle update
			if (ring.getScaleX() > SYNCH_SIZE && !beenHit)
				ring.scale(delta * shrinkRateSecs);
			else
				fading = true;


			// colors and fading
			c = ring.getColor();
			if (c.a < .95f){
				
				float alpha = c.a + (delta * 3) > 1 ? 1 : c.a + (delta * 3);
				
				ring.setColor(c.r, c.g, c.b, alpha);
			}

			if (fading) {
				c = core.getColor();
				if (c.a > .05) {
					float alpha = c.a;
					alpha -= (delta * 2);
					
					// make sure the alpha isn't set below 0
					alpha = alpha < 0 ? 0 : alpha;

					core.setColor(c.r, c.g, c.b, alpha);
					c = ring.getColor();
					ring.setColor(c.r, c.g, c.b, alpha);
				}
				else if (c.a <= .05f)
					complete = true;
			}

		}

		{// draw
			Vector2 worldPos = Resources.projectToScreen(position);

			core.draw(Resources.spriteBatch);
			ring.draw(Resources.spriteBatch);
			font.setColor(Color.BLACK);
			font.draw(Resources.spriteBatch, text, worldPos.x - 5,
					worldPos.y + 5);
		}
	}

	@Override
	public void reset() {

		ring.setScale(1);
		ring.setColor(1, 1, 1, 0f);
		beat = null;
		text = null;
		complete = false;
		beenHit = false;
		fading = false;

	}

	// modifiers
	public void setPosition(Vector2 worldPos) {

		this.position = worldPos;

		Vector2 screenPosition = Resources.projectToScreen(worldPos);
		ring.setPosition(screenPosition.x - ring.getWidth() / 2f,
				screenPosition.y - ring.getHeight() / 2f);
		core.setPosition(screenPosition.x - core.getWidth() / 2f,
				screenPosition.y - core.getHeight() / 2f);

	}

	public Accuracy onHit(long songTimeMs) {
		long diff = songTimeMs - beat.timeMs;

		Gdx.app.log("diff", String.valueOf(diff));

		if (beenHit || diff < -300)
			return Accuracy.INACTIVE;
		else {
			beenHit = true;
			fading = true;			

				
			if (diff < -210)
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
				return Accuracy.INACTIVE;
		}
	}

	// readers
	public boolean isComplete() {
		return complete;
	}

	public Rectangle getBoundingRectangle() {

		return new Rectangle(position.x - .25f, position.y - .25f, .5f, .5f);

	}

	public int getScoreValue() {
		return (int) (beat.energy * 1000);
	}

	public Vector2 getPosition() {
		return position;
	}
	public boolean beenHit(){
		return beenHit;
	}
}
