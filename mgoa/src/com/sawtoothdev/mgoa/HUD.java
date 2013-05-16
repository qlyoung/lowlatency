package com.sawtoothdev.mgoa;

import java.io.IOException;

import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.myid3.MyID3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sawtoothdev.mgoa.BeatCore.Accuracy;

class HUD implements IGameObject {

	// fonts
	private BitmapFont messageFont = new BitmapFont();
	private BitmapFont scoreFont = new BitmapFont();
	
	// gfx
	private TextureRegion underline = new TextureRegion(new Texture("data/textures/underline.png"));
	private SpriteBatch hudBatch = new SpriteBatch();
	
	// game values
	private int score, displayScore;
	private String message = "READY";
	private int totalBeatsShown = 0, totalBeatsHit = 0;
	private String percentage = "0";
	
	// music information
	private final String songInfo;
	

	public HUD(FileHandle audioFile) {
		
		MusicMetadata metadata = null;
		
		try { metadata = new MyID3().read(audioFile.file()).merged;	}
		catch (IOException e) { Gdx.app.log("warning", "Cannot read metadata! Ogg file?"); }
		
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
			percentage = String.valueOf( (int) (((float)totalBeatsHit / totalBeatsShown) * 100)) + "%";
		
		
		{// draw the display
			// artist - track
			
			hudBatch.begin();
			
			scoreFont.draw(hudBatch, songInfo, 10, 20);
			
			// score and score underline
			scoreFont.draw(hudBatch, String.format("%08d", displayScore), Gdx.graphics.getWidth() / 2f - 25, Gdx.graphics.getHeight() - 10f);
			hudBatch.draw(underline, Gdx.graphics.getWidth() / 2f - underline.getRegionWidth() / 2f, Gdx.graphics.getHeight() - 40f);
			
			// hit percentage
			scoreFont.draw(hudBatch, percentage, 10f, Gdx.graphics.getHeight() - 10f);
			
			// messages
			messageFont.draw(hudBatch, message, Gdx.graphics.getWidth() / 2f - 10f, Gdx.graphics.getHeight() / 2f);
			
			hudBatch.end();
			
		}

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