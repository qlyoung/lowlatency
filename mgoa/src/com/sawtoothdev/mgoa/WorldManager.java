package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WorldManager implements IDrawableGameObject {

	private final HUD hud;
	public final SongEngine engine;
	private final EffectsManager fx;
	private final Visualizer visualizer;
	private final CoreManager coreManager;
	
	public WorldManager(BeatMap map, FileHandle audioFile){
		
		engine = new SongEngine(map, audioFile);
		visualizer = new Visualizer(2048);
		hud = new HUD(audioFile);
		fx = new EffectsManager();
		coreManager = new CoreManager(engine, fx, hud, map.NORMAL);
	}
	
	@Override
	public void update(float delta) {
		
		engine.update(delta);
		
		coreManager.update(delta);
		fx.update(delta);
		hud.update(delta);
	}

	@Override
	public void draw(SpriteBatch batch) {
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		visualizer.renderFrame(engine.getMusic().getLatestSamples(), 61, Color.ORANGE, batch);
		fx.draw(batch);
		coreManager.draw(batch);
		hud.draw(batch);
	}
	
	public void start(){
		engine.start();
	}
	public void pause(){
		
	}
	public void end(){
		
	}
}
