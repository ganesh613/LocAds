package com.example.locationbasedads;

import android.Manifest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.List;


public class MainActivity extends AppCompatActivity{

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    private static int flag =0;
    private static int gpsPermission=0;

    GPSTracker gps;

    // update location time
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 5000;

    double FClat=16.7930064;
    double FClong=80.8231883;
//    double Liblat=16.790896;
//    double Liblong=80.825397;
   double FcDist,LibDist;

    //for notification message
    String strtitle,strtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting location permission initially
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

    }

    //for updating location every 5 seconds
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
//                Toast.makeText(MainActivity.this, "This method is run every 10 seconds",Toast.LENGTH_SHORT).show();
                trackLocation();
            }

            private void trackLocation() {
                gps = new GPSTracker(MainActivity.this);
                // check if GPS enabled
                if(gps.canGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    // \n is for new line
                  //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    //tvMyLocation.setText("latitude is :"+ latitude + "&&" + "longitude is :"+ longitude);

                    matchLocationNotification(latitude,longitude);
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    if(gpsPermission==0) {
                        gpsPermission=1;
                        gps.showSettingsAlert();
                    }
                }
            }
        }, delay);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
    }
    //end of location update
    private double calDistance(double x1, double x2, double y1, double y2){
        Location currentLocation = new Location("locationA");
        currentLocation.setLatitude(x1);
        currentLocation.setLongitude(x2);
        Location destination = new Location("locationB");
        destination.setLatitude(y1);
        destination.setLongitude(y2);
        double distance = currentLocation.distanceTo(destination);
        return distance;
    }
    public Intent fc(){
        strtitle="Food Court";
        strtext="For the love of delicious food...";
        Intent intent=new Intent(getApplicationContext(),MenuList.class);
//        intent.putExtra("1","fc");
        return intent;
    }
    public Intent lib(){
        strtitle="Central Library";
        strtext="Nothing is pleasanter than exploring a library ";
        Intent intent=new Intent(getApplicationContext(),LibList.class);
//        intent.putExtra("2","lib");
        return intent;
    }
    public Intent dh2(){
        strtitle ="Dinning Hall-2";
        strtext="Welcome to dinning hall";
        Intent intent=new Intent(getApplicationContext(),DhList.class);
//        intent.putExtra("3","dh");
        return intent;
    }
    private void matchLocationNotification(double latitude, double longitude) {

        FcDist = calDistance(latitude, longitude, FClat, FClong);
        // LibDist=calDistance(latitude,longitude,Liblat,Liblong);

        //Toast.makeText(this, "Distance is : " + FcDist, Toast.LENGTH_SHORT).show();
        // 123 meters for three places
        if (FcDist <= 500 && flag == 0) {
            flag = 1;
            //createNotification();
            //createNotification();

            CustomNotification(fc());
            //dh2();
            CustomNotification(lib());
            CustomNotification(dh2());

        }
        if (FcDist > 500) {
            flag = 0;
        }
    }

    public void CustomNotification( Intent intent) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_style);
//        strtitle = "Food Court";
//        strtext = "For the love of delicious food...";
        //String fc = "FC";
        //Intent intent = new Intent(getApplicationContext(), MenuList.class);
        intent.putExtra("title", strtitle);
        intent.putExtra("text", strtext);
//        Bundle bundle=new Bundle();

//        if (num==1) bundle.putString("location","AddMenu");
//        if (num==2) bundle.putString("location","AddBooks");
//        if(num==3) bundle.putString("location","AddMess");
//        intent.putExtras(bundle);
//        if(num==1) intent.putExtra("location","AddMenu");
//        if(num==2) intent.putExtra("location","AddBooks");
//        if(num==3) intent.putExtra("location","AddMess");

            //Toast.makeText(this, ""+strtitle+""+strtext, Toast.LENGTH_SHORT).show();
//        intent.putExtra("fc", fc);
//        intent.putExtra("lat",FClat);
//        intent.putExtra("long",FClong);
        PendingIntent pIntent=null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.S){
            pIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_MUTABLE);
        }
        else{
//            pIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
            pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
      //  PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
       // PendingIntent pIntent =PendingIntent . getActivity ( this , 0 , intent ,
         //       PendingIntent . FLAG_UPDATE_CURRENT  | PendingIntent . FLAG_MUTABLE ) ;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,default_notification_channel_id)
                .setSmallIcon(R.drawable.playstore_logo)
                .setVibrate( new long []{ 500 , 1000 })
                .setAutoCancel(true) .setContentIntent(pIntent)
                .setContent(remoteViews);
        remoteViews.setImageViewResource(R.id.icon,R.drawable.playstore_logo);

        remoteViews.setTextViewText(R.id.title,strtitle);
        remoteViews.setTextViewText(R.id.message,strtext);
        // Create Notification Manager
     //   NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //NotificationManager notif = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        // Build Notification with Notification Manager
        NotificationManager mNotificationManager = (NotificationManager)
                getSystemService(Context. NOTIFICATION_SERVICE ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis () ,
                builder.build()) ;
        //notificationmanager.notify(0, builder.build());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}