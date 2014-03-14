package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CreditScreen implements Screen {
	
	Stage stage;
	
	public CreditScreen (final Mgoa game){
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		Table root = new Table();
		root.setSkin(game.skin);
		root.setFillParent(true);
		
		root.add("Designed and programmed by Quentin Young");
		root.row();
		root.add("featherdev@gmail.com");
		root.row();
		root.add(" ");
		root.row();
		root.add("Music and sound effects by John Hughes");
		root.row();
		root.add("johnhughes514@gmail.com");
		root.row();
		
		TextButton backtomenu = new TextButton("Return", game.skin);
		backtomenu.addListener(new ClickListener(){
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen());
			};
		});
		
		root.add(" ").row();
		root.add(" ").row();
		root.add(backtomenu);
		
		
		stage.addActor(root);
	}

	@Override
	public void render(float delta) {
		stage.act();
		stage.draw();

	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}
	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}
	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}
	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
