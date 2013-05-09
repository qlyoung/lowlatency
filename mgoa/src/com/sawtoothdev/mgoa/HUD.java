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
import com.sawtoothdev.mgoa.BeatCore.Accuracy;

class HUD implements IGameObject {

	// fonts
	private BitmapFont messageFont = new BitmapFont();
	private BitmapFont scoreFont = new BitmapFont();

	// gfx
	private TextureRegion underline = new TextureRegion(new Texture("data/textures/underline.png"));

	// game values
	private int score, displayScore;
	private String message = "READY";
	private int totalBeatsShown = 0, totalBeatsHit = 0;
	private String percentage = "0";
	
	// music information
	private final String songTitle, artist;
	

	public HUD(FileHandle audioFile) {
		
		MusicMetadata metadata = null;
		try {
			metadata = new MyID3().read(audioFile.file()).merged;
		} catch (IOException e) {
			Gdx.app.log("error", "bad file");
		}
		
		songTitle = metadata.getSongTitle() == null ? "Unknown" : metadata.getSongTitle();
		artist = metadata.getArtist() == null ? "Unknown" : metadata.getArtist();
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
			percentage = String.valueOf( (int) (((float)totalBeatsHit / totalBeatsShown) * 100)) + "%";
		
		
		// draw the display
		scoreFont.draw(Resources.spriteBatch, artist + " - " + songTitle, Gdx.graphics.getWidth() / 2f - 80, 20);
		Resources.spriteBatch.draw(underline, Gdx.graphics.getWidth() / 2f - underline.getRegionWidth() / 2f, 438);
		messageFont.draw(Resources.spriteBatch, message, 380, 250);
		scoreFont.draw(Resources.spriteBatch, String.format("%08d", displayScore), Gdx.graphics.getWidth() / 2f - 25, 465);
		scoreFont.draw(Resources.spriteBatch, percentage, 10, 465);

	}

	public void incrementTotalBeatsShown(){
		totalBeatsShown++;
	}
	public void showMessage(String message) {
		this.message = message;
		messageFont.setColor(Color.WHITE);
	}
	public void actuateHitEvent(Accuracy accuracy, int scoreValue){
		showMessage(accuracy.toString());
		score += scoreValue;
		totalBeatsHit++;
	}

}