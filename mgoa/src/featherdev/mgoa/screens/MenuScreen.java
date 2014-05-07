package featherdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import featherdev.mgoa.subsystems.MusicPlayer;

public class MenuScreen extends UiScreen {

	ParticleEffect background;
	OrthographicCamera bgcam;
	Table root;
	
	public MenuScreen() {
		root = new Table();
		game.lights.idle();
		
		FileHandle musicpath;
		switch (Gdx.app.getType()) {
		case Android:
			// work around Music's inability to load Internal files on Android
			Gdx.files.internal("audio/title.mp3").copyTo( Gdx.files.local("title.mp3"));
			musicpath = Gdx.files.local("title.mp3");
			break;
		case Desktop:
			musicpath = Gdx.files.internal("audio/title.mp3");
			break;
		default:
			musicpath = null;
		}
		
		if (!MusicPlayer.instance().isPlaying()){
			MusicPlayer.instance().load(musicpath);
			MusicPlayer.instance().setLooping(true);
		}
		
		bgcam = new OrthographicCamera();
		bgcam.setToOrtho(false);
		background = new ParticleEffect();
		background.load(Gdx.files.internal("effects/space.p"), Gdx.files.internal("effects/"));
		background.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
		background.start();		
		TextButton
			playButton = new TextButton("Play", game.skin),
			optionsButton = new TextButton("Options", game.skin),
			creditsButton = new TextButton("Credits", game.skin);

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
		
		game.lights.update(5);
	}

	public void render(float delta) {

		{// update
			game.lights.update(delta);
			stage.act(delta);
		}

		{// draw
			game.batch.setProjectionMatrix(bgcam.combined);
			game.batch.begin();
			background.draw(game.batch, delta);
			game.batch.end();
			game.lights.draw(null);
			stage.draw();
		}

	}
	public void show() {
		if (game.settings.contains("music")){
			if (game.settings.getBoolean("music") && !MusicPlayer.instance().isPlaying())
				MusicPlayer.instance().play();
		}
		else
			MusicPlayer.instance().stop();
		
		super.show();
	}



}
