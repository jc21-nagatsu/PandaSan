package com.websarva.wings.android.refuge_db;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;


import java.util.ArrayList;
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
/*
        Intent intent = getIntent();
        ArrayList<String> list = intent.getStringArrayListExtra("latlon");
        nlat = Double.parseDouble(list.get(0));
        nlon = Double.parseDouble(list.get(1));
*/
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        GPSLocationListener locationListener = new GPSLocationListener();
        if (ActivityCompat.checkSelfPermission(ListActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(ListActivity.this, permissions, 1000);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);

        TestOpenHelper helper = new TestOpenHelper(ListActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql;
        try {
            sql = "DELETE FROM shelter";
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.executeUpdateDelete();
            sql = "INSERT INTO shelter(shel_no,shel_name,latitube,longitube) " +
                    "VALUES(000001,'桜丘小学校',38.3031001,140.849178)," +
                    "(000002,'中山中学校',38.2964152,140.836555)," +
                    "(000003,'中山小学校',38.2917773,140.8477392)," +
                    "(000004,'北仙台中学校',38.2932744,140.8617912)," +
                    "(000005,'台原小学校',38.2861882,140.8778506)," +
                    "(000006,'東六番丁小学校',38.2660202,140.8817937)";
            stmt = db.compileStatement(sql);
            stmt.executeInsert();

            sql = "SELECT * FROM shelter";
            Cursor cursor = db.rawQuery(sql, null);
            Cursor count = db.rawQuery("SELECT * FROM shelter", null);
            int i = 0;
            while (count.moveToNext()) {
                i++;
            }
            scenes = new String[i];
            String[][] shelter = new String[i][2];
            String[] stac = new String[i];
            //scenes = new String[5];
            int note = 0;
            //locationListener.onLocationChanged(locationManager.getLastKnownLocation(LOCATION_SERVICE));
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
            for (int l = 0; l < i; l++) {
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //ACCESS_FINE_LOCATIONに対するパーミッションダイアログでかつ許可を選択したなら…
        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //LocationManagerオブジェクトを取得
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            //位置情報が更新された際のリスナオブジェクトを生成
            GPSLocationListener locationListener = new GPSLocationListener();
            //再度ACCESS_FINE_LOCATIONの許可が下りてないかどうかをチェックし、下りてないなら処理を中止
            if (ActivityCompat.checkSelfPermission(ListActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //位置情報の追跡を開始
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
        }
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

    private class GPSLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            nlat = location.getLatitude();
            nlon = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
}
