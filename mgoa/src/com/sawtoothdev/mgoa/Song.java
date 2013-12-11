package com.sawtoothdev.mgoa;

import java.io.IOException;

import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.myid3.MyID3;

import adamb.vorbis.VorbisCommentHeader;
import adamb.vorbis.VorbisIO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Song {

	private FileHandle file;
	private String title, artist;
	
	public Song(FileHandle file){
		
		this.file = file;
		
		
		if (file.extension().toLowerCase().contains("mp3")){
			MusicMetadata metadata = null;
			
			try { metadata = new MyID3().read(file.file()).merged; }
			catch (IOException e) { Gdx.app.log("song", "mp3 metadata read failed"); }
			
			if (metadata != null){
				title = metadata.getSongTitle();
				artist = metadata.getArtist();
			}
			
		}
		else if (file.extension().toLowerCase().contains("ogg")) {
			VorbisCommentHeader comments = null;
			
			try {comments = VorbisIO.readComments(file.file());}
			catch (IOException e) { Gdx.app.log("song", "ogg metadata read failed"); }
			
			if (comments != null){
				title = comments.fields.get(0).value;
				artist = comments.fields.get(1).value;
			}
		}
		
		if (title == null)
			title = "Unknown";
		if (artist == null)
			artist = "Unknown";
	}
	
	public String getTitle(){
		return title;
	}
	public String getArtist(){
		return artist;
	}
	public FileHandle getHandle(){
		return file;
	}
	
}
