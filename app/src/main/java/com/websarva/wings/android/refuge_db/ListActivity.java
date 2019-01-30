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

    //現在地
    double nlat = 0, nlon = 0;
    //東北電子
    //double nlat = 38.2645516, nlon = 140.8795554;
    
    private static String[] scenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        ListView listView = findViewById(R.id.list_view);

        Intent intent = getIntent();
        nlat = Double.parseDouble(intent.getStringExtra("lat"));
        nlon = Double.parseDouble(intent.getStringExtra("lon"));

        TestOpenHelper helper = new TestOpenHelper(ListActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM shelter",null);
            int count = cursor.getCount();
            scenes = new String[5];
            String[][] shelter = new String[count][2];
            String[] distance = new String[count];
            int i = 0;
            while (cursor.moveToNext()) {
                int idx = cursor.getColumnIndex("shel_name");
                double lat = cursor.getDouble(cursor.getColumnIndex("latitube"));
                double lon = cursor.getDouble(cursor.getColumnIndex("longitube"));

                double stac = keisan(lat, lon, nlat, nlon);

                shelter[i][0] = cursor.getString(idx);
                shelter[i][1] = String.valueOf(stac);
                distance[i] = String.valueOf(stac);

                i++;
            }

            Arrays.sort(distance);

            for (int l = 0; l < 5; l++) {
                for (int k = 0; k < count; k++) {
                    if (shelter[k][1].equals(distance[l])) {
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

    private double keisan(double $lat1, double $lon1, double $lat2, double $lon2) {
        return Math.sqrt(Math.pow($lat1 - $lat2, 2) + Math.pow($lon1 - $lon2, 2));
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
