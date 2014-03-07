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
import com.sawtoothdev.mgoa.objects.LightBox.Mode;

public class MenuScreen implements Screen {

	ParticleEffect background;
	OrthographicCamera bgcam;
	
	private Stage stage = new Stage();
	Table root = new Table();

	Mgoa game;
	
	public MenuScreen() {
		game = Mgoa.getInstance();
		
		Gdx.input.setInputProcessor(stage);
		game.lights.setMode(Mode.IDLE);
		
		bgcam = new OrthographicCamera();
		bgcam.setToOrtho(false);
		background = new ParticleEffect();
		background.load(Gdx.files.internal("effects/slowspace.p"), Gdx.files.internal("effects/"));
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
		root.defaults().uniform().pad(0, 15, 0, 15);

		root.add("Low Latency").colspan(3).row();
		root.add(" ").row().fillX();
		root.add(playButton).minWidth(200);
		root.add(optionsButton).minWidth(200);
		root.add(creditsButton).minWidth(200);

		stage.addActor(root);

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
