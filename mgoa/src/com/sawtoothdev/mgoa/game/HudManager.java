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
import com.sawtoothdev.mgoa.MainGame;
import com.sawtoothdev.mgoa.objects.Song;

class HudManager implements IUpdateable, IDrawable {

	class PointsHUD implements IDrawable, IUpdateable {

		class PointMessage implements Poolable, IUpdateable, IDrawable {
			
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
			
			@Override
			public void update(float delta) {
				float xshift = delta * 400;
				float yshift = xshift * getSlopeToTarget();
				
				position.x += position.x < target.x ? xshift : -xshift;
				position.y += position.y < target.y ? -yshift : yshift;
			}
			@Override
			public void draw(SpriteBatch batch) {
				font.draw(batch, text, position.x, position.y);
			}
			
			public float getSlopeToTarget(){
				return (target.y - position.y) / (target.x - position.x);
			}
			public boolean isNearTarget(float maxDistance){
				if (Math.abs(target.y - position.y) < maxDistance &&
					Math.abs(target.x - position.x) < maxDistance)
					return true;
				else
					return false;
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
				p.update(delta);
				
				if (p.isNearTarget(5)){
					pointPool.free(p);
					activePoints.remove(p);				
				}
			}

		}
		@Override
		public void draw(SpriteBatch batch) {
			for (PointMessage p : activePoints)
				p.draw(batch);
		}
		
		public void spawnPointMessage(int value, Vector2 position, Vector2 target){
			PointMessage pm = pointPool.obtain();
			pm.set(value, position, target);
			activePoints.add(pm);
		}

	}
	public enum HUDState { COUNTDOWN, REALTIME }
	
	public HUDState state;
	private BitmapFont messageFont = new BitmapFont(Gdx.files.internal("fonts/typeone.fnt"), false);
	private TextureRegion bottomFadeBar = new TextureRegion(new Texture("textures/fadebar_bottom.png"));
	private BitmapFont font = MainGame.Ui.skin.getFont("naipol");
	private int displayScore;
	private String message = null;
	private Song song;
	private PointsHUD pointsHud = new PointsHUD();
	private Sprite pauseButton = new Sprite(new Texture("ui/pause.png"));
	
	
	public HudManager(Song song){
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
				MainGame.game.getScreen().pause();
		}
		
		// update the score spinner
		if (displayScore < MainGame.Temporal.stats.points)
			displayScore += 17;
		else if (displayScore > MainGame.Temporal.stats.points)
			displayScore = MainGame.Temporal.stats.points;

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

		batch.setProjectionMatrix(MainGame.Gfx.screenCam.combined);
		
		// Gfx
		batch.draw(bottomFadeBar, 0, 0);
		// song data
		font.setColor(Color.LIGHT_GRAY);
		font.draw(batch, song.getArtist() + " - " + song.getTitle(), 10, 23);
		// score
		font.draw(batch, String.format("%08d", displayScore), Gdx.graphics.getWidth() - 140f, 23);
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

	public void showMessage(String msg, Color color){
		messageFont.setColor(color);
		message = msg;
	}
	public void showPoints(int points, Vector2 position){
		pointsHud.spawnPointMessage(
				points,
				MainGame.Gfx.worldToScreen(position),
				new Vector2(Gdx.graphics.getWidth() - 100,
				bottomFadeBar.getRegionHeight()));
	}

}
