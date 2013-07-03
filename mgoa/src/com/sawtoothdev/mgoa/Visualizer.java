package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.analysis.KissFFT;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class Visualizer implements IGameObject {

	MusicPlayer player;
	KissFFT fft;
	int NUMBER_OF_BARS = 31;
	float barWidth = ((float) Gdx.graphics.getWidth() / (float) NUMBER_OF_BARS);
	Texture colors;
	float[] spectrum = new float[2048];
	OrthographicCamera camera = new OrthographicCamera();
	
	public Visualizer(MusicPlayer player){
		this.player = player;
		fft = new KissFFT(2048);
		colors = new Texture(Gdx.files.internal("data/textures/colors-borders.png"));
		camera.setToOrtho(false);
	}
	
	@Override
	public void render(float delta) {
		
		if (player.isPlaying()){
			short[] samples = player.getLatestSamples();
			fft.spectrum(samples, spectrum);

			
			camera.update();
			Resources.screenBatch.setProjectionMatrix(camera.combined);
			
			Resources.screenBatch.begin();
			
			for (int i = 0; i < NUMBER_OF_BARS; i++) {
				int histoX = 0;
				if (i < NUMBER_OF_BARS / 2) {
					histoX = NUMBER_OF_BARS / 2 - i;
				} else {
					histoX = i - NUMBER_OF_BARS / 2;
				}

				int nb = (samples.length / NUMBER_OF_BARS) / 2;

				// drawing spectrum (in blue)
				Resources.screenBatch.draw(colors, i * barWidth, 0, barWidth, scale(avg(histoX, nb)), 0, 0, 16, 5, false, false);

			}
			
			Resources.screenBatch.end();
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
