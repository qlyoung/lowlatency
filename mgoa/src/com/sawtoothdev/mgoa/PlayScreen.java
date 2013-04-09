package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

/**
 * Heart of the game, controls gameplay itself.
 * @author albatross
 *
 */

public class PlayScreen implements Screen {
	
	MgoaMusic music;
	Map map;
	Box2DDebugRenderer renderer = new Box2DDebugRenderer();
	OrthographicCamera camera;
	
	
	public PlayScreen(Map map, MgoaMusic music){
		this.map = map;
		this.music = music;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 10, 6);
		camera.position.set(0, 0, 0);
	}

	
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.update();
		
		renderer.render(map.world, camera.combined);
		
		if (!music.isPlaying() && !music.isPaused())
			music.play();
		
		camera.translate(0f, MapGenerator.player_velocity * delta);
		
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
