package featherdev.mgoa.objects;

import java.security.MessageDigest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class ScoreRecords {
	
	FileHandle scoreFile;
	
	public ScoreRecords(){
		scoreFile = Gdx.files.local("scores");
		if (scoreFile.exists())
			System.out.println("[+] Scorefile found");
		else {
			System.out.println("[+] New scorefile created");
			scoreFile.writeString("", false);
		}
		
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
	private void deleteRecord(String hash){
		String scores = scoreFile.readString();
		String regex = hash + ":[0-9]*\n";
		String updated = scores.replaceAll(regex, "");
		scoreFile.writeString(updated, false);
	}
	
	public void writeScore(FileHandle audiofile, int score){
		String hash = MD5(audiofile.readBytes());
		String val = String.valueOf(score);
		String record = hash + ":" + val + "\n";
		
		deleteRecord(hash);
		scoreFile.writeString(record, true);

	}
	public int readScore(FileHandle audiofile){
		String records = scoreFile.readString();
		String hash = MD5(audiofile.readBytes());
		
		if (records.contains(hash))
			System.out.println("[+] Found highscore");
		
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

}
