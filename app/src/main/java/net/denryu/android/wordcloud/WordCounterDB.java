package net.denryu.android.wordcloud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Map;

public class WordCounterDB {

    //database constants
    public static final String DB_NAME = "wordcloud.db";
    public static final int DB_VERSION = 1;

    //table constant
    public static final String WORDCOUNTER_TABLE = "wordcounter";

    //column constants
    public static final String WORD_ID = "_id";
    public static final int WORD_ID_COL = 0;

    public static final String WORD = "word";
    public static final int WORD_COL = 1;

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

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public WordCounterDB(Context context){

//        Log.d("Word Counter", "Inside WordsDB constructor");
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
    public long insertWords(Map<String, Integer> wordCountMap) {
        long lastRowID = 0;

        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            lastRowID = insertWord(entry.getKey(), entry.getValue());
        }
        return lastRowID;
    }

    //insert a single word & count row
    public long insertWord(String word, int count) {
        ContentValues cv = new ContentValues();
        cv.put(WORD, word);
        cv.put(TOTAL_COUNT, count);
        cv.put(MODIFIED_DATE, System.currentTimeMillis());

        this.openWriteableDB();
        long rowID = db.insert(WORDCOUNTER_TABLE, null, cv);
        Log.d("WordCounter", "Added to database: word " + word + " with count of " + count);
        this.closeDB();
        return rowID;
    }

//    //Add a public getTips method that returns an ArrayList<Tip> object that contains all columns and rows from the database table.
//    public ArrayList<Tip> getTips() {
//
//        this.openReadableDB();
//        Cursor cursor =  db.query(WORD_TABLE, null, null, null, null, null, null);
//        ArrayList<Tip> tips = new ArrayList<Tip>();
//        while (cursor.moveToNext()) {
//            tips.add(getTipFromCursor(cursor));
//        }
//        //close db connections
//        if (cursor != null)
//            cursor.close();
//        this.closeDB();
//
//        return tips;
//    }
//
//    private static Tip getTipFromCursor(Cursor cursor) {
//        if (cursor == null || cursor.getCount() <= 0)
//            return null;
//
//        try {
//            Tip tip = new Tip(
//                    cursor.getInt(WORD_ID_COL)
//                    ,cursor.getInt(BILL_DATE_COL)
//                    ,cursor.getLong(BILL_AMOUNT_COL)
//                    ,cursor.getLong(WORD_PERCENT_COL));
//            return tip;
//        } catch (Exception e) {
//            return null;
//        }
//    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_WORDCOUNTER_TABLE);

//            db.execSQL("INSERT INTO " + WORDCOUNTER_TABLE + " VALUES (1, 0, 100.00, 0.2)");
//            db.execSQL("INSERT INTO " + WORDCOUNTER_TABLE + " VALUES (2, 1, 10.98, 0.15)");

            Log.d("WordCounter", "Wordcounter database table created");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.d("WordCounter", "Upgrading db from version " + oldVersion + " to " + newVersion);

            db.execSQL(WordCounterDB.DROP_WORDCOUNTER_TABLE);
            onCreate(db);
        }
    }

}