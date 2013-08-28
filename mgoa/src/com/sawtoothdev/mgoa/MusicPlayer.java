package com.sawtoothdev.mgoa;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.io.Decoder;
import com.badlogic.gdx.audio.io.Mpg123Decoder;
import com.badlogic.gdx.audio.io.VorbisDecoder;
import com.badlogic.gdx.files.FileHandle;

public class MusicPlayer implements IGameObject {

	AudioDevice device;
	Decoder decoder;

	ArrayList<short[]> frames = new ArrayList<short[]>();
	int index = 0;
	
	short[] buffer = new short[2048];

	boolean playing = false;

	public MusicPlayer(FileHandle audiofile) {

		if (audiofile.extension().contains("mp3"))
			decoder = new Mpg123Decoder(audiofile);
		else if (audiofile.extension().contains("ogg"))
			decoder = new VorbisDecoder(audiofile);

		device = Gdx.audio.newAudioDevice(decoder.getRate(),
				decoder.getChannels() == 1);

		// this needs to be done in chunks on the fly, it results in the smoothest audio yet.
		
		for (int i = 0; i < 500; i++){
			decoder.readSamples(buffer, 0, buffer.length);
			frames.add(buffer.clone());
		}
		
	}

	@Override
	public void update(float delta) {

		
		
		if (playing)
		{
			device.writeSamples(frames.get(index), 0, frames.get(index).length);
			index++;
		}

	}

	public void play() {
		playing = true;
	}

	public void stop() {
		playing = false;
	}

	public short[] getLatestFrame() {
		return buffer;
	}

	public boolean isPlaying() {
		return playing;
	}

}