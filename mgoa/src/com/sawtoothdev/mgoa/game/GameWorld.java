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
	final EffectsManager fxmanager;
	final LightboxManager visualizer;
	final CoreManager coreManager;
	final HeadsUpDisplay hud;
	final Mgoa game;
	final Stats stats;
	final OrthographicCamera camera;
	
	public enum WorldState { INACTIVE, ACTIVE, FINISHING, FINISHED, PAUSED };
	private WorldState state;
	
	public GameWorld(Mgoa gam) {
		game = gam;
		stats = new Stats();
		music = new OneShotMusicPlayer(game.song.getHandle());
		camera = new OrthographicCamera(10, 6);
		fxmanager = new EffectsManager(camera);
		coreManager = new CoreManager(game.beatmap, game.difficulty, camera, music, stats, fxmanager);
		hud = new HeadsUpDisplay(game.song, game.skin, stats, game.batch);
		visualizer = new LightboxManager(game.rawmap, music, game.lights);
		
		state = WorldState.INACTIVE;
	}

	@Override
	public void update(float delta) {

		switch (state) {
		case ACTIVE:
			visualizer.update(delta);
			fxmanager.update(delta);
			coreManager.update(delta);
			hud.update(delta);

			if (state == WorldState.ACTIVE && !music.isPlaying()){
				state = WorldState.FINISHED;
				visualizer.flourish();
				hud.fadeout();
			}
			break;
			
		case FINISHING:
			
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
		fxmanager.draw(batch);
		coreManager.draw(batch);
		batch.end();
		
		hud.draw(null);
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
