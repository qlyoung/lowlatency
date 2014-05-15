package featherdev.lowlatency.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import featherdev.lowlatency.LowLatency;

public abstract class UiScreen implements Screen {
	
	protected LowLatency game;
	protected Stage stage;
	
	public UiScreen(){
		game = LowLatency.instance();
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
