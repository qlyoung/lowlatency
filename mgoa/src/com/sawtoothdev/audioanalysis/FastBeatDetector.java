package com.sawtoothdev.audioanalysis;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.badlogic.gdx.audio.analysis.FFT;
import com.badlogic.gdx.audio.io.Decoder;
import com.badlogic.gdx.audio.io.Mpg123Decoder;
import com.badlogic.gdx.audio.io.VorbisDecoder;
import com.badlogic.gdx.files.FileHandle;



/**
 * A lightweight beat detector integrated with LibGDX. Relies on LibGDX
 * decoder classes. Uses mpg123 for decoding and reworked pieces of
 * Minim for Fast Fourier transformations.
 * 
 * This class can handle mp3 and Ogg Vorbis files sampled at 44.1khz.
 * 
 * Works on Android since it doesn't rely on JavaSound, JavaX or the JMF.
 * 
 * @author albatross
 * 
 */


public class FastBeatDetector {


	public static class AudioFunctions {


		/***
		 * Calculates spectral fluxes of the file using 1024-sample windows
		 * @param f
		 * @return A collection containing the spectral fluxes between consecutive windows
		 * @throws IOException
		 */
		public static ArrayList<Float> getSpectralFluxes(FileHandle f) {

			Decoder decoder = null;

			String ext = f.extension().toLowerCase();

			if (ext.contains("mp3"))
				decoder = new Mpg123Decoder(f);
			else if (ext.contains("ogg"))
				decoder = new VorbisDecoder(f);


			// some collections and objects we'll need
			float[] currentSpectrum, previousSpectrum;
			FFT transformer = new FFT(1024, 44100);
			transformer.window(FFT.HAMMING);


			// make a list to hold the spectral fluxes
			ArrayList<Float> spectralFluxes = new ArrayList<Float>();

			currentSpectrum = new float[(1024 / 2) + 1];
			previousSpectrum = new float[(1024 / 2) + 1];


			// sequentially retrieve consecutive frames and calculate their spectral flux
			short[] samples = new short[2048];


			while (decoder.readSamples(samples, 0, samples.length) >= 2048) {

				short[] mergedFrames = mergeChannels(samples);

				// convert those short samples to float samples
				float[] frame = new float[mergedFrames.length];


				for (int i = 0; i < frame.length; i++) {
					frame[i] = (float) mergedFrames[i] / 32768f;
				}


				// heavy wizardry starts here
				transformer.forward(frame);
				
				System.arraycopy(currentSpectrum, 0, previousSpectrum, 0,
						currentSpectrum.length);
				System.arraycopy(transformer.getSpectrum(), 0, currentSpectrum,
						0, currentSpectrum.length);


				// calculate the spectral flux between previous and current window
				float flux = 0;
				for (int i = 0; i < currentSpectrum.length; i++) {
					float tFlux = (currentSpectrum[i] - previousSpectrum[i]);
					flux += tFlux > 0 ? tFlux : 0;
				}


				spectralFluxes.add(flux);
			}


			return spectralFluxes;
		}


		/**
		 * Performs onset detection on a set of spectral flux values
		 * 
		 * @param spectralFluxes
		 *            A collection containing consecutive spectral flux values
		 * @param sensitivity
		 *            How sensitive detection should be. A good value is 1.3
		 * @return An ArrayList<Float> containing a representation of the audio
		 *         file. There are approx. 43 values for every 1 second of
		 *         audio. All values are zero except where there are beats;
		 *         those values are the original sample values. The higher the
		 *         value the stronger the beat.
		 */
		public static ArrayList<Float> getPeaks(ArrayList<Float> spectralFluxes, float sensitivity) {


			ArrayList<Float> threshold = new ArrayList<Float>();


			{
				// This next bit calculates the threshold values for a range of ten
				// spectral fluxes. We'll use this later to find onsets.


				for (int i = 0; i < spectralFluxes.size(); i++) {
					int start = Math.max(0, i - 10);
					int end = Math.min(spectralFluxes.size() - 1, i + 10);
					float mean = 0;
					for (int j = start; j <= end; j++)
						mean += spectralFluxes.get(j);
					mean /= (end - start);
					threshold.add((float) mean * sensitivity);
				}
			}


			/*
			 * Right, now we have the threshold function, so let's pull out the
			 * values from the spectral flux function that are bigger than the
			 * threshold. What this does is it goes through each spectral flux
			 * value and checks to see if it radically exceeds those around it,
			 * using the threshold function we calculated in the previous block.
			 * For each spectral flux value it inspects, it adds a value to a
			 * new array called prunedSpectralFluxes. If it finds that the
			 * spectral flux value does indeed exceed those around it (ergo it
			 * exceeds its respective threshold value), it adds the difference
			 * between the threshold and the flux value to the list; if not, it
			 * adds a zero. Thus we get an ArrayList that parallels the entire
			 * audio track, and contains positive values where there are beats
			 * and zeros where there are none. The returned list holds approx.
			 * 43 values per second of audio.
			 */


			ArrayList<Float> prunedSpectralFluxes = new ArrayList<Float>();


			for (int i = 0; i < threshold.size(); i++) {
				if (threshold.get(i) <= spectralFluxes.get(i))
					prunedSpectralFluxes.add(spectralFluxes.get(i)
							- threshold.get(i));
				else
					prunedSpectralFluxes.add((float) 0);
			}


			/*
			 * So now, in prunedSpectralFluxes, we have values that are greater
			 * than their threshold. These will by necessity form up in a spike
			 * due to the nature of waveforms. --diagram omitted-- Now, we just
			 * want the peak value of the spike. So all we do is go through each
			 * value and see if it is bigger than the next value. If so, it's a
			 * peak.
			 */


			ArrayList<Float> peaks = new ArrayList<Float>();


			for (int i = 0; i < prunedSpectralFluxes.size() - 1; i++) {
				if (prunedSpectralFluxes.get(i) > prunedSpectralFluxes
						.get(i + 1))
					peaks.add(prunedSpectralFluxes.get(i));
				else
					peaks.add((float) 0);
			}


			/*
			 * And voila. ArrayList peaks now contains all of our beats. To
			 * calculate the time these peaks occur, simply multiply the index
			 * of the peak by (frame size / sampling rate) So here, to calculate
			 * the time, we would use (index * (1024 / 44100)) to get the time.
			 */


			return peaks;
		}


		public static short[] mergeChannels(short[] samples) {


			short[] merged = new short[(short) (samples.length / 2)];


			int unmergedIndex = 0, mergedIndex = 0;


			for (; unmergedIndex < samples.length; mergedIndex++) {
				merged[mergedIndex] = (short) ((samples[unmergedIndex] + samples[unmergedIndex + 1]) / 2);
				unmergedIndex += 2;
			}


			return merged;
		}
	
	}


	private static PrintStream debugStream;


	public static final float SENSITIVITY_AGGRESSIVE = 1.0f;
	public static final float SENSITIVITY_STANDARD = 1.4f;
	public static final float SENSITIVITY_LOW = 1.7f;

	/***
	 * Performs beat detection on this detector's audio file
	 * @param sensitivity How sensitive detection should be
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Beat> detectBeats(float sensitivity, FileHandle audioFile) throws IOException {

		long start_time = System.currentTimeMillis();

		{// make sure we have a valid file
			
			if (audioFile == null || !audioFile.exists())
				throw new IOException("Null FileHandle or bad path");
			
			if (!( audioFile.extension().toLowerCase().contains("mp3") || audioFile.extension().toLowerCase().contains("ogg")) )
				throw new IOException("Not a music file");
		}
		
		

		writeDebug("Calculating spectral flux values...");
		ArrayList<Float> spectralFluxes = AudioFunctions
				.getSpectralFluxes(audioFile);


		writeDebug("Detecting rhythmic onsets...");
		ArrayList<Float> peaks = AudioFunctions.getPeaks(
				(ArrayList<Float>) spectralFluxes, sensitivity);


		writeDebug("Formatting...");


		// Convert to time - energy map
		LinkedHashMap<Long, Float> timeEnergyMap = new LinkedHashMap<Long, Float>(15);
		{
			long i = 0;
			for (float f : peaks) {

				if (f > 0) {

					long timeInMillis = (long) (((float) i * (1024f / 44100f)) * 1000f);
					timeEnergyMap.put(timeInMillis, f);

				}

				i++;
			}
		}


		{// normalize values to range [0, 1]
			float max = 0;


			for (Float f : timeEnergyMap.values()){
				if (f > max)
					max = f;
			}


			float value = 0;
			for (Long l : timeEnergyMap.keySet()){
				value = timeEnergyMap.get(l);
				value /= max;
				timeEnergyMap.put(l, value);
			}


		}


		// store beats in a collection
		ArrayList<Beat> beats = new ArrayList<Beat>();
		{
			for (Long l : timeEnergyMap.keySet()){
				beats.add(new Beat(l, timeEnergyMap.get(l)));
			}
		}


		printResults(System.currentTimeMillis() - start_time,
				spectralFluxes.size(), beats.size());


		return beats;
	}


	/**
	 * Set the PrintStream to print debug information to
	 * 
	 * @param stream
	 *            The PrintStream. Can be null.
	 */
	public static void setDebugStream(PrintStream stream) {
		debugStream = stream;
	}


	private static void writeDebug(String message) {
		if (debugStream != null) {
			debugStream.println(message);
		}
	}
	private static void printResults(long time, int fluxCount, int beatCount) {
		writeDebug("\n---------Results---------");
		writeDebug("Time taken: " + String.valueOf(time / 1000l) + " seconds");
		writeDebug("Flux values: " + fluxCount);
		writeDebug("Beat values: " + beatCount);
		writeDebug("Song Percentage Beats: "
				+ String.valueOf(((float) beatCount / fluxCount) * 100) + "%");
		writeDebug("----Analysis Complete----");
	}
}

