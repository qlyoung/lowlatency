package com.sawtoothdev.mgoa.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IPausable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.Mgoa;
import com.sawtoothdev.mgoa.objects.OneShotMusicPlayer;
import com.sawtoothdev.mgoa.objects.Stats;

public class GameWorld implements IUpdateable, IDrawable, IPausable {
	
	final OneShotMusicPlayer music;
	final EffectsManager fxbox;
	final Visualizer visualizer;
	final CoreManager coreManager;
	final HeadsUpDisplay hud;
	final Mgoa game;
	final Stats stats;
	final OrthographicCamera camera;
	
	public enum WorldState { INACTIVE, ACTIVE, PAUSED, FINISHED };
	private WorldState state;
	
	public GameWorld(Mgoa gam) {
		game = gam;
		stats = new Stats();
		music = new OneShotMusicPlayer(game.song.getHandle());
		camera = new OrthographicCamera(10, 6);
		fxbox = new EffectsManager(camera);
		coreManager = new CoreManager(game.beatmap, game.difficulty, camera, music, stats, fxbox);
		hud = new HeadsUpDisplay(game.song, game.skin, stats);
		visualizer = new Visualizer(game.rawmap, music, game.lights);
		
		state = WorldState.INACTIVE;
	}

	@Override
	public void update(float delta) {

		switch (state) {
		case ACTIVE:
			visualizer.update(delta);
			fxbox.update(delta);
			coreManager.update(delta);
			hud.update(delta);
			// weird behavior here, when pause() gets called libgdx
			// calls it from another thread and so it is possible that
			// the music will pause in the middle of an update tick
			// and be interpreted as a game over, thus we check if
			// the game is paused before setting game over
			//if (!music.isPlaying() && state != WorldState.PAUSED)
				//state = WorldState.FINISHED;
			if (state == WorldState.ACTIVE && !music.isPlaying())
				state = WorldState.FINISHED;
			break;
			
		case PAUSED:
		case FINISHED:
		default:
			break;
		}

	}
	@Override
	public void draw(SpriteBatch batch) {

		visualizer.draw(batch);
		
		batch.begin();
		fxbox.draw(batch);
		coreManager.draw(batch);
		hud.draw(batch);
		batch.end();
	}

	public void start() {
		state = WorldState.ACTIVE;
		music.play();
	}

	@Override
	public void pause() {
		this.state = WorldState.PAUSED;
		music.pause();
	}

	@Override
	public void unpause() {
		music.play();
		this.state = WorldState.ACTIVE;
	}

	public WorldState getState() {
		return state;
	}
	public Stats getStats(){
		return stats;
	}
}
