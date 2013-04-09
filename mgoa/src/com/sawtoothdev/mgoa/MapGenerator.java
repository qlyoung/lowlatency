package com.sawtoothdev.mgoa;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.audioanalysis.FastBeatDetector;

/**
 * Pretty much what it says on the tin
 * 
 * @author albatross
 * 
 */

public class MapGenerator {

	public static Map generate(FileHandle audioFile) {

		// init analysis object
		FastBeatDetector analyzer = new FastBeatDetector(audioFile);
		analyzer.setDebugStream(System.out);

		// generate map
		ArrayList<Beat> beats = null;

		try {
			beats = analyzer
					.detectBeats(FastBeatDetector.SENSITIVITY_AGGRESSIVE);
		} catch (IOException e) {
			Gdx.app.log("Map Generator", e.getMessage());
		}

		// should be generated from music but for now constant value
		float player_velocity = 16;

		// les get funkay
		ArrayList<Body> walls = new ArrayList<Body>(), orbs = new ArrayList<Body>();

		Vector2 lastWallEnd = new Vector2(-3, 0);
		Beat lastWallBeat = new Beat(0, 1);

		World world = new World(new Vector2(0, 0), true);

		{// debugging - place marker orb at world origin
			BodyDef orbDef = new BodyDef();
			orbDef.type = BodyType.KinematicBody;
			orbDef.position.set(0, 0);

			CircleShape circleShape = new CircleShape();
			circleShape.setRadius(.5f);

			FixtureDef orbFixtureDef = new FixtureDef();
			orbFixtureDef.density = 1f;
			orbFixtureDef.friction = 0f;
			orbFixtureDef.shape = circleShape;

			Body orb = world.createBody(orbDef);
			orb.createFixture(orbFixtureDef);

			// cleanup
			circleShape.dispose();
		}

		for (Beat b : beats) {

			// if high energy, make a pair of walls
			if (b.energy > .8f) {

				/*
				 * walls are defined by bodies containing one fixture, an edge,
				 * that radiates from the center of the body; so the body's
				 * origin should be placed at the end of the last edge
				 */

				BodyDef wallDef = new BodyDef();
				wallDef.type = BodyType.StaticBody;
				wallDef.allowSleep = true;
				wallDef.position.set(lastWallEnd);

				float length = (b.timeMs - lastWallBeat.timeMs)
						* player_velocity;

				EdgeShape wallShape = new EdgeShape();
				wallShape.set(new Vector2(0, 0), new Vector2(0f, length));

				FixtureDef wallFixtureDef = new FixtureDef();
				wallFixtureDef.density = 1f;
				wallFixtureDef.friction = 0f;
				wallFixtureDef.shape = wallShape;

				Body wall = world.createBody(wallDef);
				wall.createFixture(wallFixtureDef);

				walls.add(wall);

				// cleanup
				wallShape.dispose();

				lastWallEnd.set(lastWallEnd.x, lastWallEnd.y + length);
				lastWallBeat = b;

			} else {

				// create orb
				BodyDef orbDef = new BodyDef();
				orbDef.type = BodyType.KinematicBody;
				orbDef.allowSleep = true;
				orbDef.position
						.set(new Vector2(0f, b.timeMs * player_velocity));

				CircleShape circleShape = new CircleShape();
				circleShape.setRadius(.5f);

				FixtureDef orbFixtureDef = new FixtureDef();
				orbFixtureDef.density = 1f;
				orbFixtureDef.friction = 0f;
				orbFixtureDef.shape = circleShape;

				Body orb = world.createBody(orbDef);
				orb.createFixture(orbFixtureDef);

				orbs.add(orb);

				// cleanup
				circleShape.dispose();

			}

		}

		Map map = new Map();
		map.walls = walls;
		map.orbs = orbs;
		map.world = world;

		Gdx.app.log("Map Generator", "walls: " + walls.size());
		Gdx.app.log("Map generator", "orbs: " + orbs.size());
		
		return map;
	}

}
