package net.denryu.android.wordcloud;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

public class WordCounterDB {

    //database constants
    public static final String DB_NAME = "wordcloud.db";
    public static final int DB_VERSION = 12;

    //table constants
    public static final String WORDS_TABLE = "words";
    public static final String INPUTS_TABLE = "inputs";

     //column constants for WORDS_TABLE
    public static final String WORD_ID = "_id";
    public static final int WORD_ID_COL = 0;
    public static final String WORD = "word";
    public static final int WORD_COL = 1;
    public static final String COUNT = "count";
    public static final int COUNT_COL = 2;


    //this column keeps track of how many text imports contained at least 1 instance of this word
    public static final String WORDS_PARENT_INPUT_ID = "input_id";

    //columns for INPUTS_TABLE
    public static final String INPUT_ID = "_id";
    public static final int INPUT_ID_COL = 0;
    public static final String USER = "user";
    public static final int USER_COL = 1;
    public static final String WORDCLOUD_VERSION = "wordcloud_version";
    public static final int WORDCLOUD_VERSION_COL = 2;
    public static final String CREATED_DATE = "created_date_millis";
    public static final int CREATED_DATE_COL = 3;
    public static final String USER_LOCATION = "user_location";
    public static final int USER_LOCATION_COL = 4;
    public static final String TEXT_SOURCE = "text_source";
    public static final int TEXT_SOURCE_COL = 5;

    //db command constants
    private static final String CREATE_WORDS_TABLE =
            "CREATE TABLE " + WORDS_TABLE + " (" +
                    WORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", " + WORD + " TEXT" +
                    ", " + COUNT + " INTEGER NOT NULL" +
                    ", " + WORDS_PARENT_INPUT_ID + " INTEGER NOT NULL" +
                    ", FOREIGN KEY(" + WORDS_PARENT_INPUT_ID + ") REFERENCES " + INPUTS_TABLE + "(" + INPUT_ID + ")" +
                    ");";

    //db command constants
    private static final String CREATE_INPUTS_TABLE =
            "CREATE TABLE " + INPUTS_TABLE + " (" +
                    INPUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", " + USER + " TEXT" +
                    ", " + WORDCLOUD_VERSION + " TEXT" +
                    ", " + CREATED_DATE + " INTEGER NOT NULL" +
                    ", " + USER_LOCATION + " TEXT" +
                    ", " + TEXT_SOURCE + " TEXT" +
                    ");";

    private static final String DROP_WORDS_TABLE =
            "DROP TABLE IF EXISTS " + WORDS_TABLE;
    private static final String DROP_INPUTS_TABLE =
            "DROP TABLE IF EXISTS " + INPUTS_TABLE;

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public WordCounterDB(Context context){
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private  void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    //populate database with new words and counts
    //returns the ID of the input row. textInput must have non-null WordCounter
    public long storeInput(TextInput textInput) {
        long lastRowID = 0;
        long inputId = 0;

        //open database
        this.openWriteableDB();

        //insert parent input into INPUTS_TABLE
        inputId = insertInputRow(textInput.getUserId(), textInput.getWordCloudVersionCode(),
                textInput.getCreateDateMillis(), textInput.getInputTextSource(), textInput.getUserLocation());
        //insert each child words into WORDS_TABLE
        for (Map.Entry<String, Integer> entry : textInput.getWordCounter().getWordCountMap().entrySet()) {
            lastRowID = insertWordRow(entry.getKey(), entry.getValue(), inputId);
        }

        //close database
        this.closeDB();
        //return ID of the input row
        return inputId;
    }

    //insert a new input, return _id identifier (for use in insertWord)
    private long insertInputRow(String userId, int versionCode, long createdDateMillis,
                             String textSource, String userLocation) {

        ContentValues cv = new ContentValues();

        cv.put(CREATED_DATE, createdDateMillis);
        cv.put(WORDCLOUD_VERSION, versionCode);
        cv.put(TEXT_SOURCE, textSource);
        if (userLocation != null) cv.put(USER_LOCATION, userLocation);
        if (userId != null) cv.put(USER, userId);

        long rowID = db.insert(INPUTS_TABLE, null, cv);

        Log.d("WordCounter", "Added to " + INPUTS_TABLE + " table at row " + rowID + " using Wordcloud version " + versionCode);
        return rowID;
    }

    //insert a single word & count row
    private long insertWordRow(String word, int count, long inputId) {
        ContentValues cv = new ContentValues();
        cv.put(WORD, word);
        cv.put(COUNT, count);
        cv.put(WORDS_PARENT_INPUT_ID, inputId);

        long rowID = db.insert(WORDS_TABLE, null, cv);

        Log.d("WordCounter", "Added to " + WORDS_TABLE + " table at row " + rowID + ": " + word + " with count of " + count);
        return rowID;
    }

    //retrive a single TextInput row from INPUTS_TABLE
    public TextInput getTextInput(long inputId) {

        String where = INPUT_ID + "= ?";
        String[] whereArgs = {Long.toString(inputId)};

        this.openReadableDB();
        Cursor cursor = db.query(INPUTS_TABLE, null, where, whereArgs, null, null, null, null);
        cursor.moveToFirst();
        TextInput ti = getTextInputFromCursor(cursor);
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return ti;
    }

    //retrieve all the TextInputs stored in INPUTS_TABLE
    public ArrayList<TextInput> getTextInputs() {

        String orderBy = CREATED_DATE + " DESC";

        this.openReadableDB();
        Cursor cursor = db.query(INPUTS_TABLE, null, null, null, null, null, orderBy, null);
        ArrayList<TextInput> textInputs = new ArrayList<TextInput>();
        while (cursor.moveToNext())
            textInputs.add(getTextInputFromCursor(cursor));

        if (cursor != null)
            cursor.close();
        this.closeDB();

        return textInputs;

    }

    private static TextInput getTextInputFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0)
            return null;
        else {
            try {
                TextInput textInput = new TextInput(
                        cursor.getLong(INPUT_ID_COL),
                        cursor.getString(USER_COL),
                        cursor.getInt(WORDCLOUD_VERSION_COL),
                        cursor.getLong(CREATED_DATE_COL),
                        cursor.getString(USER_LOCATION_COL),
                        cursor.getString(TEXT_SOURCE_COL));
                return textInput;
            } catch (Exception e) {
                return null;
            }
        }
    }

    //drop database tables, but maintain database definition
    public void clearDB() {
        this.openWriteableDB();

        db.execSQL(WordCounterDB.DROP_WORDS_TABLE);
        db.execSQL(WordCounterDB.DROP_INPUTS_TABLE);
        Log.d("WordCounter", "words and inputs tables dropped from db.");
        dbHelper.onCreate(db);
        this.closeDB();
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_INPUTS_TABLE);
            db.execSQL(CREATE_WORDS_TABLE);

            Log.d("WordCounter", "Wordcounter database tables created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.d("WordCounter", "Upgrading db from version " + oldVersion + " to " + newVersion);

            Log.d("WordCounter", "Dropping all tables");
            db.execSQL(WordCounterDB.DROP_WORDS_TABLE);
            db.execSQL(WordCounterDB.DROP_INPUTS_TABLE);

            onCreate(db);
        }
    }
}
