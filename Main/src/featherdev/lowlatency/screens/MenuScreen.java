package featherdev.lowlatency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import featherdev.lowlatency.Utilities;
import featherdev.lowlatency.subsystems.Holder;
import featherdev.lowlatency.subsystems.LightTank;
import featherdev.lowlatency.subsystems.MusicPlayer;
import featherdev.lowlatency.subsystems.Stats;

public class MenuScreen extends UiScreen {

	Table root;
    Viewport viewport;

	public MenuScreen() {
        viewport = new ScreenViewport();

		// ui
		TextButton playButton = new TextButton("Play", game.skin), optionsButton = new TextButton(
				"Options", game.skin), creditsButton = new TextButton(
				"Credits", game.skin);
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dispose();
				game.setScreen(new ChooseSongScreen());
				super.clicked(event, x, y);
			}
		});
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dispose();
				game.setScreen(new OptionsScreen());
				super.clicked(event, x, y);
			}
		});
		creditsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new CreditScreen(game));
				super.clicked(event, x, y);
			}
		});
		root = new Table();
		root.setFillParent(true);
		root.setSkin(game.skin);
		root.defaults().uniform().pad(0, 15, 0, 15).minWidth(200);
		root.add("__low_latency__").colspan(3).row();
		root.add(" ").row().fillX();
		root.add(optionsButton);
		root.add(playButton);
		root.add(creditsButton);
		stage.addActor(root);
		stage.getRoot().getColor().a = 0;
		Gdx.input.setInputProcessor(stage);

		// background
		LightTank.instance().setup(6, Utilities.getRandomColor(), true);

		// globals
		Holder.clear();
        Stats.clear();

		// music
		FileHandle musicpath;
		switch (Gdx.app.getType()) {
		case Android:
			// work around Music's inability to load Internal files on Android
			Gdx.files.internal("audio/title.mp3").copyTo(
					Gdx.files.local("title.mp3"));
			musicpath = Gdx.files.local("title.mp3");
			break;
		case Desktop:
			musicpath = Gdx.files.internal("audio/title.mp3");
			break;
		default:
			musicpath = null;
		}
		if (!MusicPlayer.instance().isPlaying()) {
			MusicPlayer.instance().load(musicpath);
			MusicPlayer.instance().setLooping(true);
		}

	}

	public void render(float delta) {
        LightTank.instance().update(delta);
        LightTank.instance().draw(null);
        stage.act(delta);
        stage.draw();
	}

	public void show() {
		if (game.settings.contains("music")) {
			if (game.settings.getBoolean("music")
					&& !MusicPlayer.instance().isPlaying())
				MusicPlayer.instance().play();
		} else
			MusicPlayer.instance().stop();

		super.show();
	}

    public void resize(int width, int height) {
        LightTank.instance().resize(width, height);
        super.resize(width, height);
    }

    public void dispose() {
        super.dispose();
    }
}
