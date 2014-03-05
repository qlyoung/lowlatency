package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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

	final OneShotMusicPlayer music;
	
	final OrthographicCamera bgcam;
	final ParticleEffect background;
	final LightboxManager lightboxmanager;
	final EffectsManager fxmanager;
	final CoreManager coreManager;
	final HeadsUpDisplay hud;
	
	final Mgoa game;
	final Stats stats;
	final PausedMenu pausedMenu;
	
	public GameScreen(Mgoa gam) {
		game = gam;
		bgcam = new OrthographicCamera();
		bgcam.setToOrtho(false);
		stats = new Stats();
		music = new OneShotMusicPlayer(game.song.getHandle());
		fxmanager = new EffectsManager();
		coreManager = new CoreManager(game.beatmap, game.difficulty, stats, fxmanager);
		hud = new HeadsUpDisplay(game.song, game.skin, stats, this);
		lightboxmanager = new LightboxManager(game.rawmap, game.lights);
		pausedMenu = new PausedMenu(game.skin, game, this);
		background = new ParticleEffect();
		background.load(Gdx.files.internal("effects/space.p"), Gdx.files.internal("effects/"));
		background.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
		
		hud.fadein(3);
		hud.setAsInputProcessor();
		state = WorldState.INTRO;
	}

	@Override
	public void render(float delta) {

		switch (state) {

		case INTRO:
			hud.update(delta);
			hud.draw(game.batch);
			
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
					background.start();
				}
				
				state = WorldState.MAIN;
			}
			
			break;
			
		case MAIN:
			
			// update
			lightboxmanager.setSongTime(music.currentTime());
			lightboxmanager.update(delta);
			coreManager.setSongTime(music.currentTime());
			coreManager.update(delta);
			//TODO: tell hud what percent of the song is through
			hud.update(delta);

			// draw
			game.batch.setProjectionMatrix(bgcam.combined);
			game.batch.begin();
			background.draw(game.batch, delta);
			game.batch.end();
			
			lightboxmanager.draw(null);
			coreManager.draw(game.batch);
			fxmanager.render(delta, game.batch);
			hud.draw(game.batch);
			
			// state
			if (state == WorldState.MAIN && !music.isPlaying()){
				hud.fadeout(1);
				state = WorldState.OUTRO;
			}
			break;
			
		case OUTRO:
			hud.update(delta);
			hud.draw(game.batch);
			
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
		hud.setAsInputProcessor();
		state = WorldState.values()[cachedState];
	}

	@Override
	public void dispose() {
		music.dispose();
		fxmanager.dispose();
		hud.dispose();
	}

}
