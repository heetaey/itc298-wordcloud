package net.denryu.android.wordcloud;

import android.icu.text.SimpleDateFormat;

import java.util.Date;

public class TextInput {

    private WordCounter wordCounter;
    private String userId, inputTextSource, userLocation;
    int wordCloudVersionCode;
    long inputId = -1;
    long createDateMillis;

    public TextInput(String userId,
                     String inputTextSource,
                     String userLocation,
                     WordCounter wc) {

        this.userId = userId;
        this.inputTextSource = (inputTextSource == null) ? "" : inputTextSource;
        this.userLocation = userLocation;
        this.wordCounter = wc;
    }

    public TextInput(String userId,
                     String inputTextSource,
                     String userLocation) {

        this.userId = userId;
        this.inputTextSource = (inputTextSource == null) ? "" : inputTextSource;
        this.userLocation = userLocation;
    }

    public TextInput(long inputId, String userId, int wordCloudVersionCode, long createDateMillis,
                     String userLocation, String inputTextSource) {

        this.inputId = inputId;
        this.userId = userId;
        this.wordCloudVersionCode = wordCloudVersionCode;
        this.createDateMillis = createDateMillis;
        this.inputTextSource = (inputTextSource == null) ? "" : inputTextSource;
        this.userLocation = userLocation;
    }

    public TextInput(long inputId, String userId, int wordCloudVersionCode, long createDateMillis,
                     String userLocation, String inputTextSource, WordCounter wc) {

        this.inputId = inputId;
        this.userId = userId;
        this.wordCloudVersionCode = wordCloudVersionCode;
        this.createDateMillis = createDateMillis;
        this.inputTextSource = (inputTextSource == null) ? "" : inputTextSource;
        this.userLocation = userLocation;
        this.wordCounter = wc;
    }

    public TextInput(String userId, int wordCloudVersionCode, long createDateMillis,
                     String userLocation, String inputTextSource) {

        this.userId = userId;
        this.wordCloudVersionCode = wordCloudVersionCode;
        this.createDateMillis = createDateMillis;
        this.inputTextSource = (inputTextSource == null) ? "" : inputTextSource;
        this.userLocation = userLocation;
    }

    public TextInput(String userId, int wordCloudVersionCode, long createDateMillis,
                     String userLocation, String inputTextSource, WordCounter wc) {

        this.userId = userId;
        this.wordCloudVersionCode = wordCloudVersionCode;
        this.createDateMillis = createDateMillis;
        this.inputTextSource = (inputTextSource == null) ? "" : inputTextSource;
        this.userLocation = userLocation;
        this.wordCounter = wc;
    }

    public WordCounter getWordCounter() { return wordCounter; }

    public void setWordCounter(WordCounter wc) {
        wordCounter = wc;
    }

    public String getUserId () { return userId; }
    public String getInputTextSource() { return inputTextSource; }
    public String getUserLocation() { return userLocation; }
    public int getWordCloudVersionCode() { return wordCloudVersionCode; }
    public long getCreateDateMillis() { return createDateMillis; }

    @Override
    public String toString() {
        String stringout;

        Date date = new Date(createDateMillis);

        SimpleDateFormat calendarDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat weekdayTime = new SimpleDateFormat("EEEE hh:mm a");
        SimpleDateFormat time = new SimpleDateFormat("h:mm a");

        Date today = new Date();
        long diff = today.getTime() - createDateMillis;
        if (diff > 518400000)
            stringout = calendarDate.format(date);
        else if (diff > 86400000)
            stringout = weekdayTime.format(date);
        else
            stringout = time.format(date);

        return stringout + " " + this.inputTextSource.trim();
    }

}
