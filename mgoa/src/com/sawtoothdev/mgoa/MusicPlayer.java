package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.io.Decoder;
import com.badlogic.gdx.audio.io.Mpg123Decoder;
import com.badlogic.gdx.audio.io.VorbisDecoder;
import com.badlogic.gdx.files.FileHandle;

public class MusicPlayer {
	
	public enum PlayerState {PLAYING, PAUSED, STOPPED};
	
	private PlayerState state;
	private Decoder decoder;
	private AudioDevice device;
	Thread playerThread;
	
	private short[] samples = new short[2048];
	int sampleCount = 0;
	
	
	public MusicPlayer(FileHandle songHandle)  {
		
		// initialize decoder
		String ext = songHandle.extension().toLowerCase();
		
		if (ext.contains("mp3"))
			decoder = new Mpg123Decoder(songHandle);
		else if (ext.contains("ogg"))
			decoder = new VorbisDecoder(songHandle);
		else
			return;
		
		// initialize device
		device = Gdx.audio.newAudioDevice(decoder.getRate(), decoder.getChannels() == 1);

		// set the player to stopped
		state = PlayerState.STOPPED;
		
		// make a new thread
		playerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				int readSamples = 0;
				
				while (state == PlayerState.PLAYING && (readSamples = decoder.readSamples(samples, 0, samples.length)) > 0){
					device.writeSamples(samples, 0, readSamples);
					sampleCount += readSamples;
				}
				
				
				state = PlayerState.STOPPED;
			}
		});
		playerThread.setDaemon(true);

	}
	
	public short[] getLatestSamples(){
		return samples;
	}

	public void play(){
		this.state = PlayerState.PLAYING;
		playerThread.start();
	}
	public void pause(){
		this.state = PlayerState.PAUSED;
	}
	public void stop(){
		this.state = PlayerState.STOPPED;
	}
	
	public boolean isPlaying(){
		return state == PlayerState.PLAYING;
	}
	public long getPosition(){
		// calculate the current song time in milliseconds
		long time = (long) (((float) sampleCount / decoder.getRate()) * 1000);
		
		System.out.println(time);
		return time;
	}
}
