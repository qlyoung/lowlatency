package com.sawtoothdev.mgoa.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.MGOA;
import com.sawtoothdev.mgoa.Song;

class HUD implements IUpdateable, IDrawable {

	public class PointsHUD implements IDrawable, IUpdateable {

		class PointMessage implements Poolable {
			
			public String text;
			public Vector2 target = new Vector2();
			public Vector2 position = new Vector2();
			
			public void set(int value, Vector2 position, Vector2 target){
				this.text = "+" + value;
				this.position.set(position);
				this.target.set(target);
			}
			
			@Override
			public void reset() {
				text = null;
				target.set(0, 0);
				position.set(0, 0);
			}
			
			public float getSlopeToTarget(){
				return (target.y - position.y) / (target.x - position.x);
			}
		}
		
		Pool<PointMessage> pointPool = new Pool<PointMessage>(){

			@Override
			protected PointMessage newObject() {
				return new PointMessage();
			}
			
		};
		ArrayList<PointMessage> activePoints = new ArrayList<PointsHUD.PointMessage>();
		
		@Override
		public void update(float delta) {
			
			for (int i = 0; i < activePoints.size(); i++){
				PointMessage p = activePoints.get(i);
				// if we're within five pixels of our target
				if (Math.abs(p.position.x - p.target.x) < 5 &&
					Math.abs(p.position.y - p.target.y) < 5){
					
					pointPool.free(p);
					activePoints.remove(p);				
				}
				else {
					float xshift = delta * 400;
					float yshift = xshift * p.getSlopeToTarget();
					
					p.position.x += p.position.x < p.target.x ? xshift : -xshift;
					p.position.y += p.position.y < p.target.y ? -yshift : yshift;
				}
			}

		}

		@Override
		public void draw(SpriteBatch batch) {
			for (PointMessage p : activePoints)
				MGOA.ui.uiFnt.draw(batch, p.text, p.position.x, p.position.y);
		}
		
		public void spawnPoints(int value, Vector2 position, Vector2 target){
			PointMessage pm = pointPool.obtain();
			pm.set(value, position, target);
			activePoints.add(pm);
		}

	}
	public enum HUDState { COUNTDOWN, REALTIME }
	
	public HUDState state;
	private BitmapFont messageFont = new BitmapFont(Gdx.files.internal("data/fonts/typeone.fnt"), false);
	private TextureRegion bottomFadeBar = new TextureRegion(new Texture("data/textures/fadebar_bottom.png"));
	private int displayScore;
	private String message = null;
	private Song song;
	private PointsHUD pointsHud = new PointsHUD();
	private Sprite pauseButton = new Sprite(new Texture("data/textures/ui/pause.png"));
	
	
	public HUD(Song song){
		pauseButton.setPosition(5, Gdx.graphics.getHeight() - pauseButton.getHeight() - 5);
		this.song = song;
		messageFont.setColor(Color.WHITE);
	}
	
	@Override
	public void update(float delta) {

		// check pause
		if (Gdx.input.justTouched()) {
			Vector2 lastTouch =
					new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			Rectangle spriteBox = pauseButton.getBoundingRectangle();

			if (spriteBox.contains(lastTouch.x, lastTouch.y))
				MGOA.game.getScreen().pause();
		}
		
		// update the score spinner
		if (displayScore < MGOA.temporals.stats.points)
			displayScore += 17;
		else if (displayScore > MGOA.temporals.stats.points)
			displayScore = MGOA.temporals.stats.points;

		// update point messages
		pointsHud.update(delta);
		
		// fade messages
		Color c = messageFont.getColor();
		if (c.a > 0f) {
			float alpha = (c.a - delta) < 0 ? 0 : c.a - delta;
			messageFont.setColor(c.r, c.g, c.b, alpha);
		}

	}
	@Override
	public void draw(SpriteBatch batch){

		batch.setProjectionMatrix(MGOA.gfx.screenCam.combined);
		
		// gfx
		batch.draw(bottomFadeBar, 0, 0);
		// song data
		MGOA.ui.uiFnt.setColor(Color.CYAN);
		MGOA.ui.uiFnt.draw(batch, song.getArtist() + " - " + song.getTitle(), 10, 23);
		// score
		MGOA.ui.uiFnt.draw(batch, String.format("%08d", displayScore), Gdx.graphics.getWidth() - 140f, 23);
		// messages
		if (message != null){
			float length = messageFont.getBounds(message).width;
			messageFont.draw(batch, message, Gdx.graphics.getWidth() / 2f - (length / 2f), Gdx.graphics.getHeight() / 2f);
		}
		// points
		pointsHud.draw(batch);
		// controls
		pauseButton.draw(batch);

	}

	public void showMessage(String message, Color color){
		messageFont.setColor(color);
		this.message = message;
	}
	public void showPoints(int points, Vector2 position){
		Vector2 screenPosition = MGOA.gfx.projectToScreen(position);
		pointsHud.spawnPoints(points, screenPosition, new Vector2(Gdx.graphics.getWidth() - 100, bottomFadeBar.getRegionHeight()));
	}

}
