package com.sawtoothdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.MGOA;

public class ConfigScreen implements Screen {
	
	private Stage stage;
	private Table root;
	
	SelectBox selector;
	
	public ConfigScreen(){
		
		stage = new Stage();
		root = new Table();
		
		root.setFillParent(true);
		Gdx.input.setInputProcessor(stage);
		
		String[] selections = new String[MGOA.util.difficulties.length];
		for (int i = 0; i < MGOA.util.difficulties.length; i++)
			selections[i] = MGOA.util.difficulties[i].name;
			
		selector = new SelectBox(selections, MGOA.ui.uiSelectBoxStyle);
		Label lbl = new Label("Difficulty: ", MGOA.ui.uiLabelStyle);
		TextButton playButton = new TextButton("Play", MGOA.ui.uiTextButtonStyle);
		TextButton backToMenu = new TextButton("MAIN MENU", MGOA.ui.uiTextButtonStyle);
		playButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				finish();
				super.clicked(event, x, y);
			}
		});
		backToMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				MGOA.game.setScreen(new MenuScreen());
				super.clicked(event, x, y);
			}
		});
		
		root.add(backToMenu).expandY().expandX().top().left().colspan(2);
		root.row();
		root.add(lbl).right();
		root.add(selector).left();
		root.row();
		root.add(playButton).colspan(2).center().padTop(30).padBottom(30);
		
		stage.addActor(root);
	}

	@Override
	public void render(float delta) {
		MGOA.temporals.lights.update(delta);
		stage.act();
		
		MGOA.temporals.lights.draw(null);
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
	
	private void finish(){
		int index = selector.getSelectionIndex();
		
		MGOA.temporals.difficulty = MGOA.util.difficulties[index];
		
		Gdx.input.setInputProcessor(null);
		MGOA.game.setScreen(new LoadScreen());
	}

}
