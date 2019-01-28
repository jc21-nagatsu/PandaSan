package com.websarva.wings.android.refuge_db;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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

    int i = 0;

    //現在地
    //private double nlat = 38.2645516, nlon = 140.8795554;//東北電子
    double nlat = 0, nlon = 0;

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
        String sql;
        try {
            sql = "DELETE FROM shelter";
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.executeUpdateDelete();
            sql = "INSERT INTO shelter(shel_no,shel_name,latitube,longitube) " +
                    "VALUES(000001,'桜丘小学校',38.3031001,140.849178)," +
                    "(000002,'中山中学校',38.2964152,140.836555),"+
                    "(000003,'中山小学校',38.2917773,140.8477392),"+
                    "(000004,'北仙台中学校',38.2932744,140.8617912),"+
                    "(000005,'台原小学校',38.2861882,140.8778506),"+
                    "(000006,'旭丘小学校',38.2970882,140.88353),"+
                    "(000007,'荒巻小学校',38.2871782,140.8556185),"+
                    "(000008,'国見小学校',38.2764517,140.8429165),"+
                    "(000009,'三条中学校',38.2803142,140.851432),"+
                    "(000010,'通町小学校',38.2765622,140.860915),"+
                    "(000011,'小松島小学校',38.2809102,140.886756),"+
                    "(000012,'八幡小学校',38.2712322,140.850533),"+
                    "(000013,'木町通小学校',38.2710116,140.8623098),"+
                    "(000014,'上杉山中学校',38.2791072,140.871638),"+
                    "(000015,'五城中学校',38.2793492,140.879824),"+
                    "(000016,'片平丁小学校',38.2560816,140.8668317),"+
                    "(000017,'折立小学校',38.2602072,140.8063337),"+
                    "(000018,'川平小学校',38.3056142,140.8352617),"+
                    "(000019,'東六番丁小学校',38.2660202,140.8817937),"+
                    "(000020,'北六番丁小学校',38.2763224,140.8799932),"+
                    "(000021,'広瀬小学校',38.2724057,140.7738057),"+
                    "(000022,'広瀬中学校',38.2739181,140.7605696),"+
                    "(000023,'上愛子小学校',38.285081,140.6912233),"+
                    "(000024,'作並小学校',38.3167762,140.6336197),"+
                    "(000025,'作並小学校新川分校',38.3039172,140.6307891),"+
                    "(000026,'大倉小学校',38.3343032,140.696556),"+
                    "(000027,'川前小学校',38.2902162,140.7448643),"+
                    "(000028,'大沢小学校',38.2997542,140.7710543),"+
                    "(000029,'大沢中学校',38.2913619,140.748685),"+
                    "(000030,'吉成小学校',38.2906542,140.8230813),"+
                    "(000031,'吉成中学校',38.2906626,140.8230813),"+
                    "(000032,'南吉成小学校',38.2911991,140.8116091),"+
                    "(000033,'第一中学校',38.2727962,140.8437963),"+
                    "(000034,'第二中学校',38.2728042,140.8605217),"+
                    "(000035,'栗生小学校',38.2674332,140.7840333),"+
                    "(000036,'上杉山通小学校',38.2713662,140.8714003),"+
                    "(000037,'北仙台小学校',38.2975156,140.858991),"+
                    "(000038,'桜丘中学校',38.3040202,140.8497883),"+
                    "(000039,'立町小学校',38.2620864,140.8612878),"+
                    "(000040,'五橋中学校',38.2520542,140.8770033),"+
                    "(000041,'台原中学校',38.2869782,140.8784173),"+
                    "(000042,'南吉成中学校',38.2919759,140.8101064),"+
                    "(000043,'折立中学校',38.2657042,140.8042453),"+
                    "(000044,'仙台高等学校',38.2823882,140.8316033),"+
                    "(000045,'仙台青陵中等教育学校',38.2886365,140.8316569),"+
                    "(000046,'東二番丁小学校',38.2594372,140.8725283),"+
                    "(000047,'広陵中学校',38.2999292,140.6796393),"+
                    "(000048,'愛子小学校',38.2768585,140.6911642),"+
                    "(000049,'錦ケ丘小学校',38.2565155,140.7587469)";
            stmt = db.compileStatement(sql);
            stmt.executeInsert();

            sql = "SELECT * FROM shelter";
            Cursor cursor = db.rawQuery(sql, null);
            Cursor count = db.rawQuery("SELECT * FROM shelter", null);
            int i = 0;
            while (count.moveToNext()) {
                i++;
            }
            scenes = new String[5];
            String[][] shelter = new String[i][2];
            String[] stac = new String[i];
            int note = 0;
            while (cursor.moveToNext()) {
                int idxNote = cursor.getColumnIndex("shel_name");
                double lat = cursor.getDouble(cursor.getColumnIndex("latitube"));
                double lon = cursor.getDouble(cursor.getColumnIndex("longitube"));


                double s = keisan(lat, lon, nlat, nlon);

                shelter[note][0] = cursor.getString(idxNote);
                shelter[note][1] = String.valueOf(s);
                stac[note] = String.valueOf(s);

                note++;
            }
            Arrays.sort(stac);
            for (int l = 0; l < 5; l++) {
                for (int l2 = 0; l2 < i; l2++) {
                    if (shelter[l2][1].equals(stac[l])) {
                        scenes[l] = shelter[l2][0];
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
