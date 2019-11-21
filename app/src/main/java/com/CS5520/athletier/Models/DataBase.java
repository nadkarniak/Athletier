package com.CS5520.athletier.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBase extends SQLiteOpenHelper {
    public static String name = "Friends.db";
    public static String tableName = "Friends_table";
    public static String friendName = "Name";
    public static String friendId = "Id";

    public DataBase(@Nullable Context context) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Links_table (NAME TEXT, URL TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Links_table");
        onCreate(db);
    }

    public boolean addData(String Name, String url){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(friendName, Name);
        c.put(friendId, url);
        long result = database.insert(tableName, null, c);

        if (result == -1){
            return false;
        } else{
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from Friends_table",null);
        return cursor;
    }

    public void removeData(){
        SQLiteDatabase d = this.getWritableDatabase();
        d.delete(tableName,null,null);
        d.close();
    }
}
