package featherdev.lowlatency.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import featherdev.lwbd.Beat;
import featherdev.lowlatency.objects.Difficulty;
import featherdev.lowlatency.objects.IDrawable;
import featherdev.lowlatency.objects.IUpdateable;
import featherdev.lowlatency.subsystems.MusicPlayer;

public class CoreManager implements IUpdateable, IDrawable {
	class CorePool extends Pool<BeatCore> {
		protected BeatCore newObject() {
			return new BeatCore();
		}
	}
	
	CorePool corePool;
	ArrayList<BeatCore> activeCores;
	LinkedList<Beat> beatmap;
	long preloadTime;
	Random random;
	
	public CoreManager(LinkedList<Beat> beatmap, Difficulty difficulty) {
		corePool = new CorePool();
		activeCores = new ArrayList<BeatCore>();
		this.beatmap = beatmap;
		preloadTime = difficulty.ringTimeMs;
		random = new Random();
	}

	private void spawnCore(Beat beat) {
		BeatCore core = corePool.obtain();
		
		// select random free position
		float[] xset = { -4, -2, 0, 2, 4 };
		float[] yset = {-1.5f, 0, 1.5f };		
		ArrayList<Vector2> freespots = new ArrayList<Vector2>();
		for (float x : xset){
			for (float y : yset)
				freespots.add(new Vector2(x, y));
		}
		for (BeatCore c : activeCores){
			Vector2 cp = c.getPosition();
			for (int i = 0; i < freespots.size(); i++){
				Vector2 spot = freespots.get(i);
				if (spot.x == cp.x && spot.y == cp.y)
					freespots.remove(spot);
			}
		}
		int selection = random.nextInt(freespots.size());
		Vector2 position = freespots.get(selection);
		
		core.init(beat, position, preloadTime / 1000f);
		activeCores.add(core);
	}
	
	public void update(float delta) {
		
		// update all cores
		for (BeatCore c : activeCores)
			c.update(delta);
		
		// check if we need to spawn cores & do so if necessary
		while (beatmap.size() > 0 && beatmap.peek().timeMs - preloadTime <= MusicPlayer.instance().time())
				spawnCore(beatmap.poll());
		
		// return dead cores to pool		
		for (int i = 0; i < activeCores.size(); i++) {
			BeatCore c = activeCores.get(i);
			if (c.isDead()) {
				activeCores.remove(c);
				corePool.free(c);
			}
		}

	}
	public void draw(SpriteBatch batch) {
		batch.setProjectionMatrix(BeatCore.cam.combined);
		batch.begin();
		{
			for (BeatCore core : activeCores)
				core.draw(batch);
		}
		batch.end();
	}

}