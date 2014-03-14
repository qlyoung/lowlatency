package com.sawtoothdev.mgoa;

import java.io.IOException;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.audioanalysis.BeatsProcessor;
import com.sawtoothdev.audioanalysis.FastBeatDetector;

/**
 * Analyzes selected song and generates the various beatmaps.
 * 
 * @author albatross
 * 
 */

public class LoadScreen implements Screen {
	
	class LoadingThread extends Thread {

		private final long MIN_BEAT_SPACE_MS = 120l;
		
		@Override
		public void run() {

			float sensitivity = FastBeatDetector.SENSITIVITY_AGGRESSIVE;

			LinkedList<Beat> rawbeats = null;

			try {
				rawbeats = FastBeatDetector.detectBeats(game.song.getHandle(), sensitivity);
			} catch (IOException e) {
				Gdx.app.log("Load Screen", e.getMessage());
				return;
			}

			LinkedList<Beat> beatmap = BeatsProcessor.removeCloseBeats(rawbeats, MIN_BEAT_SPACE_MS);
			beatmap = BeatsProcessor.dropLowBeats(beatmap, game.difficulty.minBeatEnergy);
			
			game.rawmap = rawbeats;
			game.beatmap = beatmap;
			
			onLoadComplete();
		}
	}

	Stage stage;
	Label status;
	LoadingThread loadThread;
	Mgoa game;
	
	public LoadScreen(){
		game = Mgoa.getInstance();
		loadThread = new LoadingThread();
		
		stage = new Stage();
		Table root = new Table();
		status = new Label("Loading . . .", game.skin);		
		root.add(status);
		root.setFillParent(true);
		stage.addActor(root);
		
		Gdx.input.setInputProcessor(stage);
	}
	
	private void onLoadComplete(){
		status.setText("Done.");
		stage.addAction(Actions.delay(1));
		stage.addAction(Actions.fadeOut(.2f));
	}
	
	@Override
	public void render(float delta) {
		
		// update
		game.lights.update(delta);
		stage.act();

		// draw
		game.lights.draw(null);
		stage.draw();

		// hack for changing the screen after fadeout
		if (stage.getRoot().getColor().a == 0) {
			game.menuMusic.pause();
			game.setScreen(new GameScreen());
			dispose();
		}
	}
	@Override
	public void show() {
		loadThread.start();
	}
	@Override
	public void resize(int width, int height) {

	}
	@Override
	public void hide() {

	}
	@Override
	public void pause() {

	}
	@Override
	public void resume() {

	}
	@Override
	public void dispose() {
		stage.dispose();
	}
}
