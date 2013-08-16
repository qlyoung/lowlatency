package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.analysis.KissFFT;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Visualizer {

	final KissFFT fft;
	final float[] spectrum;

	final Texture colors = new Texture(
			Gdx.files.internal("data/textures/colors-borders.png"));
	
	private OrthographicCamera camera = new OrthographicCamera();

	/**
	 * A frame-by-frame music visualizer
	 * @param frameSize The size of the sample frames you'll be passing to this class.
	 * You must pass this size frame!
	 */
	public Visualizer(int frameSize) {
		fft = new KissFFT(frameSize);
		spectrum = new float[frameSize];
		
		camera.setToOrtho(false);
	}

	/**
	 * Renders a single sample frame
	 * @param samples The samples to render
	 * @param bars The amount of bars in the visualizer
	 * @param color The color to render in
	 */
	public void renderFrame(short[] samples, int bars, Color color, SpriteBatch batch) {
		
		float barWidth = ((float) Gdx.graphics.getWidth() / (float) bars);
		fft.spectrum(samples, spectrum);

		
		for (int i = 0; i < bars; i++) {
			int histoX = 0;
			if (i < bars / 2) {
				histoX = bars / 2 - i;
			} else {
				histoX = i - bars / 2;
			}

			int nb = (samples.length / bars) / 2;
			float avg = avg(histoX, nb);
			
			color = Color.WHITE;
			
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			{
				batch.setColor(color);
				batch.draw(colors, i * barWidth, 0, barWidth, scale(avg), 0, 0, 16, 5, false, false);
				batch.setColor(Color.WHITE);
			}
			batch.end();
		}
		
	}
	
	private float scale(float x) {
		return x / 256 * Gdx.graphics.getHeight() * 2.0f;
	}
	private float avg(int pos, int nb) {
		int sum = 0;
		for (int i = 0; i < nb; i++) {

			sum += spectrum[pos + i];
		}

		return (float) (sum / nb);
	}

}
