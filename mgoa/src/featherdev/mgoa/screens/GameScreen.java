package featherdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import featherdev.mgoa.Mgoa;
import featherdev.mgoa.game.BackgroundManager;
import featherdev.mgoa.game.CoreManager;
import featherdev.mgoa.game.HeadsUpDisplay;
import featherdev.mgoa.objects.PausedMenu;
import featherdev.mgoa.subsystems.Effects;
import featherdev.mgoa.subsystems.MusicPlayer;
import featherdev.mgoa.subsystems.ScoreRecords;
import featherdev.mgoa.subsystems.Stats;

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
	int cachedState;
	boolean justSwitchedState = false;

	Mgoa game;
	SpriteBatch batch;

	BackgroundManager backgroundmanager;
	CoreManager coremanager;
	HeadsUpDisplay hud;
	PausedMenu pausedMenu;

	public GameScreen() {
		game = Mgoa.instance();
		batch = game.batch;

		backgroundmanager = new BackgroundManager(game.rawmap);
		coremanager = new CoreManager(game.beatmap, game.difficulty);
		hud = new HeadsUpDisplay();
		hud.setSongInfo(game.song.getArtist(), game.song.getTitle());
		pausedMenu = new PausedMenu(this);
		Stats.instance().clear();
		MusicPlayer.instance().load(game.song.getHandle());
		hud.setAsInputProcessor();

		state = WorldState.MAIN;
		justSwitchedState = true;
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
			break;

		case OUTRO:
			hud.update(delta);
			hud.draw(game.batch);

			if (hud.getAlpha() == 0f)
				game.setScreen(new FinishScreen());
			break;

		case PAUSED:
			backgroundmanager.draw(batch);

			pausedMenu.act();
			pausedMenu.draw();
			break;
		}
	}

	public void resize(int width, int height) {
	}

	public void show() {
		hud.fadein(1f);
		MusicPlayer.instance().play();
		int score = ScoreRecords.instance().readScore(game.song.getHandle());
		if (score != -1) {
			String message = "Personal best: " + String.valueOf(score);
			TextBounds bounds = game.skin.getFont("naipol").getBounds(message);
			Vector2 top = new Vector2(Gdx.graphics.getWidth() / 2f
					- bounds.width / 2f, Gdx.graphics.getHeight()
					- (bounds.height + 10f));

			hud.showMessage(message, top, .3f, 5f);
		}
	}

	public void hide() {
	}

	public void pause() {
		MusicPlayer.instance().pause();
		cachedState = state.ordinal();
		Gdx.input.setInputProcessor(pausedMenu);
		state = WorldState.PAUSED;
	}

	public void resume() {
		MusicPlayer.instance().play();
		hud.setAsInputProcessor();
		state = WorldState.values()[cachedState];
	}

	public void dispose() {
		MusicPlayer.instance().dispose();
		Effects.instance().dispose();
		hud.dispose();
	}

}
