package com.sawtoothdev.mgoa.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.GameConfiguration;
import com.sawtoothdev.mgoa.Resources;
import com.sawtoothdev.mgoa.UIResources;

public class ConfigScreen implements Screen {
	
	private Stage stage;
	private Table root;
	
	public ConfigScreen(){
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		root = new Table();
		root.setFillParent(true);
		
		Label lbl = new Label("Difficulty: ", UIResources.uiLabelStyle);
		
		String[] selections = new String[Resources.difficulties.length];
		
		for (int i = 0; i < Resources.difficulties.length; i++)
			selections[i] = Resources.difficulties[i].name;
			
		final SelectBox selector = new SelectBox(selections, UIResources.uiSelectBoxStyle);
		
		TextButton playButton = new TextButton("Play", UIResources.uiTextButtonStyle);
		playButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				finish();
				super.clicked(event, x, y);
			}
		});
		
		root.add(lbl);
		root.add(selector);
		root.row();
		root.add(playButton).colspan(2);
		
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
	
	private void finish(){
		SelectBox selector = ((SelectBox) root.getChildren().get(1));
		int index = selector.getSelectionIndex();
		
		GameConfiguration.difficulty = Resources.difficulties[index];
		
		Gdx.input.setInputProcessor(null);
		Resources.game.setScreen(new LoadScreen());
	}

}
