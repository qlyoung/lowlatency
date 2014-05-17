package featherdev.lowlatency.subsystems;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import featherdev.lowlatency.objects.IDrawable;
import featherdev.lowlatency.objects.IUpdateable;
import featherdev.lowlatency.objects.Stopwatch;

import java.util.Random;

/**
 * Created by snowdrift on 5/16/14.
 */
public class LightTank implements IUpdateable, IDrawable, Disposable {

    private static LightTank instance;

    public static LightTank instance() {
        if (instance == null)
            instance = new LightTank();

        return instance;
    }

    Viewport viewport;
    RayHandler rh;
    World world;
    boolean playful;
    Stopwatch stopwatch;
    Random random;
    final float
            LIGHT_RADIUS,
            LIGHT_BODY_RADIUS,
            MAX_VELOCITY_METRES_PER_SECOND,
            SHRINK_RATE_PPS;

    private LightTank() {
        viewport = new ScreenViewport();
        System.out.println(
                viewport.getViewportWidth() + " : " + viewport.getViewportHeight() +"\n"
              + viewport.getWorldWidth() + " : " + viewport.getWorldHeight());
        world = new World(new Vector2(), true);
        rh = new RayHandler(world);
        rh.setCombinedMatrix(viewport.getCamera().combined);
        stopwatch = new Stopwatch();
        playful = false;
        random = new Random();

        // diameter of each light should be 1/6 screen width
        LIGHT_RADIUS = 7;
        // body radius should be super small (for wall collisions), constant 5 pixels
        LIGHT_BODY_RADIUS = 5;
        // at max velocity lights should take 1/2 second to cross the screen
        MAX_VELOCITY_METRES_PER_SECOND = .5f * Gdx.graphics.getWidth();
        // lights should take 1 second to shrink to normal size from double normal size
        SHRINK_RATE_PPS = LIGHT_RADIUS;

        // create box walls
        spawnWall(0, 0, 1, viewport.getViewportHeight());
        spawnWall(0, viewport.getViewportWidth() - 1, 1, viewport.getViewportHeight());
        spawnWall(0, 0, viewport.getViewportWidth(), 1);
        spawnWall(0, viewport.getViewportHeight() - 1, viewport.getViewportWidth(), 1);
    }

    private void spawnWall(float x, float y, float width, float height) {
        BodyDef wallDef = new BodyDef();
        wallDef.type = BodyDef.BodyType.StaticBody;
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

    private void spawnLight(Color c) {
        // create body
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(0, 0);
        Body lightbody = world.createBody(def);

        // add a circular fixture to the body so they collide with walls
        CircleShape circ = new CircleShape();
        circ.setRadius(5);
        FixtureDef circfix = new FixtureDef();
        circfix.shape = circ;
        lightbody.createFixture(circfix);

        // spawn light at center of screen
        float x = Gdx.graphics.getWidth() / 2f;
        float y = Gdx.graphics.getHeight() / 2f;
        PointLight p = new PointLight(rh, 100, c, LIGHT_RADIUS, x, y);
        p.attachToBody(lightbody, 0, 0);
        p.setXray(true);
        p.setSoft(true);
    }

    public void update(float delta) {
        world.step(1 / 45f, 6, 2);

        if (playful) { // jerk from time to time
            if (stopwatch.time() > random.nextInt((6 - 4) + 1) + 4 || Gdx.input.justTouched()) {
                jerkAll(random.nextFloat());
                stopwatch.reset();
                stopwatch.start();
            }
        }

        for (Light l : rh.lightList){
            if (l.getDistance() > LIGHT_RADIUS)
                l.setDistance(l.getDistance() - delta * SHRINK_RATE_PPS);
            Gdx.app.log(l.getPosition().toString(), String.valueOf(l.getDistance()));
        }
        Gdx.app.log("lights out", "--");

        rh.update();
    }

    public void draw(SpriteBatch batch) {
        rh.render();
    }

    public void setup(int numLights, Color lightsColor, boolean playful) {
        // destroy light bodies, leaving walls intact
        for (Light l : rh.lightList)
            world.destroyBody(l.getBody());

        // destroy lights
        rh.removeAll();

        // make stuff
        this.playful = playful;
        for (int i = 0; i < numLights; i++)
            spawnLight(lightsColor);
    }

    public void changeColor(Color newColor) {
        for (Light l : rh.lightList)
            l.setColor(newColor);
    }

    /***
     * @param energy should be between 0 and 1
     */
    public void pulseAll(float energy){
        // add a percentage of the normal light distance
        float additiveDistance = energy * LIGHT_RADIUS;

        for (Light l : rh.lightList){
            float newDistance = l.getDistance() + additiveDistance;
            l.setDistance(newDistance);

            // limit light distance to double the normal
            if (l.getDistance() > LIGHT_RADIUS * 2)
                l.setDistance(LIGHT_RADIUS * 2);
        }
    }

    /***
     * @param energy should be between 0 and 1
     */
    public void jerkAll(float energy){
        for (Light l : rh.lightList){
            // calculate random direction
            float min = .01f, max = 1f;
            float xdir = ((max - min) * random.nextFloat() + min);
            float ydir = ((max - min) * random.nextFloat() + min);
            xdir = random.nextBoolean() ? xdir : -xdir;
            ydir = random.nextBoolean() ? ydir : -ydir;

            // direction * energy = force
            float xforce = xdir * energy;
            float yforce = ydir * energy;

            // apply force
            l.getBody().setLinearVelocity(xforce, yforce);
        }
    }

    public void dispose() {
        rh.dispose();
        world.dispose();
    }
}
