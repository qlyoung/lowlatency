package com.sawtoothdev.mgoa;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Heart of the game, controls gameplay itself.
 * 
 * @author albatross
 * 
 */

public class PlayScreen implements Screen {

	final MgoaMusic music;
	final Map map;
	final Box2DDebugRenderer renderer = new Box2DDebugRenderer();
	final OrthographicCamera camera;
	final World world;
	final PlayerManager playerManager;
	final LightsManager lightsManager;

	private class PlayerManager implements GameObject {

		private final Body player;
		private final float velocity;

		public PlayerManager(float velocity) {
			this.velocity = velocity;

			// make the player
			{
				BodyDef playerDef = new BodyDef();
				playerDef.type = BodyType.KinematicBody;
				playerDef.position.set(0, 0);
				FixtureDef pFixtureDef = new FixtureDef();
				pFixtureDef.density = 1f;
				pFixtureDef.friction = 0f;

				PolygonShape triangle = new PolygonShape();
				triangle.set(new float[] { -.25f, 0f, .25f, 0f, 0f, .25f });

				pFixtureDef.shape = triangle;
				player = world.createBody(playerDef);
				player.createFixture(pFixtureDef);
			}
		}

		@Override
		public void render(float delta) {

			float posX = 0;

			if (Gdx.input.isKeyPressed(Keys.LEFT))
				posX = -1;
			if (Gdx.input.isKeyPressed(Keys.RIGHT))
				posX = 1;

			player.setTransform(posX,
					player.getPosition().y + velocity * delta, 0f);

			camera.position
					.set(0, playerManager.player.getPosition().y + 2, 0f);
			camera.update();

		}

	}

	private class LightsManager implements GameObject {

		RayHandler rayHandler;

		public LightsManager(Map map, Body player) {

			this.rayHandler = new RayHandler(world);

			// attach a light to each orb
			for (Body orb : map.orbs) {
				attachPointLight(orb, Color.MAGENTA, .5f);
			}

			// give the player a light
			attachPointLight(player, Color.CYAN, 1f);

		}

		@Override
		public void render(float delta) {

			rayHandler.setCombinedMatrix(camera.combined);
			rayHandler.updateAndRender();

		}

		private void attachPointLight(Body body, Color color, float distance) {

			PointLight light = new PointLight(rayHandler, 500, color, distance,
					body.getPosition().x, body.getPosition().y);
			light.setSoft(true);
			light.setXray(true);
			light.setSoftnessLenght(3);
			light.attachToBody(body, 0, 0);

		}

	}

	public PlayScreen(Map map, MgoaMusic music) {
		this.map = map;
		this.music = music;
		this.world = map.world;
		this.playerManager = new PlayerManager(MapGenerator.player_velocity);
		this.lightsManager = new LightsManager(map, playerManager.player);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 10, 6);
		camera.position.set(0, 0, 0);
	}

	@Override
	public void render(float delta) {

		if (!music.isPlaying() && !music.isPaused())
			music.play();

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		playerManager.render(delta);
		lightsManager.render(delta);
		renderer.render(map.world, camera.combined);

		camera.update();

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
