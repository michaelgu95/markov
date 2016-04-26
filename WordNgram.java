import java.util.Arrays;

/*
 * This class encapsulates N words/strings so that the
 * group of N words can be treated as a key in a map or an
 * element in a set, or an item to be searched for in an array.
 * <P>
 * @author Michael Gu, gum5606
 */

public class WordNgram implements Comparable<WordNgram>{
    
    private String[] myWords;
    
    /*
     * Store the n words that begin at index start of array list as
     * the N words of this N-gram.
     * @param list contains at least n words beginning at index start
     * @start is the first of the N worsd to be stored in this N-gram
     * @n is the number of words to be stored (the n in this N-gram)
     */
    public WordNgram(String[] list, int start, int n) {
        myWords = new String[n];
        System.arraycopy(list, start, myWords, 0, n);
    }
    
    // overloaded constructor to allow for wrapping around
    public WordNgram(String[] list){
    	myWords = list;
    }
    
    /**
     * Return value that meets criteria of compareTo conventions.
     * @param wg is the WordNgram to which this is compared
     * @return appropriate value less than zero, zero, or greater than zero
     */
    public int compareTo(WordNgram wg) {
       if(this.getLength() != wg.getLength()){
    	   return this.getLength() - wg.getLength();
       }
       int compare = 0;
       
       for(int i = 0; i< getLength(); i++){
    	   if(getWord(i).equals(wg.getWord(i))){
    		   compare = 0;
    	   }else{
    		   return getWord(i).compareTo(wg.getWord(i));
    	   }
       }
       return compare;
    }
    
    /**
     * Return true if this N-gram is the same as the parameter: all words the same(and in same order)
     * @param o is the WordNgram to which this one is compared
     * @return true if o is equal to this N-gram
     */
    public boolean equals(Object o){
    	WordNgram wg = (WordNgram) o;
    	return this.compareTo(wg) == 0;
    }
    
    /**
     * Returns a good value for this N-gram to be used in hashing.
     * @return value constructed from all N words in this N-gram
     */
    public int hashCode(){
    	int hash = 7;
    	for (String w: myWords) {
    	    hash = hash*31 + w.hashCode()*17;
    	}
    	return hash;
    }
    
    public String[] getWords(){
    	return myWords;
    }
    
    public String stringify(){
    	StringBuilder builder = new StringBuilder();
    	for(String s : myWords) {
    	    builder.append(s);
    	    builder.append(" ");
    	}
    	return builder.toString();
    }
    
    public int getLength(){
    	return myWords.length;
    }
    
    public String getWord(int i){
    	return myWords[i];
    }
}
