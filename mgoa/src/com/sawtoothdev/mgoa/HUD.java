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
import com.badlogic.gdx.math.Vector2;
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
	private final String songInfo;
	

	public HUD(FileHandle audioFile) {
		
		MusicMetadata metadata = null;
		try {
			metadata = new MyID3().read(audioFile.file()).merged;
		} catch (IOException e) {
			Gdx.app.log("error", "bad file");
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
			percentage = String.valueOf( (int) (((float)totalBeatsHit / totalBeatsShown) * 100)) + "%";
		
		Vector2 position = new Vector2();
		
		// draw the display
		{
			// artist - track
			position = Resources.projectToScreen(new Vector2(-4.9f, -2.75f));
			scoreFont.draw(Resources.spriteBatch, songInfo, position.x, position.y);
			
			// score and score underline
			position = Resources.projectToScreen(new Vector2(-.4f, 2.9f));
			scoreFont.draw(Resources.spriteBatch, String.format("%08d", displayScore), position.x, position.y);
			
			position = Resources.projectToScreen(new Vector2(-3.3f, 2.5f));
			Resources.spriteBatch.draw(underline, position.x, position.y);
			
			// messages
			position = Resources.projectToScreen(new Vector2(0, 0));
			messageFont.draw(Resources.spriteBatch, message, position.x, position.y);
			
			// hit percentage
			position = Resources.projectToScreen(new Vector2(-4.9f, 2.9f));
			scoreFont.draw(Resources.spriteBatch, percentage, position.x, position.y);
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