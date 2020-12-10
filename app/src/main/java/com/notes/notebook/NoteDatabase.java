package com.notes.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NoteDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "nodedb5";
    private static final String DATABASE_TABLE = "notestable5";

    //column names for database
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_COLOR = "color";
    private static final String KEY_FAVOURITE = "favourite";



    NoteDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create table TableName(ID INT PRIMARY_KEY,title TEXT,content TEXT,date TEXT,time TEXT);
        String query = "CREATE TABLE " +  DATABASE_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE+" TEXT,"
                +KEY_CONTENT+ " TEXT,"
                +KEY_DATE+" TEXT,"
                +KEY_TIME+ " TEXT,"
                +KEY_COLOR+ " INTEGER,"
                +KEY_FAVOURITE+ " INTEGER" +")";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion >= newVersion)
            return;
        db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE);
        onCreate(db);
    }

    public long addNote(NodeModel note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(KEY_TITLE,note.getHead());
        c.put(KEY_CONTENT,note.getDesc());
        c.put(KEY_DATE,note.getDate());
        c.put(KEY_TIME,note.getTime());
        c.put(KEY_COLOR,note.getColor());
        c.put(KEY_FAVOURITE,note.getFav());

        long ID  = db.insert(DATABASE_TABLE,null,c);
        Log.d("ID", "id: "+ID);
        return ID;

    }

    public NodeModel getNote(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID,KEY_TITLE,KEY_CONTENT,KEY_DATE,KEY_TIME,KEY_COLOR,KEY_FAVOURITE},KEY_ID+"=?",
                new String[]{String.valueOf(id)},null,null,null);

        if(cursor != null)
            cursor.moveToFirst();
         NodeModel note = new NodeModel(cursor.getLong(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5),cursor.getInt(6));

         return note;
    }

    public List<NodeModel> getNotes(String q){


        SQLiteDatabase db = this.getReadableDatabase();
        List<NodeModel> allnotes = new ArrayList<>();
        String query_tail = q;  //KEY_ID + " DESC";
        String query = "SELECT * FROM "+ DATABASE_TABLE+ " ORDER BY " + query_tail;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do {
                NodeModel note = new NodeModel();
                note.setID(cursor.getLong(0));
                Log.d("CheckId","ID: "+ cursor.getString(0));

                note.setHead(cursor.getString(1));
                note.setDesc(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));
                note.setColor(cursor.getInt(5));
                note.setFav(cursor.getInt(6));


                allnotes.add(note);

            }while (cursor.moveToNext());
        }

        return allnotes;

    }

    void deleteNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE,KEY_ID+"=?",new String[]{String.valueOf(id)});
        db.close();
    }

    public int editNote(NodeModel note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        Log.d("Edited", "Edited Title: -> "+ note.getHead() + "\n ID -> "+note.getDesc());
        c.put(KEY_TITLE,note.getHead());
        c.put(KEY_CONTENT,note.getDesc());
        c.put(KEY_DATE,note.getDate());
        c.put(KEY_TIME,note.getTime());
        c.put(KEY_COLOR,note.getColor());
        c.put(KEY_FAVOURITE,note.getFav());
        return db.update(DATABASE_TABLE,c,KEY_ID+"=?",new String[]{String.valueOf(note.getID())});
    }

}
