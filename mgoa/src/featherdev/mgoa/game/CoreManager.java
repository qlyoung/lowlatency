package featherdev.mgoa.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import featherdev.lwbd.Beat;
import featherdev.mgoa.IDrawable;
import featherdev.mgoa.IUpdateable;
import featherdev.mgoa.objects.Difficulty;
import featherdev.mgoa.objects.MusicPlayer;

public class CoreManager implements IUpdateable, IDrawable {

	
	class CorePool extends Pool<BeatCore> {
		
		protected BeatCore newObject() {
			return new BeatCore();
		}

	}
	
	CorePool corePool;
	ArrayList<BeatCore> activeCores;
	LinkedList<Beat> events;
	OrthographicCamera cam;
	Difficulty diff;
	Random random;
	EffectsManager fx;
	HashMap<String, Float> stats;
	
	public CoreManager(LinkedList<Beat> beatmap, Difficulty difficulty, EffectsManager effects) {
		cam = new OrthographicCamera(10, 6);
		corePool = new CorePool();
		activeCores = new ArrayList<BeatCore>();
		events = new LinkedList<Beat>();
		diff = difficulty;
		random = new Random();
		fx = effects;
		stats = new HashMap<String, Float>();
		stats.put("points", 0f);
		stats.put("combo", 0f);
		stats.put("avgacc", 0f);
		
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
	
	public void update(float delta) {

		// hit detection
		if (Gdx.input.isTouched()) {
			for (BeatCore core : activeCores)
				core.update(delta);
		}

		System.out.println(MusicPlayer.instance().time());
		// check if we need to spawn cores
		while (events.peek() != null && MusicPlayer.instance().time() >= events.peek().timeMs - diff.ringTimeMs) {
			spawnCore(events.poll());
		}

		// clean up dead cores
		for (int i = 0; i < activeCores.size(); i++) {

			BeatCore c = activeCores.get(i);

			// free the dead ones
			if (c.isDead()) {
				// check if the current combo has been broken
				if (!c.beenHit()){
					//break the combo
					stats.put("combo", stats.get("combo") + 1);
					
					//hurt accuracy
					float newacc = stats.get("avgacc") - .1f;
					if (newacc < 0f)
						stats.put("avgacc", 0f);
					else
						stats.put("avgacc", newacc);
				}
				activeCores.remove(c);
				corePool.free(c);
			}
		}

	}
	public void draw(SpriteBatch batch) {
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		{
			for (BeatCore core : activeCores)
				core.draw(batch);
		}
		batch.end();

	}

	public int getPoints(){
		return (int) stats.get("points").floatValue();
	}
	public float getAverageAccuracy(){
		return stats.get("avgacc");
	}

}