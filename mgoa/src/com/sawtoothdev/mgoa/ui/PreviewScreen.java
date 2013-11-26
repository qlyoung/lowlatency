package com.sawtoothdev.mgoa.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.sawtoothdev.mgoa.BeatMap;
import com.sawtoothdev.mgoa.Difficulty;
import com.sawtoothdev.mgoa.Resources;
import com.sawtoothdev.mgoa.UIStyles;
import com.sawtoothdev.mgoa.game.GameScreen;
import com.sawtoothdev.mgoa.game.Playthrough;

public class PreviewScreen implements Screen {

	private Stage stage = new Stage();
	Label difficulty, txtTotalBeats, beatCount, length, title;
	SelectBox difficultySelector;
	TextButton playButton;

	private int totalBeats = 0;

	public PreviewScreen(final BeatMap map, final FileHandle fileHandle) {

		Gdx.input.setInputProcessor(this.stage);

		TextButtonStyle tStyle = new TextButtonStyle();
		tStyle.font = UIStyles.uiLabelStyle.font;

		TextureRegionDrawable downArrow = new TextureRegionDrawable(
				new TextureRegion(new Texture(
						Gdx.files.internal("data/textures/ui/downarrow.png"))));
		TextureRegionDrawable graybg = new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files
						.internal("data/textures/ui/dropdown-bg.png"))));
		TextureRegionDrawable clearbg = new TextureRegionDrawable(
				new TextureRegion(new Texture(
						Gdx.files.internal("data/textures/ui/clear-bg.png"))));
		SelectBoxStyle sbStyle = new SelectBoxStyle(UIStyles.uiLabelStyle.font, Color.WHITE,
				clearbg, graybg, graybg);
		sbStyle.font = UIStyles.uiLabelStyle.font;

		// actors
		txtTotalBeats = new Label("Beats - ", UIStyles.uiLabelStyle);
		beatCount = new Label(String.valueOf(map.NORMAL.size()), UIStyles.uiLabelStyle);
		difficulty = new Label("Difficulty:", UIStyles.uiLabelStyle);
		title = new Label("Stage Ready", UIStyles.uiLabelStyle);

		difficultySelector = new SelectBox(new String[] { "Relaxed", "Regular",
				"Energetic", "Altered" }, sbStyle);
		difficultySelector.setSelection(1);
		difficultySelector.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SelectBox sb = (SelectBox) actor;

				switch (sb.getSelectionIndex()) {
				case 0:
					Playthrough.difficulty = Difficulty.EASY;
					totalBeats = map.EASY.size();
					break;
				case 1:
					Playthrough.difficulty = Difficulty.NORMAL;
					totalBeats = map.NORMAL.size();
					break;
				case 2:
					Playthrough.difficulty = Difficulty.HARD;
					totalBeats = map.HARD.size();
					break;
				case 3:
					Playthrough.difficulty = Difficulty.ORIGINAL;
					totalBeats = map.ORIGINAL.size();
					break;
				}

				beatCount.setText(String.valueOf(totalBeats));

			}
		});

		playButton = new TextButton("Play", tStyle);
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				Gdx.input.setInputProcessor(null);
				Resources.menuMusic.stop();
				Resources.game.setScreen(new GameScreen(map, fileHandle));
			}
		});

		// setup
		Table container = new Table();
		container.add(title);
		container.row();
		container.add(difficulty);
		container.add(difficultySelector);
		container.add(new Image(downArrow));
		container.row();
		container.add(txtTotalBeats);
		container.add(beatCount);
		container.row().fill();
		container.add(playButton);
		container.row();

		container.setFillParent(true);

		stage.addActor(container);
	}

	@Override
	public void render(float delta) {

		stage.act();
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
