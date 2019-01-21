package com.websarva.wings.android.refuge_db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class TestOpenHelper extends SQLiteOpenHelper {

    // データーベース名
    private static final String DATABASE_NAME = "shelter.db";
    // データーベースのバージョン
    private static final int DATABASE_VERSION = 1;

    public TestOpenHelper(Context context){
        super(context , DATABASE_NAME ,null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // テーブル作成
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE shelter(");
        sb.append("shel_no INTEGER PRIMARY KEY,");
        sb.append("shel_name TEXT,");
        sb.append("latitube REAL,");
        sb.append("longitube REAL");
        sb.append(");");
        String sql = sb.toString();
        //SQL実行
        db.execSQL(sql);
        /*
        db.execSQL("INSERT INTO shelter(shel_no,shel_name,latitube,longitube) " +
                "VALUES(000001,'桜丘小学校',38.3031001,140.849178)," +
                "(000002,'中山中学校',38.2964152,140.836555)," +
                "(000003,'中山小学校',38.2917773,140.8477392)," +
                "(000004,'北仙台中学校',38.2932744,140.8617912)," +
                "(000005,'台原小学校',38.2861882,140.8778506)" );

        db.execSQL(sql);
*/

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
