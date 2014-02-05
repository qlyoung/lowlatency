package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sawtoothdev.mgoa.game.CoreManager;
import com.sawtoothdev.mgoa.game.EffectsManager;
import com.sawtoothdev.mgoa.game.HeadsUpDisplay;
import com.sawtoothdev.mgoa.game.LightboxManager;
import com.sawtoothdev.mgoa.objects.OneShotMusicPlayer;
import com.sawtoothdev.mgoa.objects.Stats;

/**
 * What are we here for, anyway?
 * 
 * @author albatross
 */

public class GameScreen implements Screen {
	
	public enum WorldState { INTRO, MAIN, OUTRO, PAUSED };
	private WorldState state;
	private int cachedState;

	final OneShotMusicPlayer music;
	final EffectsManager fxmanager;
	final LightboxManager lightboxmanager;
	final CoreManager coreManager;
	final HeadsUpDisplay hud;
	final Mgoa game;
	final Stats stats;
	final OrthographicCamera camera;
	final PausedMenu pausedMenu;
	
	public GameScreen(Mgoa gam) {
		game = gam;
		stats = new Stats();
		music = new OneShotMusicPlayer(game.song.getHandle());
		camera = new OrthographicCamera(10, 6);
		fxmanager = new EffectsManager(camera);
		coreManager = new CoreManager(game.beatmap, game.difficulty, camera, music, stats, fxmanager);
		hud = new HeadsUpDisplay(game.song, game.skin, stats, game.batch, this);
		lightboxmanager = new LightboxManager(game.rawmap, music, game.lights);
		pausedMenu = new PausedMenu(game.skin, game, this);
		
		Gdx.input.setInputProcessor(hud);
		hud.fadein(2);
		state = WorldState.INTRO;
	}

	@Override
	public void render(float delta) {

		switch (state) {

		case INTRO:
			hud.act(delta);
			hud.draw();
			
			if (hud.getAlpha() == 1.0f) {
				state = WorldState.MAIN;
				music.play();
				hud.showMessage("BEGIN FAGGOT");
			}
			
			break;
			
		case MAIN:
			lightboxmanager.update(delta);
			fxmanager.update(delta);
			coreManager.update(delta);
			hud.act(delta);
			
			lightboxmanager.draw(null);
			game.batch.begin();
			fxmanager.draw(game.batch);
			coreManager.draw(game.batch);
			game.batch.end();
			hud.draw();
			
			if (state == WorldState.MAIN && !music.isPlaying()){
				lightboxmanager.flourish();
				hud.fadeout(1);
				state = WorldState.OUTRO;
			}
			break;
			
		case OUTRO:
			hud.act();
			hud.draw();
			
			if (hud.getAlpha() == 0f)
				game.setScreen(new FinishScreen(game, stats));
			break;
			
		case PAUSED:
			lightboxmanager.draw(null);
			game.batch.begin();
			coreManager.draw(game.batch);
			fxmanager.draw(game.batch);
			game.batch.end();
			
			pausedMenu.act();
			pausedMenu.draw();
			break;
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {
		music.pause();
		cachedState = state.ordinal();
		Gdx.input.setInputProcessor(pausedMenu);
		state = WorldState.PAUSED;
	}

	@Override
	public void resume() {
		music.play();
		Gdx.input.setInputProcessor(hud);
		state = WorldState.values()[cachedState];
	}

	@Override
	public void dispose() {
		music.dispose();
		fxmanager.dispose();
		hud.dispose();
	}

}
