package featherdev.mgoa.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import featherdev.mgoa.Mgoa;
import featherdev.mgoa.screens.GameScreen;
import featherdev.mgoa.screens.MenuScreen;

public class PausedMenu extends Stage {
	
	public PausedMenu(final GameScreen gs) {
		super();
		
		Skin skin = Mgoa.instance().skin;
		
		TextButton resume = new TextButton("Resume", skin);
		TextButton quitToMenu = new TextButton("Quit to Main Menu", skin);
		
		resume.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gs.resume();
				super.clicked(event, x, y);
			}
		});
		quitToMenu.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.input.setInputProcessor(null);
				Mgoa.instance().setScreen(new MenuScreen());
				super.clicked(event, x, y);
			}
		});
		
		Table table = new Table(skin);
		table.setFillParent(true);
		table.add("Options");
		table.row();
		table.add(resume);
		table.row();
		table.add(quitToMenu);
		
		addActor(table);
	}

}
