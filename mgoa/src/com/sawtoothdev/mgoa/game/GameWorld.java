package com.sawtoothdev.mgoa.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.sawtoothdev.mgoa.Drawable;
import com.sawtoothdev.mgoa.Pausable;
import com.sawtoothdev.mgoa.Updateable;
import com.sawtoothdev.mgoa.Mgoa;
import com.sawtoothdev.mgoa.objects.OneShotMusicPlayer;
import com.sawtoothdev.mgoa.objects.Stats;

public class GameWorld implements Updateable, Drawable, Pausable, Disposable {
	
	final OneShotMusicPlayer music;
	final EffectsManager fxmanager;
	final LightboxManager visualizer;
	final CoreManager coreManager;
	final HeadsUpDisplay hud;
	final Mgoa game;
	final Stats stats;
	final OrthographicCamera camera;
	
	public enum WorldState { INACTIVE, INTRO, MAIN, OUTRO, FINISHED, PAUSED };
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
		case INTRO:
			hud.update(delta);
			
			if (hud.getAlpha() == 1.0f) {
				state = WorldState.MAIN;
				music.play();
				hud.showMessage("BEGIN FAGGOT");
			}
			break;
		case MAIN:
			visualizer.update(delta);
			fxmanager.update(delta);
			coreManager.update(delta);
			hud.update(delta);

			if (state == WorldState.MAIN && !music.isPlaying()){
				visualizer.flourish();
				hud.fadeout();
				state = WorldState.OUTRO;
			}
			break;
			
		case OUTRO:
			hud.update(delta);
			
			if (hud.getAlpha() == 0f)
				state = WorldState.FINISHED;
			break;
		case FINISHED:
		case PAUSED:
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
		hud.fadein();
		state = WorldState.INTRO;
	}

	@Override
	public void pause() {
		this.state = WorldState.PAUSED;
		music.pause();
	}

	@Override
	public void unpause() {
		music.play();
		this.state = WorldState.MAIN;
	}

	public WorldState getState() {
		return state;
	}
	public Stats getStats(){
		return stats;
	}

	@Override
	public void dispose() {
		music.dispose();
		fxmanager.dispose();
		hud.dispose();
	}
}
