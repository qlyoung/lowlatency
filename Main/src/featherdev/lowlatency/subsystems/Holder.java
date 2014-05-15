package featherdev.lowlatency.subsystems;

import java.util.LinkedList;

import featherdev.lwbd.Beat;
import featherdev.lowlatency.objects.Difficulty;
import featherdev.lowlatency.objects.Song;

public class Holder {
	
	public static Difficulty difficulty;
	public static LinkedList<Beat> beatmap;
	public static LinkedList<Beat> rawmap;
	public static Song song;
	public static boolean visualizer = false;
	
	public static void clear(){
		difficulty = null;
		beatmap = null;
		rawmap = null;
		song = null;
		visualizer = false;
	}

}
