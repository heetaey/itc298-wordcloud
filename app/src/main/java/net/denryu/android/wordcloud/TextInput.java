package net.denryu.android.wordcloud;

import java.util.Date;

/**
 * Created by archie on 8/14/2017.
 */

public class TextInput {

    private WordCounter wordCounter;
    private String userId, inputTextSource, userLocation;
    int wordCloudVersionCode;
    long inputId;
    long createDateMillis;

    public TextInput(String userId,
                     String inputTextSource,
                     String userLocation,
                     WordCounter wc) {

        this.userId = userId;
        this.inputTextSource = inputTextSource == null ? "" : inputTextSource;
        this.userLocation = userLocation;
        this.wordCounter = wc;
    }

    public TextInput(String userId,
                     String inputTextSource,
                     String userLocation) {

        this.userId = userId;
        this.inputTextSource = inputTextSource == null ? "" : inputTextSource;
        this.userLocation = userLocation;
    }

    public TextInput(long inputId, String userId, int wordCloudVersionCode, long createDateMillis,
                     String userLocation, String inputTextSource) {

        this.inputId = inputId;
        this.userId = userId;
        this.wordCloudVersionCode = wordCloudVersionCode;
        this.createDateMillis = createDateMillis;
        this.inputTextSource = inputTextSource == null ? "" : inputTextSource;
        this.userLocation = userLocation;
    }

    public TextInput(long inputId, String userId, int wordCloudVersionCode, long createDateMillis,
                     String userLocation, String inputTextSource, WordCounter wc) {

        this.inputId = inputId;
        this.userId = userId;
        this.wordCloudVersionCode = wordCloudVersionCode;
        this.createDateMillis = createDateMillis;
        this.inputTextSource = inputTextSource == null ? "" : inputTextSource;
        this.userLocation = userLocation;
        this.wordCounter = wc;
    }

    public TextInput(String userId, int wordCloudVersionCode, long createDateMillis,
                     String userLocation, String inputTextSource) {

        this.userId = userId;
        this.wordCloudVersionCode = wordCloudVersionCode;
        this.createDateMillis = createDateMillis;
        this.inputTextSource = inputTextSource == null ? "" : inputTextSource;
        this.userLocation = userLocation;
    }

    public TextInput(String userId, int wordCloudVersionCode, long createDateMillis,
                     String userLocation, String inputTextSource, WordCounter wc) {

        this.userId = userId;
        this.wordCloudVersionCode = wordCloudVersionCode;
        this.createDateMillis = createDateMillis;
        this.inputTextSource = inputTextSource == null ? "" : inputTextSource;
        this.userLocation = userLocation;
        this.wordCounter = wc;
    }

    public WordCounter getWordCounter() {  return wordCounter;  }
    public String getUserId () { return userId; }
    public String getInputTextSource() { return inputTextSource; }
    public String getUserLocation() { return userLocation; }
    public int getWordCloudVersionCode() { return wordCloudVersionCode; }
    public long getCreateDateMillis() { return createDateMillis; }

    @Override
    public String toString() {
        Date date = new Date(createDateMillis);

        return date + " " + inputTextSource;
    }

}
