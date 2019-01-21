package com.websarva.wings.android.refuge_db;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.Locale;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    int i = 0;
    private static String[] scenes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listView = findViewById(R.id.list_view);

        TestOpenHelper helper = new TestOpenHelper(ListActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql;
        try{
            sql="DELETE FROM shelter";
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.executeUpdateDelete();
            sql="INSERT INTO shelter(shel_no,shel_name,latitube,longitube) " +
                    "VALUES(000001,'桜丘小学校',38.3031001,140.849178)," +
                    "(000002,'中山中学校',38.2964152,140.836555)," +
                    "(000003,'中山小学校',38.2917773,140.8477392)," +
                    "(000004,'北仙台中学校',38.2932744,140.8617912)," +
                    "(000005,'台原小学校',38.2861882,140.8778506)" ;
            stmt = db.compileStatement(sql);
            stmt.executeInsert();

            sql = "SELECT * FROM shelter";
            Cursor cursor = db.rawQuery(sql,null);
            Cursor count = db.rawQuery("SELECT count(*) FROM shelter",null);
            //scenes = new String[count.getInt(0)];
            scenes = new String[5];
            int note = 0;
            while(cursor.moveToNext()){
                int idxNote = cursor.getColumnIndex("shel_name");
                scenes[note] = cursor.getString(idxNote);
                note++;
            }

        }
        finally {
            db.close();
        }

        BaseAdapter adapter = new ListViewAdapter(this.getApplicationContext(),
                R.layout.list, scenes);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedText = scenes[position];
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");

        String str = String.format(Locale.US,
                "http://maps.google.com/maps?daddr=%s",
                selectedText);
        intent.setData(Uri.parse(str));
        startActivity(intent);

    }
}
