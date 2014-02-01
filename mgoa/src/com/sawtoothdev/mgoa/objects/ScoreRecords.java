package com.sawtoothdev.mgoa.objects;

import java.security.MessageDigest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class ScoreRecords {
	
	FileHandle scoreFile;
	
	public ScoreRecords(){
		scoreFile = Gdx.files.local("scores");
	}

	/***
	 * Updates a song score record with the new value.
	 * If there is not already a record for the given song a
	 * new record is created.
	 * 
	 * @param audiofile The song file
	 * @param score
	 */
	public void writeScore(FileHandle audiofile, int score){
		String hash = MD5(audiofile.readBytes());
		String scoreval = String.valueOf(score);
		String record = hash + ":" + scoreval + "\n";
		
		String records = scoreFile.readString();
		
		boolean previousRecordExists = records.contains(hash);
		
		if (previousRecordExists) {
			while (records.contains(hash)){
				
				// TODO: fix this shit
				int recordStart = records.indexOf(hash);
				int recordEnd   = records.indexOf("\n", recordStart);
				String occurence = records.substring(recordStart, recordEnd);
				records.replace(occurence, "");
			}
		}
		
		scoreFile.writeString(record, true);

	}
	public int readScore(FileHandle audiofile){
		String records = scoreFile.readString();
		String hash = MD5(audiofile.readBytes());
		
		if (records.contains(hash)){
			int recordStart = records.indexOf(hash);
			int recordEnd   = records.indexOf("\n", recordStart);
			String record   = records.substring(recordStart, recordEnd);
			String value    = record.split(":")[1];
			int score = Integer.valueOf(value);
			return score;
		}
		else
			return -1;
	}
	private static String MD5(byte[] input) {
		
		String hash = null;
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
		    byte[] array = md.digest(input);
		    
		    StringBuffer sb = new StringBuffer();
		    for (int i = 0; i < array.length; ++i)
		    	sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		    
		    hash = sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) { }
		
		return hash;
	}
}
