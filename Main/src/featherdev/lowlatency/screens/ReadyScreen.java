package featherdev.lowlatency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import featherdev.lowlatency.LowLatency;
import featherdev.lowlatency.subsystems.Holder;
import featherdev.lowlatency.subsystems.LightTank;

public class ReadyScreen extends UiScreen {

	Dialog playdialog;
	
	public ReadyScreen(){
		Gdx.input.setInputProcessor(stage);
		
		Dialog.fadeDuration = .5f;
		playdialog = new Dialog("", LowLatency.instance().skin);
		playdialog.text("  Touch anywhere  ");
		playdialog.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				Gdx.input.setInputProcessor(null);
				if (Holder.visualizer)
					LowLatency.instance().setScreen(new VisualizerScreen());
				else
					LowLatency.instance().setScreen(new GameScreen());
			};
		});
	}

	public void render(float delta) {
        LightTank.instance().update(delta);
		LightTank.instance().draw(null);
		stage.act();
		stage.draw();

	}
	
	public void show() {
		playdialog.show(stage);
		super.show();
	}

    public void dispose() {
        super.dispose();
    }
}
