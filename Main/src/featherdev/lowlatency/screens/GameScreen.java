package featherdev.lowlatency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import featherdev.lowlatency.LowLatency;
import featherdev.lowlatency.game.BackgroundManager;
import featherdev.lowlatency.game.CoreManager;
import featherdev.lowlatency.subsystems.Effects;
import featherdev.lowlatency.subsystems.HeadsUpDisplay;
import featherdev.lowlatency.subsystems.Holder;
import featherdev.lowlatency.subsystems.MusicPlayer;
import featherdev.lowlatency.subsystems.ScoreRecords;
import featherdev.lowlatency.subsystems.Stats;

/**
 * What are we here for, anyway?
 * 
 * @author albatross
 */

public class GameScreen implements Screen {

	enum WorldState {
		MAIN, OUTRO, PAUSED
	};

	WorldState state;
	LowLatency game;
	SpriteBatch batch;
	BackgroundManager backgroundmanager;
	CoreManager coremanager;
	Stage pausedMenu;
	HeadsUpDisplay hud;

	public GameScreen() {
		game = LowLatency.instance();
		batch = game.batch;

		backgroundmanager = new BackgroundManager(Holder.rawmap);
		coremanager = new CoreManager(Holder.beatmap, Holder.difficulty);
		hud = HeadsUpDisplay.instance();
		hud.setSongInfo(Holder.song.getArtist(), Holder.song.getTitle());
		
		pausedMenu = new Stage();
		Skin skin = LowLatency.instance().skin;
		TextButton resume = new TextButton("Resume", skin);
		TextButton quitToMenu = new TextButton("Quit to Main Menu", skin);
		resume.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				resume();
				super.clicked(event, x, y);
			}
		});
		quitToMenu.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				Gdx.input.setInputProcessor(null);
				LowLatency.instance().setScreen(new MenuScreen());
				super.clicked(event, x, y);
			}
		});
		Table table = new Table(skin);
		table.defaults().pad(10);
		table.setFillParent(true);
		table.add("Options");
		table.row();
		table.add(resume);
		table.row();
		table.add(quitToMenu);
		pausedMenu.addActor(table);
		
		Stats.instance().clear();
		MusicPlayer.instance().load(Holder.song.getHandle());

		state = WorldState.MAIN;
	}

	public void render(float delta) {
		switch (state) {

		case MAIN:
			// update
			backgroundmanager.update(delta);
			coremanager.update(delta);
			hud.update(delta);

			// draw
			backgroundmanager.draw(batch);
			coremanager.draw(batch);
			Effects.instance().render(delta, batch);
			hud.draw(game.batch);

			// state
			if (state == WorldState.MAIN && !MusicPlayer.instance().isPlaying()) {
				hud.fadeout(1);
				state = WorldState.OUTRO;
			}
			if (Gdx.input.justTouched()){
				System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());
				
				if (Gdx.input.getX() < 30 && Gdx.input.getY() < 40){
					System.out.println("pausing");
					MusicPlayer.instance().pause();
					state = WorldState.PAUSED;
					Gdx.input.setInputProcessor(pausedMenu);
				}
			}
			break;


        case PAUSED:
            backgroundmanager.draw(batch);
            pausedMenu.act();
            pausedMenu.draw();
            break;

		case OUTRO:
			hud.update(delta);
			hud.draw(game.batch);

			if (hud.getAlpha() == 0f)
				game.setScreen(new FinishScreen());
			break;
        }
    }

	public void resize(int width, int height) { }

	public void show() {
		hud.fadein(1f);
		MusicPlayer.instance().play();
		int score = ScoreRecords.instance().readScore(Holder.song.getHandle());
		if (score != -1) {
			String message = "Personal best: " + String.valueOf(score);
			TextBounds bounds = game.skin.getFont("naipol").getBounds(message);
			Vector2 top = new Vector2(Gdx.graphics.getWidth() / 2f
					- bounds.width / 2f, Gdx.graphics.getHeight()
					- (bounds.height + 10f));

			hud.showMessage(message, top, 5f, .2f);
		}
	}

	public void hide() { }

	public void pause() {
        // only allow pausing if they're actually playing
        if (state == WorldState.MAIN){
            MusicPlayer.instance().pause();
            Gdx.input.setInputProcessor(pausedMenu);
            state = WorldState.PAUSED;
        }
	}

	public void resume() {
		MusicPlayer.instance().play();
		state = WorldState.MAIN;
        Gdx.input.setInputProcessor(null);
	}

	public void dispose() {
		MusicPlayer.instance().dispose();
		Effects.instance().dispose();
		hud.dispose();
	}

}
