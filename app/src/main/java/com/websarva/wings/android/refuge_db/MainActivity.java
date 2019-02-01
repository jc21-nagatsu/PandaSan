package com.websarva.wings.android.refuge_db;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    //現在位置の緯度と経度
    private String lat = "0",lon = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);setContentView(R.layout.activity_main);
        insert();
        //LocationManagerオブジェクトを取得
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //位置情報が更新された際のリスナオブジェクトを生成
        GPSLocationListener locationListener = new GPSLocationListener();
        //ACCESS_FINE_LOCATIONの許可が下りてないかどうかをチェックし、下りてないなら許可を求める
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(MainActivity.this, permissions,1000);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //位置情報の追跡を開始
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    //ボタンが押された時の処理
    public void onClick(View view){
        //インテントの作成
        Intent intent1 = new Intent(this, ListActivity.class);
        //ここに遷移するための処理
        intent1.putExtra("lat",lat);
        intent1.putExtra("lon",lon);

        startActivity(intent1);//画面遷移
    }

    private void insert(){
        DB_shelter shelter = new DB_shelter();

        //データベースと接続
        TestOpenHelper helper = new TestOpenHelper(MainActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try{
            //insert要素をセット
            shelter.ins_set();
            //SQLの実行
            String sql = "INSERT OR REPLACE INTO shelter(shel_no,shel_name,latitube,longitube) VALUES" +shelter.getshelter();
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.executeInsert();
        }finally {
            db.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        //ACCESS_FINE_LOCATIONに対するパーミッションダイアログでかつ許可を選択したなら…
        if(requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //LocationManagerオブジェクトを取得
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            //位置情報が更新された際のリスナオブジェクトを生成
            GPSLocationListener locationListener = new GPSLocationListener();
            //再度ACCESS_FINE_LOCATIONの許可が下りてないかどうかをチェックし、下りてないなら処理を中止
            if(ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return;
            }
            //位置情報の追跡を開始
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }

    private class GPSLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            //位置情報を格納
            lat = String.valueOf(location.getLatitude());
            lon = String.valueOf(location.getLongitude());
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
