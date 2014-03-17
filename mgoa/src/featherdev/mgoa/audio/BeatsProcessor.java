package featherdev.mgoa.audio;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Post processor for use with Beat collections returned by FastBeatDetector.
 * 
 * @author albatross
 */
public class BeatsProcessor {

	/***
	 * Processes a collection of beats to ensure a minimum amount of time is
	 * present between consecutive beats. If two consecutive beats are too close
	 * together, then the one with the higher energy will be kept.
	 * 
	 * Does not modify the collection passed to it.
	 * 
	 * @param beats
	 *            The collection to process
	 * @param minTimeBetween
	 *            The minimum time, in milliseconds, that must be present
	 *            between consecutive beats.
	 * @return a time-ordered list of beats
	 */
	public static LinkedList<Beat> removeCloseBeats(LinkedList<Beat> beats, long minTimeBetween) {

		LinkedList<Beat> result = new LinkedList<Beat>();

		ListIterator<Beat> iterator = beats.listIterator();
		result.add(iterator.next());
		
		while(iterator.hasNext()){
			Beat currBeat = iterator.next();
			Beat prevBeat = result.getLast();
		
			if (currBeat.timeMs - prevBeat.timeMs >= minTimeBetween)
				result.add(currBeat);
			else {
				Beat winner = currBeat.energy > prevBeat.energy ? currBeat : prevBeat;
				int index = result.size() - 1;
				result.set(index, winner);
			}
		}

		return result;
	}
	
	/**
	 * Returns only the beats with energy values greater than the threshold.
	 * @param beats
	 * @param threshold
	 * @return
	 */
	public static LinkedList<Beat> dropLowBeats(LinkedList<Beat> beats, float threshold){
		LinkedList<Beat> result = new LinkedList<Beat>();
		
		for (Beat b : beats){
			if (b.energy > threshold)
				result.add(b);
		}
		
		return result;
	}
}
