package com.sawtoothdev.mgoa.game;

import java.io.IOException;

import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.myid3.MyID3;

import adamb.vorbis.VorbisCommentHeader;
import adamb.vorbis.VorbisIO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sawtoothdev.mgoa.IDrawable;

class HUD implements IDrawable {

	// fonts
	private BitmapFont messageFont = new BitmapFont(Gdx.files.internal("data/fonts/typeone.fnt"), false);
	private BitmapFont hudFont = new BitmapFont(Gdx.files.internal("data/fonts/naipol.fnt"), false);

	// gfx
	private TextureRegion bottomFadeBar = new TextureRegion(new Texture("data/textures/fadebar_bottom.png"));

	// guts
	final OrthographicCamera camera;
	
	// game values
	private int score, displayScore;
	private String message = null;

	// music information
	private final String songInfo;

	public HUD(FileHandle audioFile, OrthographicCamera camera) {
		this.camera = camera;
		
		String title = null, artist = null;
		
		if (audioFile.extension().toLowerCase().contains("mp3")){
			MusicMetadata metadata = null;
			
			try { metadata = new MyID3().read(audioFile.file()).merged; }
			catch (IOException e) { Gdx.app.log("hud", "mp3 metadata read failed"); }
			
			title = metadata == null || metadata.getSongTitle() == null ? "Unknown" : metadata.getSongTitle();
			artist = metadata == null || metadata.getArtist() == null ? "Unknown" : metadata.getArtist();
		}
		else if (audioFile.extension().toLowerCase().contains("ogg")) {
			VorbisCommentHeader comments = null;
			
			try {comments = VorbisIO.readComments(audioFile.file());}
			catch (IOException e) { Gdx.app.log("hud", "ogg metadata read failed"); }
			
			title = comments == null ? "Unknown" : comments.fields.get(0).value;
			artist = comments == null ? "Unknown" : comments.fields.get(1).value;
		}



		songInfo = artist + " - " + title;
	}

	@Override
	public void update(float delta) {

		// update the score spinner
		if (displayScore < score)
			displayScore += 17;
		else if (displayScore > score)
			displayScore = score;

		// update the message fade
		Color c = messageFont.getColor();
		if (c.a > 0f) {
			float alpha = (c.a - delta) < 0 ? 0 : c.a - delta;
			messageFont.setColor(c.r, c.g, c.b, alpha);
		}

	}
	@Override
	public void draw(SpriteBatch batch){
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		{
			// gfx
			batch.draw(bottomFadeBar, 0, 0);
			
			// song data
			hudFont.draw(batch, songInfo, 10, 23);

			// score
			hudFont.draw(batch, String.format("%08d", displayScore), Gdx.graphics.getWidth() - 140f, 20);

			// messages
			if (message != null){
				float length = messageFont.getBounds(message).width;
				messageFont.draw(batch, message, Gdx.graphics.getWidth() / 2f - (length / 2f), Gdx.graphics.getHeight() / 2f);
			}
			
			
		}
		batch.end();
	}

	public void showMessage(String message) {
		this.message = message;
		messageFont.setColor(Color.WHITE);
	}
	
	public void updateDisplay(int totalBeatsShown, int totalBeatsHit, int combo, int score){
		this.score = score;
	}

}
