package featherdev.lowlatency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import featherdev.lowlatency.LowLatency;
import featherdev.lowlatency.game.LightShow;
import featherdev.lowlatency.game.CoreManager;
import featherdev.lowlatency.subsystems.Effects;
import featherdev.lowlatency.subsystems.HeadsUpDisplay;
import featherdev.lowlatency.subsystems.Holder;
import featherdev.lowlatency.subsystems.MusicPlayer;
import featherdev.lowlatency.subsystems.ScoreRecords;

public class GameScreen implements Screen {

    enum WorldState {
        MAIN, OUTRO, PAUSED
    }
    WorldState state;

    LowLatency game;
    LightShow lightShow;
    CoreManager coremanager;
    Stage pausedMenu;

    public GameScreen() {
        game = LowLatency.instance();
        lightShow = new LightShow(Holder.rawmap);
        coremanager = new CoreManager(Holder.beatmap, Holder.difficulty);
        HeadsUpDisplay.instance().setSongInfo(Holder.song.getArtist(), Holder.song.getTitle());

        pausedMenu = new Stage();
        Skin skin = LowLatency.instance().skin;
        TextButton resume = new TextButton("Resume", skin);
        TextButton quitToMenu = new TextButton("Quit to Main Menu", skin);
        resume.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                switchstate(WorldState.MAIN);
                super.clicked(event, x, y);
            }
        });
        quitToMenu.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setInputProcessor(null);
                LowLatency.instance().setScreen(new MenuScreen());
                super.clicked(event, x, y);
            }
        });
        Table table = new Table(skin);
        table.defaults().pad(10);
        table.defaults().fill();
        table.setFillParent(true);
        table.add("Paused");
        table.row();
        table.add(resume);
        table.row();
        table.add(quitToMenu);
        pausedMenu.addActor(table);

        MusicPlayer.instance().load(Holder.song.getHandle());

        // shitty performance shit
        System.gc();
        
        state = WorldState.MAIN;
    }

    private void switchstate(WorldState newState) {
        switch (newState) {
            case MAIN:
                if (state == WorldState.PAUSED) {
                    MusicPlayer.instance().play();
                    Gdx.input.setInputProcessor(null);
                    state = WorldState.MAIN;
                }
                break;
            case PAUSED:
                if (state == WorldState.MAIN) {
                    // pause music, focus paused menu, switch to paused update loop
                    MusicPlayer.instance().pause();
                    Gdx.input.setInputProcessor(pausedMenu);
                    state = WorldState.PAUSED;
                }
                break;
            case OUTRO:
                if (state == WorldState.MAIN){
                    HeadsUpDisplay.instance().fadeout(1);
                    state = WorldState.OUTRO;
                }
                break;
        }
    }

    public void render(float delta) {
        switch (state) {

            case MAIN:
                // update
                lightShow.update(delta);
                coremanager.update(delta);
                HeadsUpDisplay.instance().update(delta);

                // draw
                lightShow.draw(game.batch);
                coremanager.draw(game.batch);
                Effects.instance().render(delta, game.batch);
                HeadsUpDisplay.instance().draw(game.batch);

                // state
                if (!MusicPlayer.instance().isPlaying())
                    switchstate(WorldState.OUTRO);
                else if (Gdx.input.justTouched() && Gdx.input.getX() < 30 && Gdx.input.getY() < 40)
                    switchstate(WorldState.PAUSED);
                else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
                    switchstate(WorldState.PAUSED);

                break;


            case PAUSED:
                lightShow.draw(game.batch);
                coremanager.draw(game.batch);
                HeadsUpDisplay.instance().draw(game.batch);
                pausedMenu.act();
                //pausedMenu.draw();
                break;

            case OUTRO:
                HeadsUpDisplay.instance().update(delta);
                HeadsUpDisplay.instance().draw(game.batch);

                if (HeadsUpDisplay.instance().getAlpha() == 0f)
                    game.setScreen(new FinishScreen());
                break;
        }
    }

    public void resize(int width, int height) { }

    public void show() {
        // fade in HeadsUpDisplay.instance()
        HeadsUpDisplay.instance().fadein(1f);
        // show player's last score
        int score = ScoreRecords.instance().readScore(Holder.song.getHandle());
        if (score != -1) {
            String message = "Personal best: " + String.valueOf(score);
            TextBounds bounds = game.skin.getFont("naipol").getBounds(message);
            Vector2 top = new Vector2(Gdx.graphics.getWidth() / 2f
                    - bounds.width / 2f, Gdx.graphics.getHeight()
                    - (bounds.height + 10f));

            HeadsUpDisplay.instance().showMessage(message, top, 5f, .2f);
        }
        // play the music
        MusicPlayer.instance().play();
    }

    public void hide() { }

    public void pause() {
        switchstate(WorldState.PAUSED);
    }

    public void resume() { }

    public void dispose() {
        pausedMenu.dispose();
    }

}
