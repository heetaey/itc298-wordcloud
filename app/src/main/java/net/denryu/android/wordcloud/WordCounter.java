package net.denryu.android.wordcloud;

//import java.util.Comparator;
//import java.util.Map;
//import java.util.Set;
//import java.util.TreeMap;
//import java.io.*;
import java.util.*;
import java.util.stream.*;
//import android.util.Log;

/**
 * Created by Joe on 7/16/2017.
 */

public class WordCounter {

    private Map<String, Integer> wordCountMap;
    private int countOfWords;
    public String mostCommonWord;
    public double appearanceRate;

    public Map<String, Integer> getWordCountMap() {
        return wordCountMap;
    }

    public void countWords(String in){
        Map<String, Integer> wordCountMap = new TreeMap<String, Integer>();

        
        //this regex uses all non alpha-numeric characters (plus apostrophes) as delimiters
        String[] allWords = in.split("[^a-zA-Z0-9']");

        countOfWords = allWords.length;
        for( int i = 0; i <= countOfWords - 1; i++){
            String word = allWords[i].toLowerCase();
            if (!wordCountMap.containsKey(word)) {
                // never seen this word before

                wordCountMap.put(word, 1);
            } else {
                // seen this word before; increment count

                int count = wordCountMap.get(word);

                wordCountMap.put(word, count + 1);
            }
        }
        this.wordCountMap = wordCountMap;
        setMostCommonWord();
    }

    private void setMostCommonWord() {
        String mostCommon = "";
        int highestCount = 0;

        int currCount = 0;
        //iterate through each word in the map to find the highest count
        for (String word : wordCountMap.keySet()) {
            currCount = wordCountMap.get(word);
            if (currCount > highestCount) {
                mostCommon = word;
                highestCount = currCount;
            }
        }
        mostCommonWord = mostCommon;
        appearanceRate = 1.0 * highestCount / countOfWords;
    }

    public int distinctWordCount() {
        return wordCountMap.size();
    }

    public int totalWordCount() {
        return countOfWords;
    }

}
