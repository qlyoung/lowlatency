package featherdev.mgoa.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import featherdev.mgoa.Mgoa;

public abstract class UiScreen implements Screen {
	
	protected Mgoa game;
	protected Stage stage;
	
	public UiScreen(){
		game = Mgoa.getInstance();
		stage = new Stage();
	}
	
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	public void show() {
		stage.addAction(Actions.fadeIn(.5f));
	}
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	public void dispose(){
		stage.dispose();
	}

}
