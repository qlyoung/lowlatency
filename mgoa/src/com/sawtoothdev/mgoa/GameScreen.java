package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Vector2;
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

	final Texture background;
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
		background = new Texture(Gdx.files.internal("textures/background.png"));
		
		Gdx.input.setInputProcessor(hud);
		hud.present();
		state = WorldState.INTRO;
	}

	@Override
	public void render(float delta) {

		switch (state) {

		case INTRO:
			hud.act(delta);
			hud.draw();
			
			if (hud.getAlpha() == 1.0f) {
				music.play();
				
				Vector2 center = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
				hud.showMessage("Begin", center, .3f, 1f, .1f);
				
				int score = game.records.readScore(game.song.getHandle());
				if (score != -1){
					String message = "Personal best: " + String.valueOf(score);
					TextBounds bounds = game.skin.getFont("naipol").getBounds(message);
					Vector2 top = new Vector2(
							Gdx.graphics.getWidth()/2f - bounds.width / 2f,
							Gdx.graphics.getHeight() - (bounds.height + 10f));
					
					hud.showMessage(message, top, .3f, .3f, 5f);
				}
				
				state = WorldState.MAIN;
			}
			
			break;
			
		case MAIN:

			lightboxmanager.update(delta);
			coreManager.update(delta);
			hud.act(delta);
			
			OrthographicCamera c = new OrthographicCamera();
			c.setToOrtho(false);
			game.batch.setProjectionMatrix(c.combined);
			game.batch.begin();
			game.batch.draw(background, Gdx.graphics.getWidth()/2f - background.getWidth()/2f, 0);
			game.batch.end();
			lightboxmanager.draw(null);
			game.batch.begin();
			fxmanager.render(game.batch);
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
