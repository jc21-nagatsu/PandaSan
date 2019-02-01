package com.websarva.wings.android.refuge_db;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.Locale;


public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //現在位置の緯度と経度
    private double lat = 0, lon = 0;
    //東北電子
    //private double lat = 38.2645516, lon = 140.8795554;

    //リストに表示する
    private static String[] scenes = new String[5];
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        ListView listView = findViewById(R.id.list_view);

        //MainActivityから現在位置の緯度と経度を受け取る
        Intent intent = getIntent();
        lat = Double.parseDouble(intent.getStringExtra("lat"));
        lon = Double.parseDouble(intent.getStringExtra("lon"));

        //データベースに接続
        TestOpenHelper helper = new TestOpenHelper(ListActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            //SQL実行
            Cursor cursor = db.rawQuery("SELECT * FROM shelter",null);
            //取り出した行数
            int count = cursor.getCount();
            //受け取り用の変数
            String[][] shelter = new String[count][2];
            String[] distanceList = new String[count];

            int i = 0;

            while (cursor.moveToNext()) {
                int idx = cursor.getColumnIndex("shel_name");
                double slat = cursor.getDouble(cursor.getColumnIndex("latitube"));
                double slon = cursor.getDouble(cursor.getColumnIndex("longitube"));

                //現在位置の緯度経度と避難所の緯度経度から、現在位置から避難所までの直線距離を計算
                double distance = Math.sqrt(Math.pow(slat - lat, 2) + Math.pow(slon - lon, 2));

                shelter[i][0] = cursor.getString(idx);
                shelter[i][1] = String.valueOf(distance);
                distanceList[i] = String.valueOf(distance);

                i++;
            }

            //距離の近い順に並び変え
            Arrays.sort(distanceList);

            //並び変えたdistanceListを用いてshelterから5つ取り出す
            for (int l = 0; l < 5; l++) {
                for (int k = 0; k < count; k++) {
                    if (shelter[k][1].equals(distanceList[l])) {
                        scenes[l] = shelter[k][0];
                        break;
                    }
                }
            }
        } finally {
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
