package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PreviewScreen implements Screen {

	private Stage stage = new Stage();
	Label difficulty, txtTotalBeats, beatCount, length;
	SelectBox difficultySelector;
	TextButton playButton;
	
	private boolean LOCKED_ON = false;
	
	private int totalBeats = 0;
	
	
	public PreviewScreen(final BeatMap map, final FileHandle fileHandle) {
		
		Gdx.input.setInputProcessor(this.stage);
		
		// styles
		LabelStyle lStyle = new LabelStyle();
		lStyle.font = new BitmapFont(Gdx.files.internal("data/fonts/naipol.fnt"), false);
		TextButtonStyle tStyle = new TextButtonStyle();
		tStyle.font = lStyle.font;
		
		TextureRegionDrawable downArrow = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/textures/ui/downarrow.png"))));
		TextureRegionDrawable graybg = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/textures/ui/dropdown-bg.png"))));
		TextureRegionDrawable clearbg = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/textures/ui/clear-bg.png"))));
		SelectBoxStyle sbStyle = new SelectBoxStyle(lStyle.font, 
				Color.WHITE, 
				clearbg,
				graybg,
				graybg);
		sbStyle.font = lStyle.font;
		
		// actors
		txtTotalBeats = new Label("Total beats - ", lStyle);
		beatCount = new Label(String.valueOf(map.NORMAL.size()), lStyle);
		
		difficulty = new Label("Difficulty:", lStyle);
		
		difficultySelector = new SelectBox(new String[] {"Easy", "Normal", "Hard", "Insane"}, sbStyle);
		difficultySelector.setSelection(1);
		difficultySelector.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SelectBox sb = (SelectBox) actor;
				
				switch (sb.getSelectionIndex()){
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
		playButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// this locked_on deal is to avoid multiple playscreens getting created
				// since this clicked event can be triggered while the playscreen is loading
				
				if (!LOCKED_ON){
					LOCKED_ON = true;
					Resources.menuMusic.stop();
					Resources.game.setScreen(new PlayScreen(map, fileHandle));
				}
				else
					super.clicked(event, x, y);
			}
		});
		
		// setup
		Table container = new Table();
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
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
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
