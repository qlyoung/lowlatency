package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.Mgoa;
import com.sawtoothdev.mgoa.objects.LightBox.Mode;

public class MenuScreen implements Screen {

	private Stage stage = new Stage();
	Table root = new Table();
	Mgoa game;
	
	public MenuScreen(Mgoa gam) {
		game = gam;
		Gdx.input.setInputProcessor(stage);
		game.lights.setMode(Mode.IDLE);
		
		
		TextButton
			playButton = new TextButton("Play", game.skin),
			optionsButton = new TextButton("Options", game.skin),
			statsButton = new TextButton("Leaderboards", game.skin),
			creditsButton = new TextButton("Credits", game.skin);

		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dispose();
				game.setScreen(new ChooseSongScreen(game));
				super.clicked(event, x, y);
			}
		});
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dispose();
				game.setScreen(new OptionsScreen(game));
				super.clicked(event, x, y);
			}
		});

		// scene layout
		root.setFillParent(true);
		root.defaults().uniform().padBottom(30);

		root.add(playButton);
		root.row();
		root.add(optionsButton);
		root.row();
		root.add(statsButton);
		root.row();
		root.add(creditsButton);
		root.row();

		stage.addActor(root);

	}

	@Override
	public void render(float delta) {

		{// update
			game.lights.update(delta);
			stage.act(delta);
		}

		{// draw
			game.lights.draw(null);
			stage.draw();
			//audioControl.draw(MGOA.Gfx.defaultSpriteBatch);
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
