package featherdev.lowlatency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import featherdev.lowlatency.LowLatency;
import featherdev.lowlatency.objects.FileBrowserWidget;
import featherdev.lowlatency.objects.Song;
import featherdev.lowlatency.subsystems.Holder;
import featherdev.lowlatency.subsystems.LightTank;


public class SelectSongScreen extends UiScreen {

    FileBrowserWidget browser;

    public SelectSongScreen(){
        this(Gdx.files.getExternalStoragePath());
    }
    public SelectSongScreen(String dir){
        browser = new FileBrowserWidget(dir, LowLatency.instance().skin);

        Table root = new Table(game.skin);
        root.add("Select Song").top().pad(5, 0, 5, 0);
        root.row();
        root.add(browser).expand().fill();
        root.row();
        TextButton home = new TextButton("Main Menu", game.skin);
        home.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new MenuScreen());
            }
        });
        root.add(home).bottom().pad(15, 0, 5, 0);
        root.setFillParent(true);

        stage.addActor(root);
        Gdx.input.setInputProcessor(stage);
    }

    public void render(float delta){
        LightTank.instance().update(delta);
        LightTank.instance().draw(null);
        stage.act();
        stage.draw();

        if (browser.selection != null){
            String extension = browser.selection.extension().toLowerCase();
            boolean isAudio = extension.contains("mp3") || extension.contains("ogg");
            if (isAudio){
                Gdx.input.setInputProcessor(null);
                Holder.song = new Song(browser.selection);
                game.setScreen(new ConfigScreen());
            }
            else {
                dialog("Unsupported file type.");
                browser.selection = null;
            }

        }
    }

    private void dialog(String text){
        final Dialog dialog = new Dialog("", game.skin);
        dialog.text(text);
        dialog.button("Ok").addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                dialog.cancel();
            }
        });
        Dialog.fadeDuration = .2f;

        dialog.show(stage);
    }
}
