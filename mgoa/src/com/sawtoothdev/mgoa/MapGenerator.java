package com.sawtoothdev.mgoa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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

	// should be generated from music but for now constant value, metres / second
	static float player_velocity = 10;
	
	public static Map generate(FileHandle audioFile) {

		// make a beat detector
		FastBeatDetector analyzer = new FastBeatDetector(audioFile);
		analyzer.setDebugStream(System.out);

		// detect beats
		ArrayList<Beat> beats = null;

		try {
			
			beats = analyzer.detectBeats(FastBeatDetector.SENSITIVITY_STANDARD);
			
		} catch (IOException e) {
			Gdx.app.log("Map Generator", e.getMessage());
		}


		// les get funkay
		ArrayList<Body> walls = new ArrayList<Body>(), orbs = new ArrayList<Body>();

		Vector2 prevWallEnd = new Vector2();
		World world = new World(new Vector2(0, 0), true);

		//debugging marker at origin
		putOrb(world, 0, 0, .5f);
		
		//random number generator
		Random r = new Random();

		for (Beat beat : beats) {

			float timeSecs = beat.timeMs / 1000f;
			
			if (beat.energy > .5f) {				
				
				
				walls.add( putEdge(world, prevWallEnd.x - 1, prevWallEnd.y, prevWallEnd.x - 1, beat.timeMs * player_velocity) );
				walls.add( putEdge(world, prevWallEnd.x + 1, prevWallEnd.y, prevWallEnd.x + 1, beat.timeMs * player_velocity) );
				
				//put marker orbs where the walls end, so we can see where walls should turn
				putOrb(world, prevWallEnd.x - 2, prevWallEnd.y, .1f);
				putOrb(world, prevWallEnd.x + 2, prevWallEnd.y, .1f);
				
				prevWallEnd.set(0, timeSecs * player_velocity);		

			} else {
				
				float orbX = 0;
				
				switch( r.nextInt(3) ){
				case 0:
					orbX = -1f;
					break;
				case 1:
					orbX = 0f;
					break;
				case 2:
					orbX = 1f;
					break;
				}
				
				orbs.add( putOrb(world, orbX, timeSecs * player_velocity, .2f) );

			}

		}

		Map map = new Map();
		map.walls = walls;
		map.orbs = orbs;
		map.world = world;

		Gdx.app.log("Map Generator", "Generation complete.");
		Gdx.app.log("Map Generator", "walls - " + walls.size());
		Gdx.app.log("Map generator", "orbs - " + orbs.size());
		
		return map;
	}
	
	private static Body putOrb(World world, float x, float y, float radius){
		
		BodyDef orbDef = new BodyDef();
		orbDef.type = BodyType.KinematicBody;
		orbDef.allowSleep = true;
		orbDef.position.set(x, y);

		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(radius / 2);

		FixtureDef orbFixtureDef = new FixtureDef();
		orbFixtureDef.density = 1f;
		orbFixtureDef.friction = 0f;
		orbFixtureDef.shape = circleShape;

		Body orb = world.createBody(orbDef);
		orb.createFixture(orbFixtureDef);

		circleShape.dispose();
		
		return orb;
	}
	private static Body putEdge(World world, float x1, float y1, float x2, float y2){

		BodyDef wallDef = new BodyDef();
		wallDef.type = BodyType.StaticBody;
		wallDef.allowSleep = true;
		wallDef.position.set(x1, y1);


		EdgeShape wallShape = new EdgeShape();
		wallShape.set(x1, y1, x2, y2);

		FixtureDef wallFixtureDef = new FixtureDef();
		wallFixtureDef.density = 1f;
		wallFixtureDef.friction = 0f;
		wallFixtureDef.shape = wallShape;

		Body wall = world.createBody(wallDef);
		wall.createFixture(wallFixtureDef);

		// cleanup
		wallShape.dispose();
		
		return wall;
	}
	
}
