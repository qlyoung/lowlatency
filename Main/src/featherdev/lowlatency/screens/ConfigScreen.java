package featherdev.lowlatency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import featherdev.lowlatency.objects.Difficulty;
import featherdev.lowlatency.subsystems.Holder;
import featherdev.lowlatency.subsystems.LightTank;

public class ConfigScreen extends UiScreen {
	
	Table root;
	
	public ConfigScreen(){
		final CheckBox visualizerMode = new CheckBox(" Visualizer mode", game.skin);

		TextButton backToMenu = new TextButton("Return", game.skin);
		backToMenu.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new SelectSongScreen());
                dispose();
			}
		});

        final List<String> difficultyList = new List<String>(game.skin);
        Array<String> listItems = new Array<String>();
        for (Difficulty d : game.difficulties)
            listItems.add(d.name);
        difficultyList.setItems(listItems);
        difficultyList.setSelectedIndex(1);

        TextButton playButton = new TextButton("Play", game.skin);
        playButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                int index = difficultyList.getSelectedIndex();
                Holder.difficulty = game.difficulties[index];
                Holder.visualizer = visualizerMode.isChecked();
                game.setScreen(new LoadScreen());
                Gdx.input.setInputProcessor(null);
                dispose();
            }
        });

        Table left = new Table(game.skin);
        left.defaults().pad(15, 0, 15, 0);
        left.add("Select Difficulty").row();
        left.add(difficultyList).expandY().left().fill().padTop(17);

        Table right = new Table();
        right.defaults().pad(15, 0, 15, 15);
        right.add(visualizerMode).left().row();
        right.add(playButton).expand().fill();

        String songString = Holder.song.getArtist() + " - " + Holder.song.getTitle();

		root = new Table(game.skin);
        root.setFillParent(true);
        root.add(songString).expandX().fill().left().colspan(2).pad(25, 25, 25, 0).row();
		root.add(left).expandY().fill();
        root.add(right).expandY().fill().row();
		root.add(backToMenu).pad(15, 15, 15, 15).colspan(2).expandX().fill();
		
		stage.addActor(root);
		stage.getRoot().getColor().a = 0;
		Gdx.input.setInputProcessor(stage);
	}

	public void render(float delta) {
        LightTank.instance().update(delta);
		stage.act();

        LightTank.instance().draw(null);
		stage.draw();
		
	}

}
