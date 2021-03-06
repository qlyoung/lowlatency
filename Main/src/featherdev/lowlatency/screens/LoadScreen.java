package featherdev.lowlatency.screens;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import featherdev.lowlatency.subsystems.LightTank;
import featherdev.lwbd.*;
import featherdev.lwbd.decoders.*;
import featherdev.lwbd.processing.*;
import featherdev.lowlatency.LowLatency;
import featherdev.lowlatency.subsystems.Holder;
import featherdev.lowlatency.subsystems.MusicPlayer;

/**
 * music processing & map gen
 */

public class LoadScreen extends UiScreen {
	
	class LoadingThread extends Thread {

		@Override
		public void run() {

			LinkedList<Beat> rawbeats = null;
			LinkedList<Beat> beatmap  = null;
			
			FileHandle audiofile = Holder.song.getHandle();
			LwbdDecoder decoder = null;
			
			String extension = audiofile.extension().toLowerCase();

            Gdx.app.log("[+]", "Initializing audio analysis system");
			if (extension.contains("mp3"))
				decoder = new GdxMp3Decoder(audiofile);
			else if (extension.contains("ogg"))
				decoder = new GdxOggDecoder(audiofile);
			else
				onLoadComplete(false);

			rawbeats = BeatDetector.detectBeats(decoder, BeatDetector.SENSITIVITY_AGGRESSIVE);
			beatmap  = BeatsProcessor.thinBeats(rawbeats, 120);
			beatmap  = BeatsProcessor.dropWeakBeats(beatmap, Holder.difficulty.minBeatEnergy);
			
			Holder.rawmap = rawbeats;
			Holder.beatmap = beatmap;
			Gdx.app.log("[+]", "Audio analysis complete.");

			onLoadComplete(true);
		}
	}

	Label status;
	LoadingThread loadThread;
	enum LoadScreenState { LOADING, DYING }
	LoadScreenState state;
	
	public LoadScreen(){
		game = LowLatency.instance();
		loadThread = new LoadingThread();
		
		stage = new Stage();
		Table root = new Table();
		status = new Label("Loading, Please Wait . . .", game.skin);
		root.add(status).left();
		root.setFillParent(true);
		stage.addActor(root);
		stage.getRoot().getColor().a = 0;
		state = LoadScreenState.LOADING;
		
		Gdx.input.setInputProcessor(stage);
	}
	
	private void onLoadComplete(boolean success){
		if (success){
			status.setText("Done.");
			stage.addAction(Actions.sequence(Actions.delay(1), Actions.fadeOut(1f)));
			state = LoadScreenState.DYING;
		}
		else
			System.out.println("Load failed.");
	}
	
	public void render(float delta) {
		
		switch (state){
		case LOADING:
			// update
			LightTank.instance().update(delta);
			stage.act(delta);

			// draw
			LightTank.instance().draw(null);
			stage.draw();
			break;
		case DYING:
			// update
			LightTank.instance().update(delta);
			stage.act(delta);

			// draw
			LightTank.instance().draw(null);
			stage.draw();
			
			if (stage.getRoot().getColor().a == 0){
				MusicPlayer.instance().pause();
				game.setScreen(new ReadyScreen());
				dispose();
			}
			break;
		}
	}
	public void show() {
		loadThread.start();
		super.show();
	}

    public void dispose() {
        super.dispose();
    }
}
