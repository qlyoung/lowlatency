package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.io.Decoder;
import com.badlogic.gdx.audio.io.Mpg123Decoder;
import com.badlogic.gdx.audio.io.VorbisDecoder;
import com.badlogic.gdx.files.FileHandle;

public class MusicPlayer {

	public enum PlayerState {
		PLAYING, PAUSED, STOPPED, FINISHED
	};
	
	class PlayerThread extends Thread {
		
		private Decoder decoder;
		private AudioDevice device;
		private FileHandle audioHandle;
		
		private boolean virgin = true;
		
		private short[] samples = new short[2048];
		int sampleCount = 0;
		
		public PlayerThread(FileHandle audioHandle) {
			this.audioHandle = audioHandle;
			
			String ext = audioHandle.extension().toLowerCase();

			if (ext.contains("mp3"))
				decoder = new Mpg123Decoder(audioHandle);
			else if (ext.contains("ogg"))
				decoder = new VorbisDecoder(audioHandle);
			else
				return;

			// initialize device
			device = Gdx.audio.newAudioDevice(decoder.getRate(),
					decoder.getChannels() == 1);
		}
		
		@Override
		public void run() {
			int readSamples = 0;
			
			while (true){
				
				// allow other threads to execute
				Thread.yield();
				
				if (state == PlayerState.PLAYING){
					
					if (virgin)
						virgin = false;
					
					readSamples = decoder.readSamples(samples, 0, samples.length);
					
					if (readSamples > 0) {
						device.writeSamples(samples, 0, readSamples);
						sampleCount += readSamples;
					}
					else {
						state = PlayerState.FINISHED;
						reinitialize();
					}
				}
				else if (state == PlayerState.FINISHED) {
					if (looping && !virgin)
						state = PlayerState.PLAYING;
				}
			}
		}
		
		private void reinitialize(){
			if (decoder instanceof Mpg123Decoder)
				decoder = new Mpg123Decoder(audioHandle);
			else if (decoder instanceof VorbisDecoder)
				decoder = new VorbisDecoder(audioHandle);
			
			device = Gdx.audio.newAudioDevice(decoder.getRate(),
					decoder.getChannels() == 1);
				
		}
		
	}

	private boolean looping;
	
	private PlayerState state;
	private PlayerThread playerThread;

	public MusicPlayer(FileHandle songHandle) {

		// set the player to stopped
		state = PlayerState.STOPPED;

		playerThread = new PlayerThread(songHandle);
		playerThread.setDaemon(true);
	
	}

	public void play() {
		if (!playerThread.isAlive())
			playerThread.start();
		
		state = PlayerState.PLAYING;
	}

	public void pause() {
		state = PlayerState.PAUSED;
	}

	public void stop() {
		state = PlayerState.STOPPED;
	}

	public boolean isPlaying() {
		return state == PlayerState.PLAYING;
	}
	
	public short[] getLatestSamples() {
		return playerThread.samples;
	}

	public void setLooping(boolean looping){
		this.looping = looping;
	}
}
