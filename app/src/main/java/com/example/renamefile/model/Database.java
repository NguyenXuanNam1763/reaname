package com.example.renamefile.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class Database  extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    public void jquery(String sql){
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL(sql);
    }


    public Cursor getData(String sql){
        SQLiteDatabase database=getReadableDatabase();
        return  database.rawQuery(sql,null);
    }

    public void insertInto(Images images){
        String sql="insert into Images values(null,'"+images.getNewUri()+"','"+images.getNewName()+"','"+images.getOriginalUri()+"','"+images.getOriginalName()+"')";
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
