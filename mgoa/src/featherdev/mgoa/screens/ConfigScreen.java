package featherdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import featherdev.mgoa.Mgoa;

public class ConfigScreen implements Screen {
	
	Stage stage;
	Table root;
	SelectBox selector;
	Mgoa game;
	
	public ConfigScreen(){
		game = Mgoa.getInstance();
		stage = new Stage();
		
		String[] selections = new String[game.difficulties.length];
		for (int i = 0; i < game.difficulties.length; i++)
			selections[i] = game.difficulties[i].name;
		selector = new SelectBox(selections, game.skin);
		selector.setSelection(1);
		Label lbl = new Label("Difficulty: ", game.skin);
		TextButton playButton = new TextButton("Play", game.skin);
		playButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				int index = selector.getSelectionIndex();
				game.difficulty = game.difficulties[index];
				Gdx.input.setInputProcessor(null);
				dispose();
				game.setScreen(new LoadScreen());
				super.clicked(event, x, y);
			}
		});
		TextButton backToMenu = new TextButton("Return", game.skin);
		backToMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dispose();
				game.setScreen(new ChooseSongScreen());
				super.clicked(event, x, y);
			}
		});
		Table controlBar = new Table();
		controlBar.add(backToMenu).expandX().left().pad(10);
		Table content = new Table();
		content.defaults().pad(10);
		content.add(lbl);
		content.add(selector);
		content.row();
		content.add(playButton).colspan(2).fillX();
		
		root = new Table();
		root.add(content).expandY().center();
		root.row();
		root.add(backToMenu).pad(5);
		root.setFillParent(true);
		
		stage.addActor(root);
		stage.getRoot().getColor().a = 0;
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		game.lights.update(delta);
		stage.act();
		
		game.lights.draw(null);
		stage.draw();
		
	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void show() {
		stage.addAction(Actions.fadeIn(.5f));
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
		stage.dispose();
	}

}
