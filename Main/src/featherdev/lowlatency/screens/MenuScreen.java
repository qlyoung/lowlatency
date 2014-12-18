package featherdev.lowlatency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import featherdev.lowlatency.Utilities;
import featherdev.lowlatency.subsystems.Holder;
import featherdev.lowlatency.subsystems.LightTank;
import featherdev.lowlatency.subsystems.MusicPlayer;
import featherdev.lowlatency.subsystems.Stats;

public class MenuScreen extends UiScreen {

    Table root;
    Viewport viewport;

    public MenuScreen() {
        viewport = new ScreenViewport();

        // ui
        TextButton
                playButton = new TextButton("Play", game.skin),
                optionsButton = new TextButton("Options", game.skin),
                creditsButton = new TextButton("Credits", game.skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new SelectSongScreen());
                super.clicked(event, x, y);
            }
        });
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new OptionsScreen());
                super.clicked(event, x, y);
            }
        });
        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new CreditScreen(game));
                super.clicked(event, x, y);
            }
        });

        Table menu = new Table();
        menu.defaults().uniform().pad(45, 15, 45, 0).minWidth(400).left();
        menu.add(playButton).expandY().fill().row();
        menu.add(optionsButton).expandY().fill().row();
        menu.add(creditsButton).expandY().fill().row();

        Label title = new Label("Low\nLatency", game.skin, "naipol_i_big", Color.WHITE);

        root = new Table(game.skin);
        root.setFillParent(true);
        root.add(menu).expand().left().fillY();
        root.add(title).expand().left().padLeft(20);
        stage.addActor(root);
        stage.getRoot().getColor().a = 0;
        Gdx.input.setInputProcessor(stage);

        // background
        LightTank.instance().setup(6, Utilities.getRandomColor(), true);

        // globals
        Holder.clear();
        Stats.clear();

        // music
        FileHandle musicpath;
        switch (Gdx.app.getType()) {
            case Android:
                musicpath = Gdx.files.local("title.mp3");
                break;
            case Desktop:
                musicpath = Gdx.files.internal("audio/title.mp3");
                break;
            default:
                Gdx.app.error("[!]", "Unsupported System: " + Gdx.app.getType().toString());
                return;
        }
        if (!musicpath.exists()){
            Gdx.app.error("[!]", "Cannot locate assets");
            return;
        }

        try {
            if (!MusicPlayer.instance().isPlaying()) {
                MusicPlayer.instance().load(musicpath);
                MusicPlayer.instance().setLooping(true);
            }
        }
        catch (Exception e){
            Gdx.app.error("[!]", e.getMessage());
        }

        // move lights so they don't obscure title
        LightTank.instance().jerkAll(0.5f);
    }

    public void render(float delta) {
        LightTank.instance().update(delta);
        LightTank.instance().draw(null);
        stage.act(delta);
        stage.draw();


        Table.drawDebug(stage);
    }

    public void show() {
        if (game.settings.contains("music")) {
            if (game.settings.getBoolean("music"))
                MusicPlayer.instance().play();
        } else
            MusicPlayer.instance().play();

        super.show();
    }

    public void resize(int width, int height) {
        LightTank.instance().resize(width, height);
        super.resize(width, height);
    }

    public void dispose() {
        super.dispose();
    }
}
