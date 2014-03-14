package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuScreen implements Screen {

	Stage stage;
	Table root;
	ParticleEffect background;
	OrthographicCamera bgcam;
	Mgoa game;
	
	public MenuScreen() {
		game = Mgoa.getInstance();
		stage = new Stage();
		root = new Table();
		game.lights.idle();
		
		bgcam = new OrthographicCamera();
		bgcam.setToOrtho(false);
		background = new ParticleEffect();
		background.load(Gdx.files.internal("effects/space.p"), Gdx.files.internal("effects/"));
		background.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
		
		TextButton
			playButton = new TextButton("Play", game.skin),
			optionsButton = new TextButton("Options", game.skin),
			//statsButton = new TextButton("Leaderboards", game.skin),
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

		// scene layout
		root.setFillParent(true);
		root.setSkin(game.skin);
		root.defaults().uniform().pad(0, 15, 0, 15).minWidth(200);

		root.add("Low Latency").colspan(3).row();
		root.add(" ").row().fillX();
		root.add(optionsButton);
		root.add(playButton);
		root.add(creditsButton);
		
		stage.addActor(root);
		Gdx.input.setInputProcessor(stage);
		
		background.start();
	}

	@Override
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
	@Override
	public void resize(int width, int height) {

	}
	@Override
	public void show() {
		if (game.settings.getBoolean("musicon"))
			game.menuMusic.play();
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
