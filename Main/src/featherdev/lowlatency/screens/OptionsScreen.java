package featherdev.lowlatency.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import featherdev.lowlatency.LowLatency;
import featherdev.lowlatency.subsystems.MusicPlayer;

public class OptionsScreen extends UiScreen {

	Table root;

	public OptionsScreen() {
		game = LowLatency.instance();
		stage = new Stage();
		root = new Table();
		root.setFillParent(true);

		CheckBox fullscreen = new CheckBox("  Fullscreen", game.skin);
		CheckBox music = new CheckBox("  Music", game.skin);
		TextButton backToMenu = new TextButton("Return", game.skin);
		fullscreen.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!Gdx.graphics.isFullscreen())
					Gdx.graphics.setDisplayMode(Gdx.graphics
							.getDesktopDisplayMode());
				else
					Gdx.graphics.setDisplayMode(1280, 720, false);

				game.setScreen(new OptionsScreen());
			}
		});
		music.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (MusicPlayer.instance().isPlaying())
					MusicPlayer.instance().pause();
				else
					MusicPlayer.instance().play();

				game.settings.putBoolean("music", MusicPlayer.instance().isPlaying());
				game.settings.flush();
			}
		});
		backToMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen());
				super.clicked(event, x, y);
			}
		});

		music.setChecked(MusicPlayer.instance().isPlaying());

		root.defaults().uniform().pad(10);
		root.add(new Label("Options", game.skin));
		root.row();
		if (Gdx.app.getType() != ApplicationType.Android)
			root.add(fullscreen).left();
		root.row();
		root.add(music).left();
		root.row();
		root.add(backToMenu);

		stage.addActor(root);
		stage.getRoot().getColor().a = 0;

		Gdx.input.setInputProcessor(stage);
	}

	public void render(float delta) {

		// update
		if (Gdx.input.isKeyPressed(Keys.ESCAPE))
			game.setScreen(new MenuScreen());
		
		stage.act();

		// draw
		stage.draw();

	}

}
