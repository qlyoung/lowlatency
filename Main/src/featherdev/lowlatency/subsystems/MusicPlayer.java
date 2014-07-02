package featherdev.lowlatency.subsystems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

import featherdev.lowlatency.objects.Stopwatch;

/**
 * Wrapper for Music that keeps correct time
 *
 * @author snowdrift
 */

public class MusicPlayer implements Disposable {

    enum PlayerState {STOPPED, PLAYING, PAUSED}

    private static MusicPlayer instance;

    public static MusicPlayer instance() {
        if (instance == null)
            instance = new MusicPlayer();
        return instance;
    }

    Music music;
    Stopwatch stopwatch;
    PlayerState state;

    private MusicPlayer() {
        stopwatch = new Stopwatch();
    }

    public void load(FileHandle audio) {
        if (music != null) {
            stop();
            music.dispose();
        }
        music = Gdx.audio.newMusic(audio);
        music.play();
        music.stop();

        state = PlayerState.STOPPED;
    }

    public void play() {
        if (music == null)
            return;

        switch (state) {
            case STOPPED:
                music.play();
                stopwatch.start();
                break;
            case PLAYING:
                break;
            case PAUSED:
                music.play();
                stopwatch.start();
                break;
        }

        state = PlayerState.PLAYING;
    }

    public void pause() {
        if (music == null)
            return;

        music.pause();
        stopwatch.pause();
        state = PlayerState.PAUSED;
    }

    public void stop() {
        if (music == null)
            return;

        music.stop();
        stopwatch.reset();
        state = PlayerState.STOPPED;
    }

    public long time() {
        if (music == null)
            return 0;

        if (state == PlayerState.STOPPED)
            return 0;
        else
            return stopwatch.time();
    }

    public boolean isPlaying() {
        if (music == null)
            return false;
        else
            return music.isPlaying();
    }

    public void setVolume(float volume) {
        if (music == null)
            return;

        music.setVolume(volume);
    }

    public void setLooping(boolean val) {
        if (music == null)
            return;

        music.setLooping(val);
    }

    public void dispose() {
        if (music == null)
            return;

        music.dispose();
    }


}