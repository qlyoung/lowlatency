package featherdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import featherdev.mgoa.Mgoa;

public class ReadyScreen extends UiScreen {

	Dialog playdialog;
	
	public ReadyScreen(){
		Gdx.input.setInputProcessor(stage);
		
		Dialog.fadeDuration = .5f;
		playdialog = new Dialog("", Mgoa.instance().skin);
		playdialog.text("  Touch anywhere  ");
		playdialog.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				Mgoa.instance().setScreen(new GameScreen());
			};
		});
	}

	public void render(float delta) {
		Mgoa.instance().lights.update(delta);
		Mgoa.instance().lights.draw(null);
		stage.act();
		stage.draw();

	}
	
	public void show() {
		playdialog.show(stage);
		super.show();
	}

}
