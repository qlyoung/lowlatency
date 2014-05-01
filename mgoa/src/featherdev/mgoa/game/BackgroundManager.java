package featherdev.mgoa.game;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import featherdev.lwbd.Beat;
import featherdev.mgoa.IDrawable;
import featherdev.mgoa.IUpdateable;
import featherdev.mgoa.Mgoa;
import featherdev.mgoa.objects.LightBox;
import featherdev.mgoa.objects.MusicPlayer;

public class BackgroundManager implements IUpdateable, IDrawable {

	/**
	 * Wet layer that interfaces PrettyLights to mgoa
	 * 
	 * @author snowdrift
	 */
	private class LightboxManager implements IDrawable, IUpdateable {

		private final Iterator<Beat> events;
		private Beat nextBeat;
		private LinkedList<Color> window = new LinkedList<Color>();
		private LightBox lightbox;
		
		// decrease for more alive color changes
		private int WINDOW_SIZE = 3;
		
		public LightboxManager(LinkedList<Beat> beatevents, LightBox lights) {
			events = beatevents.iterator();
			lightbox = lights;

			lightbox.standby();
			nextBeat = events.next();
		}

		public void update(float delta) {

			while (nextBeat != null && MusicPlayer.instance().time() >= nextBeat.timeMs) {
				Color ec = BeatCore.getEnergyColor(nextBeat.energy);
				
				// calculate the color average of the last 5 beats
				if (window.size() == WINDOW_SIZE)
					window.removeFirst();
				if (ec.toIntBits() != Color.MAGENTA.toIntBits())
					window.addLast(ec);
				

				float r = 0, g = 0, b = 0;
				if (window.size() > 0){
					for (Color c : window) {
						r += c.r;
						g += c.g;
						b += c.b;
					}
					r /= window.size();
					g /= window.size();
					b /= window.size();
				}
				Color c = new Color(r, g, b, 1);
				
				lightbox.setAllLightsColor(c);
				lightbox.jerkLights(nextBeat.energy * 30);
				lightbox.pulseLights(nextBeat.energy * 10);

				nextBeat = events.hasNext() ? events.next() : null;
			}


			lightbox.update(delta);
		}
		public void draw(SpriteBatch batch) {
			lightbox.draw(null);
		}
	}
	
	LightboxManager lbm;
	private ParticleEffect fountain;
	OrthographicCamera screencam;
	
	boolean FOUNTAIN_ON;
	
	public BackgroundManager(LinkedList<Beat> beats){
		LightBox lb = Mgoa.getInstance().lights;
		fountain = new ParticleEffect();
		fountain.load(Gdx.files.internal("effects/space.p"), Gdx.files.internal("effects/"));
		fountain.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
		
		lbm = new LightboxManager(beats, lb);
		FOUNTAIN_ON = true;
		
		screencam = new OrthographicCamera();
		screencam.setToOrtho(false);
	}
	
	@Override
	public void update(float delta) {
		lbm.update(delta);
	}
	public void draw(SpriteBatch batch) {
		if (FOUNTAIN_ON){
			batch.setProjectionMatrix(screencam.combined);
			batch.begin();
			{
				fountain.draw(batch, Gdx.graphics.getDeltaTime());
			}
			batch.end();
		}

		lbm.draw(null);
	}
	public void numlights(int n){
		lbm.lightbox.setNumLights(lbm.lightbox.getColor(), n);
	}
	public void setFountainOn(boolean on){
		this.FOUNTAIN_ON = on;
	}
}
