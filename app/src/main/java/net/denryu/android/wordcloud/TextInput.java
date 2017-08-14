package net.denryu.android.wordcloud;

import java.util.Map;

/**
 * Created by archie on 8/14/2017.
 */

public class TextInput {

    private WordCounter wordCounter;
    private String advertisingId, inputTextSource, userLocation;

    public TextInput(WordCounter wordCounter,
                     String advertisingId,
                     String inputTextSource,
                     String userLocation) {

        this.wordCounter = wordCounter;
        this.advertisingId = advertisingId;
        this.inputTextSource = inputTextSource;
        this.userLocation = userLocation;

    }

    public WordCounter getWordCounter() {  return wordCounter;  }
    public String getAdvertisingId () { return advertisingId; }
    public String getInputTextSource() { return inputTextSource; }
    public String getUserLocation() { return userLocation; }

}
