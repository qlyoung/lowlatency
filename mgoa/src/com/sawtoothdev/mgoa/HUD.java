package com.sawtoothdev.mgoa;

import java.io.IOException;

import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.myid3.MyID3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

class HUD implements IGameObject {

	// fonts
	private BitmapFont messageFont = new BitmapFont(Gdx.files.internal("data/fonts/typeone.fnt"), false);
	private BitmapFont hudFont = new BitmapFont(Gdx.files.internal("data/fonts/naipol.fnt"), false);

	// gfx
	private TextureRegion underline = new TextureRegion(new Texture("data/textures/underline.png"));

	// game values
	private int score, displayScore, totalBeatsShown, totalBeatsHit, combo;
	private String message = null, percentage = "0";

	// music information
	private final String songInfo;

	public HUD(FileHandle audioFile) {
		

		MusicMetadata metadata = null;

		try {
			metadata = new MyID3().read(audioFile.file()).merged;
		} catch (IOException e) {
			Gdx.app.log("warning", "Cannot read metadata! Ogg file?");
		}

		String title = metadata.getSongTitle() == null ? "Unknown" : metadata.getSongTitle();
		String artist = metadata.getArtist() == null ? "Unknown" : metadata.getArtist();

		songInfo = artist + " - " + title;

	}

	@Override
	public void render(float delta) {

		// update the score spinner
		if (displayScore < score)
			displayScore += 5;
		else if (displayScore > score)
			displayScore = score;

		// update the message fade
		Color c = messageFont.getColor();
		if (c.a > 0f) {
			float alpha = (c.a - delta) < 0 ? 0 : c.a - delta;
			messageFont.setColor(c.r, c.g, c.b, alpha);
		}

		// calculate the hit percentage
		if (totalBeatsShown != 0)
			percentage = String.valueOf((int) (((float) totalBeatsHit / totalBeatsShown) * 100)) + "%";

		
		Resources.screenBatch.begin();
		{
			// song data
			hudFont.draw(Resources.screenBatch, songInfo, 10, 23);

			// score and score underline
			hudFont.draw(Resources.screenBatch, String.format("%08d", displayScore), Gdx.graphics.getWidth() / 2f - 60, Gdx.graphics.getHeight() - 8f);
			Resources.screenBatch.draw(underline, Gdx.graphics.getWidth() / 2f - underline.getRegionWidth() / 2f, Gdx.graphics.getHeight() - 40f);

			// hit percentage
			hudFont.draw(Resources.screenBatch, percentage, 10f, Gdx.graphics.getHeight() - 10f);

			// messages
			if (message != null)
				messageFont.draw(Resources.screenBatch, message, Gdx.graphics.getWidth() / 2f - 70f, Gdx.graphics.getHeight() / 2f);

			// combo
			hudFont.draw(Resources.screenBatch, String.valueOf("Combo: " + String.format("%03d", combo)), Gdx.graphics.getWidth() - 130, Gdx.graphics.getHeight() - 10f);
			
			// fps counter
			hudFont.draw(Resources.screenBatch, "FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()), Gdx.graphics.getWidth() - 100, 20);
			
		}
		Resources.screenBatch.end();

	}

	public void showMessage(String message) {
		this.message = message;
		messageFont.setColor(Color.WHITE);
	}
	
	public void update(int totalBeatsShown, int totalBeatsHit, int combo, int score){
		this.totalBeatsShown = totalBeatsShown;
		this.totalBeatsHit = totalBeatsHit;
		this.combo = combo;
		this.score = score;
	}

}
