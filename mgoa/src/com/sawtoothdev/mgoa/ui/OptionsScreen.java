package com.sawtoothdev.mgoa.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.sawtoothdev.mgoa.Resources;

public class OptionsScreen implements Screen {

	private Stage stage = new Stage();
	private final Table root = new Table();

	public OptionsScreen() {
		Gdx.input.setInputProcessor(stage);
		stage.addActor(root);
		root.setFillParent(true);
		
		root.add(new Label("---Graphics Options---", UIResources.uiLabelStyle));
		root.row();
		
	}

	@Override
	public void render(float delta) {

		{// update
			if (Gdx.input.isKeyPressed(Keys.ESCAPE))
				Resources.game.setScreen(new MenuScreen());
			stage.act();
		}
		
		{// draw
			stage.draw();
		}

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

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
		
	}

}
