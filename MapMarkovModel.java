import java.util.*;
import java.io.*;


public class MapMarkovModel extends AbstractModel {


	private String myString;
	private Random myRandom;
	public static final int DEFAULT_COUNT = 100; // default # random letters generated
	private Map<String, ArrayList<Character>> myMap; //map to reduce runtime/lookups
	private int prevK = 0; // initialize previous k to 0 -> starting condition

	public MapMarkovModel() {

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
			myMap = new HashMap<String, ArrayList<Character>>();
			createMap(k);
			generateChars(k, numLetters);
		}
	}

	public void createMap(int k){
		String wrapAroundString = myString + myString.substring(0,k); 
		for(int i = 0; i<wrapAroundString.length()-k; i++){
			String str = wrapAroundString.substring(i, i+k);
			char next = wrapAroundString.charAt(i+k);
			if(myMap.containsKey(str)){
				ArrayList<Character> oldList = myMap.get(str);
				oldList.add(next);
				myMap.put(str, oldList);
			}else{
				ArrayList<Character> newList = new ArrayList<Character>();
				newList.add(next);
				myMap.put(str, newList);
			}
		}
	}

	public void generateChars(int k, int numLetters){
		int start = myRandom.nextInt(myString.length() - k + 1);
		String str = myString.substring(start, start + k);  
		StringBuilder build = new StringBuilder();
		double stime = System.currentTimeMillis();
		for (int i = 0; i < numLetters; i++) {
			ArrayList<Character> list = myMap.get(str);
			int pick = myRandom.nextInt(list.size());
			char ch = list.get(pick);
			build.append(ch);
			str = str.substring(1) + ch;
		}
		double etime = System.currentTimeMillis();
		double time = (etime - stime) / 1000.0;
		this.messageViews("Time to generate: " + time);
		this.notifyViews(build.toString());
	}
}
