package featherdev.mgoa.subsystems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import featherdev.mgoa.Utilities;

public class ScoreRecords {
	
	private static ScoreRecords instance;
	public static ScoreRecords instance(){
		if (instance == null)
			instance = new ScoreRecords();
		return instance;
	}
	
	FileHandle scoreFile;
	
	private ScoreRecords(){
		scoreFile = Gdx.files.local("scores");
		if (scoreFile.exists())
			Gdx.app.log("[+]", "Scorefile found");
		else {
			Gdx.app.log("[+]", "New scorefile created");
			scoreFile.writeString("", false);
		}
		
	}

	private void deleteRecord(String hash){
		String scores = scoreFile.readString();
		String regex = hash + ":[0-9]*\n";
		String updated = scores.replaceAll(regex, "");
		scoreFile.writeString(updated, false);
	}
	
	public void writeScore(FileHandle audiofile, int score){
		String hash = Utilities.MD5(audiofile.readBytes());
		String val = String.valueOf(score);
		String record = hash + ":" + val + "\n";
		
		deleteRecord(hash);
		scoreFile.writeString(record, true);

	}
	public int readScore(FileHandle audiofile){
		String records = scoreFile.readString();
		String hash = Utilities.MD5(audiofile.readBytes());
		
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
