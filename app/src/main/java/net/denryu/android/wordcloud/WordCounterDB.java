package net.denryu.android.wordcloud;
<<<<<<< HEAD
import net.denryu.android.wordcloud.BuildConfig;


import android.content.ContentValues;
import android.content.Context;
=======

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
>>>>>>> f34a0616f19a8ad05356d547762c0260ef14d67a
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Map;

public class WordCounterDB {

    //database constants
    public static final String DB_NAME = "wordcloud.db";
<<<<<<< HEAD
    public static final int DB_VERSION = 7;

    //table constants
    public static final String WORDS_TABLE = "words";
    public static final String CLOUDS_TABLE = "clouds";

    //column constants
//    public static final String WORD_ID = "_id";
//    public static final int WORD_ID_COL = 0;

=======
    public static final int DB_VERSION = 3;

    //table constant
    public static final String WORDCOUNTER_TABLE = "wordcounter";

    //column constants
>>>>>>> f34a0616f19a8ad05356d547762c0260ef14d67a
    public static final String WORD_ID = "_id";
    public static final int WORD_ID_COL = 0;

    public static final String WORD = "word";
    public static final int WORD_COL = 1;

<<<<<<< HEAD
    public static final String COUNT = "count";
    public static final int COUNT_COL = 2;

    //this column keeps track of how many text imports contained at least 1 instance of this word
    public static final String WORDS_CLOUD_ID = "cloud_id";
    public static final int WORDS_CLOUD_ID_COL = 3;

    public static final String CLOUD_ID = "_id";
    public static final int CLOUD_ID_COL = 0;

    public static final String USER = "user";
    public static final String WORDCLOUD_VERSION = "wordcloud_version";
    public static final String CREATED_DATE = "created_date_millis";
    public static final String USER_LOCATION = "user_location";
    public static final String TEXT_SOURCE = "text_source";

    //db command constants
    public static final String CREATE_WORDS_TABLE =
            "CREATE TABLE " + WORDS_TABLE + " (" +
                    WORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", " + WORD + " TEXT" +
                    ", " + COUNT + " INTEGER NOT NULL" +
                    ", " + WORDS_CLOUD_ID + " INTEGER NOT NULL" +
                    ", FOREIGN KEY(" + WORDS_CLOUD_ID + ") REFERENCES " + CLOUDS_TABLE + "(" + CLOUD_ID + ")" +
//                    IMPORT_COUNT + ", INTEGER NOT NULL" +
                    ");";

    //db command constants
    public static final String CREATE_CLOUDS_TABLE =
            "CREATE TABLE " + CLOUDS_TABLE + " (" +
                    CLOUD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", " + USER + " TEXT" +
                    ", " + WORDCLOUD_VERSION + " TEXT" +
                    ", " + CREATED_DATE + " INTEGER NOT NULL" +
                    ", " + USER_LOCATION + " TEXT" +
                    ", " + TEXT_SOURCE + " TEXT" +
                    ");";

    public static final String DROP_WORDS_TABLE =
            "DROP TABLE IF EXISTS " + WORDS_TABLE;
    public static final String DROP_CLOUDS_TABLE =
            "DROP TABLE IF EXISTS " + CLOUDS_TABLE;
=======
    public static final String TOTAL_COUNT = "total_count";
    public static final int TOTAL_COUNT_COL = 2;

    //this column keeps track of how many text imports contained at least 1 instance of this word
    public static final String IMPORT_COUNT = "import_count";
    public static final int IMPORT_COUNT_COL = 3;

    public static final String MODIFIED_DATE = "modified_date_millis";
    public static final int MODIFIED_DATE_COL = 4;

    //db command constants
    public static final String CREATE_WORDCOUNTER_TABLE =
            "CREATE TABLE " + WORDCOUNTER_TABLE + " (" +
                    WORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", " + WORD + " TEXT NOT NULL" +
                    ", " + TOTAL_COUNT + " INTEGER NOT NULL" +
                    ", " + MODIFIED_DATE + " INTEGER NOT NULL" +
//                    IMPORT_COUNT + ", INTEGER NOT NULL" +
                    ");";

    public static final String DROP_WORDCOUNTER_TABLE =
            "DROP TABLE IF EXISTS " + WORDCOUNTER_TABLE;
>>>>>>> f34a0616f19a8ad05356d547762c0260ef14d67a

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
    //returns the ID of the last row added
<<<<<<< HEAD
    public long insertWords(Map<String, Integer> wordCountMap,
                            String advertisingId,
                            String textSource,
                            String userLocation) {
        long lastRowID = 0;
        long cloudId = 0;

        cloudId = insertCloud(advertisingId, textSource, userLocation);
        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            lastRowID = insertWord(entry.getKey(), entry.getValue(), cloudId);
=======
    public long insertWords(Map<String, Integer> wordCountMap) {
        long lastRowID = 0;

        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            lastRowID = insertWord(entry.getKey(), entry.getValue());
>>>>>>> f34a0616f19a8ad05356d547762c0260ef14d67a
        }
        return lastRowID;
    }

<<<<<<< HEAD
    //insert a new cloud, return _id identifier (for use in insertWord)
    public long insertCloud(String advertisingId, String textSource, String userLocation) {
        int versionCode = BuildConfig.VERSION_CODE;

        ContentValues cv = new ContentValues();

        cv.put(CREATED_DATE, System.currentTimeMillis());
        if (versionCode > 0) cv.put(WORDCLOUD_VERSION, versionCode);
        if (userLocation != null) cv.put(USER_LOCATION, userLocation);
        if (advertisingId != null) cv.put(USER, advertisingId);

        this.openWriteableDB();

        long rowID = db.insert(CLOUDS_TABLE, null, cv);

//        db.update(WORDS_TABLE, cv, String.format("%s = ?", WORD), new String[]{word});
//        db.replace()
        Log.d("WordCounter", "Added cloud at row " + rowID + " using Wordcloud version " + versionCode);
        this.closeDB();
        return rowID;
    }

    //insert a single word & count row
    public long insertWord(String word, int count, long cloudId) {
        ContentValues cv = new ContentValues();
        cv.put(WORD, word);
        cv.put(COUNT, count);
//        cv.put(MODIFIED_DATE, System.currentTimeMillis());
        cv.put(WORDS_CLOUD_ID, cloudId);
        this.openWriteableDB();

        long rowID = db.insert(WORDS_TABLE, null, cv);

//        db.update(WORDS_TABLE, cv, String.format("%s = ?", WORD), new String[]{word});
//        db.replace()
        Log.d("WordCounter", "Added to words table at row " + rowID + ": word " + word + " with count of " + count);
=======
    //insert a single word & count row
    public long insertWord(String word, int count) {
        ContentValues cv = new ContentValues();
        cv.put(WORD, word);
        cv.put(TOTAL_COUNT, count);
        cv.put(MODIFIED_DATE, System.currentTimeMillis());

        this.openWriteableDB();
        long rowID = db.insert(WORDCOUNTER_TABLE, null, cv);
        Log.d("WordCounter", "Added to database at row " + rowID + ": word " + word + " with count of " + count);
>>>>>>> f34a0616f19a8ad05356d547762c0260ef14d67a
        this.closeDB();
        return rowID;
    }

    //drop database tables, but maintain database definition
    public void clearDB() {
        this.openWriteableDB();
<<<<<<< HEAD
        db.execSQL(WordCounterDB.DROP_WORDS_TABLE);
        db.execSQL(WordCounterDB.DROP_CLOUDS_TABLE);
        Log.d("WordCounter", "words and clouds tables dropped from db.");
=======
        db.execSQL(WordCounterDB.DROP_WORDCOUNTER_TABLE);
        Log.d("WordCounter", "Wordcounter Table dropped from db.");
>>>>>>> f34a0616f19a8ad05356d547762c0260ef14d67a
        dbHelper.onCreate(db);
        this.closeDB();
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

<<<<<<< HEAD
            db.execSQL(CREATE_CLOUDS_TABLE);
            db.execSQL(CREATE_WORDS_TABLE);

<<<<<<< HEAD
//            db.execSQL("INSERT INTO " + WORDS_TABLE + " VALUES (1, 0, 100.00, 0.2)");
//            db.execSQL("INSERT INTO " + WORDS_TABLE + " VALUES (2, 1, 10.98, 0.15)");

=======
>>>>>>> f34a0616f19a8ad05356d547762c0260ef14d67a
=======
            db.execSQL(CREATE_WORDCOUNTER_TABLE);

>>>>>>> f34a0616f19a8ad05356d547762c0260ef14d67a
            Log.d("WordCounter", "Wordcounter database table created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.d("WordCounter", "Upgrading db from version " + oldVersion + " to " + newVersion);

<<<<<<< HEAD
<<<<<<< HEAD
            db.execSQL(WordCounterDB.DROP_WORDS_TABLE);
            db.execSQL(WordCounterDB.DROP_CLOUDS_TABLE);

=======
            db.execSQL(WordCounterDB.DROP_WORDCOUNTER_TABLE);
>>>>>>> f34a0616f19a8ad05356d547762c0260ef14d67a
=======
            db.execSQL(WordCounterDB.DROP_WORDCOUNTER_TABLE);
>>>>>>> f34a0616f19a8ad05356d547762c0260ef14d67a
            onCreate(db);
        }
    }

}
