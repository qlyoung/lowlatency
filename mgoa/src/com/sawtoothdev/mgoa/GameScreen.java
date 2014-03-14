package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.game.BackgroundManager;
import com.sawtoothdev.mgoa.game.CoreManager;
import com.sawtoothdev.mgoa.game.EffectsManager;
import com.sawtoothdev.mgoa.game.HeadsUpDisplay;
import com.sawtoothdev.mgoa.game.MusicManager;

/**
 * What are we here for, anyway?
 * 
 * @author albatross
 */

public class GameScreen implements Screen {
	
	enum WorldState { INTRO, MAIN, OUTRO, PAUSED };
	WorldState state;
	int cachedState;
	boolean justSwitchedToMain = false;
	
	Mgoa game;
	SpriteBatch batch;

	MusicManager musicmanager;
	BackgroundManager backgroundmanager;
	CoreManager coremanager;
	EffectsManager fxmanager;
	HeadsUpDisplay hud;
	PausedMenu pausedMenu;
	
	public GameScreen() {
		game = Mgoa.getInstance();
		batch = game.batch;
		
		backgroundmanager = new BackgroundManager(game.rawmap);
		fxmanager = new EffectsManager();
		coremanager = new CoreManager(game.beatmap, game.difficulty, fxmanager);
		hud = new HeadsUpDisplay(game.song, this);
		pausedMenu = new PausedMenu(this);

		musicmanager = new MusicManager(game.song.getHandle());
		musicmanager.addListener(backgroundmanager);
		musicmanager.addListener(coremanager);

		hud.setAsInputProcessor();
		
		state = WorldState.INTRO;
	}

	@Override
	public void render(float delta) {

		switch (state) {

		case INTRO:
			if (hud.getAlpha() == 0){
				hud.fadein(1f);
				
				Dialog.fadeDuration = .5f;
				Dialog playdialog = new Dialog("", Mgoa.getInstance().skin);
				playdialog.button("Play");
				playdialog.addListener(new ClickListener(){
					@Override
					public void clicked(InputEvent event, float x, float y) {
						Dialog d = (Dialog) event.getListenerActor();
						d.cancel();
						
						state = WorldState.MAIN;
						justSwitchedToMain = true;
					}
				});
				hud.showDialog(playdialog);
			}
			
			hud.update(delta);
			hud.draw(batch);
			break;
			
		case MAIN:
			if (justSwitchedToMain){
				musicmanager.play();
				int score = game.records.readScore(game.song.getHandle());
				if (score != -1){
					String message = "Personal best: " + String.valueOf(score);
					TextBounds bounds = game.skin.getFont("naipol").getBounds(message);
					Vector2 top = new Vector2(
							Gdx.graphics.getWidth()/2f - bounds.width / 2f,
							Gdx.graphics.getHeight() - (bounds.height + 10f));
					
					hud.showMessage(message, top, .3f, .3f, 5f);
				}
				
				justSwitchedToMain = false;
			}
			
			// update
			musicmanager.update(delta);
			backgroundmanager.update(delta);
			coremanager.update(delta);
			hud.setPoints(coremanager.getPoints());
			hud.update(delta);

			// draw
			backgroundmanager.draw(batch);
			coremanager.draw(batch);
			fxmanager.render(delta, batch);
			hud.draw(game.batch);
			
			// state
			if (state == WorldState.MAIN && !musicmanager.isPlaying()){
				hud.fadeout(1);
				state = WorldState.OUTRO;
			}
			break;
			
		case OUTRO:
			hud.update(delta);
			hud.draw(game.batch);
			
			if (hud.getAlpha() == 0f)
				game.setScreen(new FinishScreen(coremanager.getPoints()));
			break;
			
		case PAUSED:
			backgroundmanager.draw(batch);
			
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
		musicmanager.pause();
		cachedState = state.ordinal();
		Gdx.input.setInputProcessor(pausedMenu);
		state = WorldState.PAUSED;
	}
	@Override
	public void resume() {
		musicmanager.play();
		hud.setAsInputProcessor();
		state = WorldState.values()[cachedState];
	}
	@Override
	public void dispose() {
		musicmanager.dispose();
		fxmanager.dispose();
		hud.dispose();
	}

}
