package net.denryu.android.wordcloud;

import net.alhazmy13.wordcloud.WordCloud;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.*;

public class WordCounter {

    private Map<String, Integer> wordCountMap;
    private int countOfWords;
    public String mostCommonWord;
    public double appearanceRate;

    public WordCounter(String text) {
        this.countWords(text);
    }
    public WordCounter(Map<String, Integer> wordCountMap) {
        this.wordCountMap = wordCountMap;
    }

    public Map<String, Integer> getWordCountMap() {
        return wordCountMap;
    }
    public void setWordCountMap(Map<String, Integer> wcm) {
        wordCountMap = wcm;
    }

    public boolean isFiftyMostCommon(String words) {
        String[] wordList = {
                "the", "be", "to", "of", "and", "a", "in", "that", "have", "i",
                "it", "for", "not", "on", "with", "he", "as", "you", "do", "at",
                "this", "but", "his", "by", "from", "they", "we", "say", "her", "she",
                "or", "an", "will", "my", "one", "all", "would", "there", "their", "what",
                "so", "up", "out", "if", "about", "who", "get", "which", "go", "me",
                };
        for (String word : wordList) {
            if (words.equalsIgnoreCase(word))
                return true;
        }
        return false;
    }

    public boolean isSecondFiftyMostCommon(String words) {
        String[] wordList = {
                "when", "make", "can", "like", "time", "no", "just", "him", "know", "take",
                "people", "into", "year", "your", "good", "some", "could", "them", "see", "other",
                "than", "then", "now", "look", "only", "come", "its", "over", "think", "also",
                "back", "after", "use", "two", "how", "our", "work", "first", "well", "way",
                "even", "new", "want", "because", "any", "these", "give", "day", "most", "us",
                "so", "up", "out", "if", "about", "who", "get", "which", "go", "me",
                };
        for (String word : wordList) {
            if (words.equalsIgnoreCase(word))
                return true;
        }
        return false;
    }

    public boolean isCommonPreposition(String words) {
        String[] wordList = {
                "to", "of", "in", "for", "on", "with", "at", "by", "from", "up",
                "about", "into", "over", "after", "through", "after", "over", "between", "out", "against",
                "during", "without", "before", "under", "around", "among"
        };
        for (String word : wordList) {
            if (words.equalsIgnoreCase(word))
                return true;
        }
        return false;
    }

    public boolean isAuxiliaryVerb(String words) {
        String[] wordList = {
                "be", "am", "are", "is", "was", "were", "being", "been",
                "can", "could",
                "do", "does", "did",
                "have", "has", "had", "having",
                "may", "might", "must", "shall", "should", "will", "would"
        };
        for (String word : wordList) {
            if (words.equalsIgnoreCase(word))
                return true;
        }
        return false;
    }

    public void countWords(String in) {
        //this regex uses all non alpha-numeric characters (plus apostrophes) as delimiters
        String[] allWords = in.split("[^a-zA-Z0-9']+");

        List<String> wordsList = new ArrayList<String>(Arrays.asList(allWords));
        Pattern regex = Pattern.compile("[a-zA-Z0-9']+");
        List<String> cleanWordsList = wordsList.stream()
                .filter(regex.asPredicate())
                .filter(text -> !isFiftyMostCommon(text))
                .filter(text -> !isSecondFiftyMostCommon(text))
                .filter(text -> !isCommonPreposition(text))
                .filter(text -> !isAuxiliaryVerb(text))
                .collect(Collectors.toList());

        this.countOfWords = cleanWordsList.size();
        this.wordCountMap = new HashMap<>();

        Map<String, Long> preliminaryWordMap = cleanWordsList.stream().collect(Collectors.groupingBy(String::toLowerCase, Collectors.counting()));
        for (Map.Entry<String, Long> wordGroup : preliminaryWordMap.entrySet()) {
            this.wordCountMap.put(wordGroup.getKey(), wordGroup.getValue().intValue());
        }

        this.setMostCommonWord();
    }

    private Map.Entry<String, Integer> deriveMostCommonWordStat() {
        Stream<Map.Entry<String, Integer>> wordStream = wordCountMap.entrySet().stream();

        Optional<Map.Entry<String, Integer>> foundWord = wordStream.sorted(Comparator.comparingInt(Map.Entry<String, Integer>::getValue).reversed())
                .findFirst();

        return foundWord.isPresent() ? foundWord.get() : new AbstractMap.SimpleEntry<>("[no words entered]", 0);
    }

    protected List<WordCloud> createCloudList()
    {
        Stream<Map.Entry<String,Integer>> wordStream = wordCountMap.entrySet().stream();

        ArrayList<WordCloud> cloudList = new ArrayList<>();

        wordStream.sorted(Comparator.comparingInt(Map.Entry<String, Integer>::getValue).reversed())
                .limit(12).forEach(word -> cloudList.add(new WordCloud(word.getKey(), word.getValue())));


        return cloudList;
    }


    private void setMostCommonWord() {
        Map.Entry<String, Integer> mostCommonWordStat = this.deriveMostCommonWordStat();
        this.mostCommonWord = mostCommonWordStat.getKey();

        // Ensure CountOfWords is not zero before dividing by it to avoid a DivideByZero exception
        if (this.countOfWords != 0) {
            this.appearanceRate = ((float) mostCommonWordStat.getValue()) / countOfWords;
        } else {
            this.appearanceRate = 0.0f;
        }
    }

    public int distinctWordCount() {
        return wordCountMap.size();
    }

    public int totalWordCount() {
        return countOfWords;
    }

    //output all words in WordCounter times the number of appearances, space delimited
    // E.g. bike, 5 => bike bike bike bike bike
    public String toString() {
        String output = "";
        for (Map.Entry<String,Integer> mapItem : wordCountMap.entrySet())
            for (int i=0; i < mapItem.getValue(); i++)
                output += mapItem.getKey() + " ";
        return output;
    }
}
