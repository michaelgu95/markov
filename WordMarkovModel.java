import java.util.*;
import java.io.*;


public class WordMarkovModel extends AbstractModel {
	private String[] array;
	private String myString;
	private Random myRandom;
	public static final int DEFAULT_COUNT = 100; // default # random letters generated
	private ArrayList<WordNgram> myNgrams;
	private HashMap<WordNgram, ArrayList<WordNgram>> myMap;
	private int prevK = 0; // initialize previous k to 0 -> starting condition

	public WordMarkovModel() {
		myRandom = new Random(1234);
	}

	public void initialize(Scanner s) {
		double start = System.currentTimeMillis();
		int count = readChars(s);
		double end = System.currentTimeMillis();
		double time = (end - start) / 1000.0;
		super.messageViews("#read: " + count + " chars in: " + time + " secs");
	}

	protected int readChars(Scanner s) {
		myString = s.useDelimiter("\\Z").next();
		System.out.println(s);
		s.close();      
		return myString.length();
	}

	/**
	 * Generate N letters using an order-K markov process where
	 * the parameter is a String containing K and N separated by
	 * whitespace with K first. If N is missing it defaults to some
	 * value.
	 */
	public void process(Object o) {
		String temp = (String) o;
		String[] nums = temp.split("\\s+");
		int k = Integer.parseInt(nums[0]);
		int numLetters = DEFAULT_COUNT;
		if (nums.length > 1) {
			numLetters = Integer.parseInt(nums[1]);
		}
		if(prevK == k){ // if k same as before, don't recreate myMap
			generateChars(k, numLetters);
		}else{
			prevK = k;
			myMap = new HashMap<WordNgram, ArrayList<WordNgram>>();
			createMap(k);
			generateChars(k, numLetters);
		}
	}

	public void createMap(int k){
		array = myString.split("\\s+");
		int n = array.length;

		//handle wraparound
		ArrayList<String> wrap = new ArrayList<String>(Arrays.asList(array));
		for(int i=0; i<k; i++){
			String append = array[i];
			wrap.add(append);
		}
		array = wrap.toArray(new String[n]);

		// create map
		myNgrams= new ArrayList<WordNgram>();
		for(int i=0; i < n; i++){
			WordNgram g = new WordNgram(array,i,k);
			myNgrams.add(g);
			WordNgram next = new WordNgram(array, i+1, k);
			setInMap(g, next);
		}
		System.out.println("Number of keys: " + myMap.keySet().size());
	}

	public void generateChars(int k, int numLetters){
		int start = myRandom.nextInt(myNgrams.size()-k+1);

		WordNgram gram = new WordNgram(array, start, k);

		StringBuilder build = new StringBuilder();
		double stime = System.currentTimeMillis();
		for (int i = 0; i < numLetters; i++) {
			ArrayList<WordNgram> list = myMap.get(gram);
			String [] words = gram.getWords();

			WordNgram nextWords;
			String nextString;

			//select the next WordNgram
			int pick = myRandom.nextInt(list.size());
			nextWords = list.get(pick);


			//append the last string of the WordNgram 
			nextString = nextWords.getWord(k-1);
			build.append(nextString);
			build.append(" ");
			
			//set next gram as nextWords
			gram = nextWords;
		}
		double etime = System.currentTimeMillis();
		double time = (etime - stime) / 1000.0;
		this.messageViews("Time to generate: " + time);
		System.out.println("Number of keys: " + myMap.keySet().size());
		this.notifyViews(build.toString());
	}

	private void setInMap(WordNgram hash, WordNgram next){
		ArrayList<WordNgram> values = myMap.get(hash);
		if(values == null){
			values = new ArrayList<WordNgram>();
			myMap.put(hash, values);
		}
		values.add(next);
	}
}
