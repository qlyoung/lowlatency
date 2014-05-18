package featherdev.lowlatency.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import featherdev.lowlatency.Utilities;
import featherdev.lowlatency.objects.IDrawable;
import featherdev.lowlatency.objects.IUpdateable;
import featherdev.lowlatency.subsystems.LightTank;
import featherdev.lowlatency.subsystems.MusicPlayer;
import featherdev.lwbd.Beat;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * LightTank controller that follows an event timeline
 */
public class LightShow implements IUpdateable, IDrawable {

    final int WINDOW_SIZE = 3;
    Iterator<Beat> events;
    Beat nextBeat;
    LinkedList<Color> window = new LinkedList<Color>();
	
	public LightShow(LinkedList<Beat> beats){
        // initialize event timeline
        events = beats.iterator();
        nextBeat = events.next();

        // configure lights
        switch (Gdx.app.getType()){
            case Android:
                LightTank.instance().setup(3, Utilities.getRandomColor(), false);
                break;
            case Desktop:
                LightTank.instance().setup(6, Utilities.getRandomColor(), false);
                break;
        }

	}
	
	public void update(float delta) {
        while (nextBeat != null && MusicPlayer.instance().time() >= nextBeat.timeMs) {

            // out with the old, in with the new
            if (window.size() == WINDOW_SIZE)
                window.removeFirst();

            if (BeatCore.getEnergyColor(nextBeat.energy) != Color.MAGENTA)
                window.addLast(BeatCore.getEnergyColor(nextBeat.energy));

            // average all the colors in the window
            float r = 0, g = 0, b = 0;
            if (window.size() > 0){
                for (Color c : window) {
                    r += c.r;
                    g += c.g;
                    b += c.b;
                }
                r /= window.size();
                g /= window.size();
                b /= window.size();
            }
            Color c = new Color(r, g, b, 1);

            LightTank.instance().changeColor(c);
            LightTank.instance().jerkAll(nextBeat.energy);
            LightTank.instance().pulseAll(nextBeat.energy);

            nextBeat = events.hasNext() ? events.next() : null;
        }

        LightTank.instance().update(delta);
	}
	public void draw(SpriteBatch batch) {
        LightTank.instance().draw(null);
	}

}
