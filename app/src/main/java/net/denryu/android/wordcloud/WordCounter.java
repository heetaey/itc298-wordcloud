package net.denryu.android.wordcloud;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Joe on 7/16/2017.
 */

public class WordCounter {

    private Map<String, Integer> wordCountMap;

    public Map<String, Integer> getWordCountMap() {
        return wordCountMap;
    }

    public void countWords(String in){
        Map<String, Integer> wordCountMap = new TreeMap<String, Integer>();

        String[] allWords = in.split("((\\b[^\\s]+\\b)((?<=\\.\\w).)?)");

        for( int i = 0; i <= allWords.length - 1; i++){
            String word = allWords[i].toLowerCase();
            if (!wordCountMap.containsKey(word)) {
                // never seen this word before

                wordCountMap.put(word, 1);
            } else {
                // seen this word before; increment count

                int count = wordCountMap.get(word);

                wordCountMap.put(word, count + 1);
            }
            this.wordCountMap = wordCountMap;

        }



    }

}
