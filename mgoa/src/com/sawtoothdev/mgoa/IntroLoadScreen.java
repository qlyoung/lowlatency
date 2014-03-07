package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class IntroLoadScreen implements Screen {
	
	AssetManager manager;
	Mgoa game;
	
	public IntroLoadScreen(AssetManager manager, Mgoa game){
		this.game = game;
		this.manager = manager;
		
		manager.load("fonts/naipol.fnt", BitmapFont.class);
		manager.load("textures/core.png", Texture.class);
		manager.load("textures/progbarunit.png", Texture.class);
		manager.load("textures/ring.png", Texture.class);
		manager.load("ui/uiskin.json", Skin.class);
		
	}

	@Override
	public void render(float delta) {
		manager.update();
		int percentdone = (int) manager.getProgress() * 100;
		System.out.println("Loading..." + percentdone + "%");
		
		if (percentdone == 100)
			game.setScreen(new MenuScreen());
		
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
