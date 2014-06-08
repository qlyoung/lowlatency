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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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
            NORMAL_LIGHT_RADIUS,
            MAX_LIGHT_RADIUS,
            LIGHT_BODY_RADIUS,
            MAX_VELOCITY_METRES_PER_SECOND,
            SHRINK_RATE_METRES_PER_SECOND;

    private LightTank() {
        // start with world viewport of 10 x 6 & automatically adjust to match
        // screen aspect ratio. Cam centered at (0, 0)
        viewport = new ExtendViewport(10, 6);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        world = new World(new Vector2(), true);
        rh = new RayHandler(world);
        rh.setCombinedMatrix(viewport.getCamera().combined);
        rh.setBlur(true);
        rh.setCulling(true);
        stopwatch = new Stopwatch();
        playful = false;
        random = new Random();

        // radius of each light should be 1/2 world's smaller dimension
        NORMAL_LIGHT_RADIUS = viewport.getWorldHeight() / 2f;
        // maximum should be 4 times normal size
        MAX_LIGHT_RADIUS = 4 * NORMAL_LIGHT_RADIUS;
        // physics body radius should be super small, we'll do 1/10 light radius
        LIGHT_BODY_RADIUS = NORMAL_LIGHT_RADIUS / 10f;
        // at max velocity lights should take 1/2 second to cross the screen
        MAX_VELOCITY_METRES_PER_SECOND = viewport.getWorldWidth() * 2f;
        // lights should take 1 second to shrink to normal size from maximum
        SHRINK_RATE_METRES_PER_SECOND = MAX_LIGHT_RADIUS;

        // create box walls
        spawnWall(-viewport.getWorldWidth() / 2f, 0, .1f, viewport.getWorldHeight());
        spawnWall(viewport.getWorldWidth() / 2f, 0, .1f, viewport.getWorldHeight());
        spawnWall(0, viewport.getWorldHeight() / 2f, viewport.getWorldWidth(), .1f);
        spawnWall(0, -viewport.getWorldHeight() / 2f, viewport.getWorldWidth(), .1f);
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
        wallFixture.restitution = 1f;

        wall.createFixture(wallFixture);
    }

    private void spawnLight(Color c) {
        // create body
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        Body lightbody = world.createBody(def);
        lightbody.setLinearDamping(viewport.getWorldWidth() / 3f);

        // add a circular fixture to the body so they collide with walls
        CircleShape circ = new CircleShape();
        circ.setRadius(LIGHT_BODY_RADIUS);
        FixtureDef circfix = new FixtureDef();
        circfix.shape = circ;
        lightbody.createFixture(circfix);

        // spawn light at center of screen
        float x = viewport.getWorldWidth() / 2f;
        float y = viewport.getWorldHeight() /2f;
        PointLight p = new PointLight(rh, 100, c, NORMAL_LIGHT_RADIUS, x, y);
        p.attachToBody(lightbody, 0, 0);
        p.setSoft(true);
        p.setXray(true);

        p.setPosition(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f);
    }

    public void update(float delta) {
        world.step(1 / 45f, 6, 2);

        if (playful) { // jerk from time to time
            if (stopwatch.time() / 1000f > random.nextInt((6 - 4) + 1) + 4 || Gdx.input.justTouched()) {
                jerkAll(random.nextFloat());
                stopwatch.reset();
                stopwatch.start();
            }
        }

        for (Light l : rh.lightList){
            if (l.getDistance() > NORMAL_LIGHT_RADIUS)
                l.setDistance(l.getDistance() - delta * SHRINK_RATE_METRES_PER_SECOND);
        }

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
        float additiveDistance = energy * NORMAL_LIGHT_RADIUS * 2;

        for (Light l : rh.lightList){
            float newDistance = l.getDistance() + additiveDistance;
            l.setDistance(newDistance);

            // limit light distance to double the normal
            if (l.getDistance() > NORMAL_LIGHT_RADIUS * 2)
                l.setDistance(NORMAL_LIGHT_RADIUS * 2);
        }
    }

    /***
     * @param energy should be between 0 and 1
     */
    public void jerkAll(float energy){
        energy *= 2;
        for (Light l : rh.lightList){
            // calculate random direction
            float min = .5f, max = MAX_VELOCITY_METRES_PER_SECOND;
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

    public void resize(int screenWidth, int screenHeight){
        viewport.update(screenWidth, screenHeight);
    }
}
