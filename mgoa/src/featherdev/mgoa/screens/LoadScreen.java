package featherdev.mgoa.screens;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import featherdev.lwbd.Beat;
import featherdev.lwbd.BeatDetector;
import featherdev.lwbd.BeatsProcessor;
import featherdev.lwbd.LwbdDecoder;
import featherdev.lwbd.decoders.gdx.GdxMp3Decoder;
import featherdev.lwbd.decoders.gdx.GdxOggDecoder;
import featherdev.mgoa.Mgoa;

/**
 * music processing & map gen
 * 
 */

public class LoadScreen implements Screen {
	
	class LoadingThread extends Thread {

		@Override
		public void run() {

			LinkedList<Beat> rawbeats = null;
			LinkedList<Beat> beatmap  = null;
			
			FileHandle audiofile = game.song.getHandle();
			LwbdDecoder decoder = null;
			
			String extension = audiofile.extension().toLowerCase();
			
			if (extension.contains("mp3"))
				decoder = new GdxMp3Decoder(audiofile);
			else if (extension.contains("ogg"))
				decoder = new GdxOggDecoder(audiofile);
			else
				onLoadComplete(false);
			
			System.out.print("loading...");
			rawbeats = BeatDetector.detectBeats(decoder, BeatDetector.SENSITIVITY_AGGRESSIVE);
			System.out.print("done.");
			beatmap  = BeatsProcessor.thinBeats(rawbeats, 120);
			beatmap  = BeatsProcessor.dropWeakBeats(beatmap, game.difficulty.minBeatEnergy);
			
			for (Beat b : beatmap)
				System.out.println(b.timeMs);
			
			game.rawmap = rawbeats;
			game.beatmap = beatmap;
			
			onLoadComplete(true);
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
		stage.getRoot().getColor().a = 0;
		
		Gdx.input.setInputProcessor(stage);
	}
	
	private void onLoadComplete(boolean success){
		if (success){
			status.setText("Done.");
			stage.addAction(Actions.delay(1));
			stage.addAction(Actions.fadeOut(.2f));
		}
		else {
			System.out.println("Load failed.");
			return;
		}
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
		stage.getRoot().addAction(Actions.fadeIn(.5f));
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
