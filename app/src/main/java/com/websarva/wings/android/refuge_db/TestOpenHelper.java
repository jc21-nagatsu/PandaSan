package com.websarva.wings.android.refuge_db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TestOpenHelper extends SQLiteOpenHelper {

    // データーベース名
    private static final String DATABASE_NAME = "shelter.db";
    // データーベースのバージョン
    private static final int DATABASE_VERSION = 1;

    public TestOpenHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
