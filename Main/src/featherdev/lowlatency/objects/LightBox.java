package featherdev.lowlatency.objects;

import java.util.Iterator;
import java.util.Random;


import box2dLight.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Eye candy for a more...synchronized age.
 * @author snowdrift
 *
 */

public class LightBox implements IUpdateable, IDrawable, Disposable {

	World world;
	OrthographicCamera cam;
	RayHandler rayHandler;
	Random random;
	boolean idle;
	float idleTimer;
	float
		LIGHT_MAX_DISTANCE = 5,
		LIGHT_MIN_DISTANCE = 2,
		LIGHT_SHRINK_RATE = 6,
		LIGHT_BODY_LINEAR_DAMPING = .5f,
		LIGHT_BODY_RADIUS = .5f,
		IDLE_TIMER_MIN = 4,
		IDLE_TIMER_MAX = 8;
	
	public LightBox(){
		this.cam = new OrthographicCamera(10, 6);
		this.world = new World(new Vector2(0, 0), false);
		this.idle = false;
		this.random = new Random();

		rayHandler = new RayHandler(world);
		rayHandler.setCombinedMatrix(cam.combined);
		rayHandler.setBlur(true);
		rayHandler.setCulling(true);
		
		spawnWall(5, 0, .25f, 6);
		spawnWall(-5, 0, .25f, 6);
		spawnWall(0, 3, 10, .25f);
		spawnWall(0, -3, 10, .25f);
		
		idleTimer = IDLE_TIMER_MIN;
	}
	
	private void spawnWall(float x, float y, float width, float height) {
		BodyDef wallDef = new BodyDef();
		wallDef.type = BodyType.StaticBody;
		wallDef.position.set(new Vector2(x, y));

		Body wall = world.createBody(wallDef);

		PolygonShape wallShape = new PolygonShape();
		wallShape.setAsBox(width / 2f, height / 2f);

		FixtureDef wallFixture = new FixtureDef();
		wallFixture.shape = wallShape;
		wallFixture.friction = 0f;
		wallFixture.restitution = 0f;

		wall.createFixture(wallFixture);
	}
	private void addLight(Color color){
		BodyDef orbDef = new BodyDef();

		orbDef.position.set(new Vector2());
		orbDef.type = BodyType.DynamicBody;

		Body orbBody = world.createBody(orbDef);

		CircleShape circle = new CircleShape();
		circle.setRadius(LIGHT_BODY_RADIUS);
		FixtureDef circfix = new FixtureDef();
		circfix.shape = circle;
		circfix.friction = 0f;
		// circfix.restitution = 1f;
		circfix.density = 0f;

		orbBody.createFixture(circfix);
		orbBody.setLinearVelocity(random.nextFloat() - .5f,
				random.nextFloat() - .5f);
		orbBody.setLinearDamping(LIGHT_BODY_LINEAR_DAMPING);

		PointLight plight = new PointLight(rayHandler, 128, color, LIGHT_MIN_DISTANCE, 0, 0);
		plight.attachToBody(orbBody, 0, 0);
		plight.setSoft(true);
		plight.setXray(true);
	}
	
	@Override
	public void update(float delta) {

		world.step(1 / 45f, 6, 2);

		if (idle){
			idleTimer -= delta;
			if (idleTimer <= 0 || Gdx.input.justTouched()){
				jerkLights(2);
				idleTimer = ((IDLE_TIMER_MAX - IDLE_TIMER_MIN) * random.nextFloat() + IDLE_TIMER_MIN);
			}
		}

		for (Light l : rayHandler.lightList)
			if (l.getDistance() > LIGHT_MIN_DISTANCE){
				float newDistance = l.getDistance() - (delta * LIGHT_SHRINK_RATE);
				l.setDistance(newDistance);
			}

	}
	@Override
	public void draw(SpriteBatch batch) {
		rayHandler.updateAndRender();
	}
	public void setNumLights(Color color, int n){
		for (Light l : rayHandler.lightList)
			world.destroyBody(l.getBody());
		rayHandler.removeAll();
		
		for (int i = 0; i < n; i++)
			addLight(color);
	}
	public void pulseLights(float force){
		for (Light l : rayHandler.lightList) {
			float newDistance = l.getDistance() + force;

			if (newDistance > LIGHT_MAX_DISTANCE)
				l.setDistance(LIGHT_MAX_DISTANCE);
			else
				l.setDistance(newDistance);
		}
	}
	public void jerkLights(float metersPerSecond){

        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        Iterator<Body> i = bodies.iterator();

		while (i.hasNext()) {
			float min = .03f, max = 1f;

			float xDirection = ((max - min) * random.nextFloat() + min);
			float yDirection = ((max - min) * random.nextFloat() + min);

			xDirection *= metersPerSecond;
			yDirection *= metersPerSecond;
			
			xDirection = random.nextBoolean() ? xDirection : -xDirection;
			yDirection = random.nextBoolean() ? yDirection : -yDirection;
			
			i.next().setLinearVelocity(xDirection, yDirection);
		}
	}
	public void setAllLightsColor(Color color){
		for (Light l : rayHandler.lightList)
			l.setColor(color);
	}
	public void idle(){
		this.idle = true;
	}
	public void standby(){
		this.idle = false;
	}
	public void setGravity(Vector2 gravity){
		world.setGravity(gravity);
	}
	public Color getColor(){
		Light l = rayHandler.lightList.random();
		if (l != null)
			return l.getColor();
		else
			return null;
	}
	@Override
	public void dispose() {
		world.dispose();
		rayHandler.dispose();
	}
}
