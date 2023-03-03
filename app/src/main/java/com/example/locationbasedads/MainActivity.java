package com.example.locationbasedads;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity{

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    private static int flag =0,f2=0,f3=0;
    private static int gpsPermission=0;

    GPSTracker gps;

    // update location time
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 2000;

    double FClat=16.7930064;
    double FClong=80.8231883;

    double Loclat=16.5271359;
    double Loclong=81.7326067;
    double LocDist;
    //my home town location
    double Homelat=16.5268816;
    double Homelong = 81.7320998;


    //double FClat=16.793026;
    //double FClong=80.823236;
    double FcDist;
    double HomeDist;


    //for notification message

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
                trackLocation();
            }

            private void trackLocation() {
                gps = new GPSTracker(MainActivity.this);
                // check if GPS enabled
                if(gps.canGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    // \n is for new line

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
//        Toast.makeText(MainActivity.this,""+distance,Toast.LENGTH_SHORT).show();

        return distance;
    }
    private void matchLocationNotification(double latitude, double longitude) {
        FcDist = calDistance(latitude, longitude, FClat, FClong);
        LocDist = calDistance(latitude,longitude,Loclat,Loclong);
        HomeDist = calDistance(latitude,longitude,Homelat,Homelong);
        if (FcDist <= 240 && flag == 0) {
            flag = 1;
            Intent intent2=new Intent(this, MenuList.class);
            setNotificationContent("FC","For the love of delicious food...",intent2);
            CustomNotification("FC Food","For the love of delicious food...",intent2);
        }
        if (FcDist > 240) {
            flag = 0;
        }

        if(HomeDist<=240 && f2==0){
            f2=1;
            Intent intent2=new Intent(this, LibraryMenu.class);
            setNotificationContent("Home","For the love of unknown...",intent2);
            CustomNotification("Home Love","For the love of unknown...",intent2);
//            Toast.makeText(MainActivity.this," f2 value "+f2, Toast.LENGTH_SHORT).show();

        }
        if (HomeDist > 240) {
            f2 = 0;
        }

        if (LocDist <= 240 && f3 == 0) {

            f3 = 1;
            Intent intent2=new Intent(this, MenuList.class);
            setNotificationContent("Pkl","For the love of None...",intent2);
            CustomNotification("Palakol","For the love of None...",intent2);

        }
        if (LocDist > 240) {
            f3 = 0;
        }
    }

    String strtitle1,strtext1;
    Intent intent1;
    public void setNotificationContent(String strtitle,String strtext,Intent intent){
        strtitle1 = strtitle;
        strtext1 = strtext;;
        intent1 = intent;new Intent(this, MenuList.class);
        intent1.putExtra("title", strtitle);
        intent1.putExtra("text", strtext);
    }

    public void CustomNotification(String strtitle,String strtext,Intent intent) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_style);
//        String strtitle = "Food Court";
//        String strtext = "For the love of delicious food...";
//        Intent intent = new Intent(this, MenuList.class);
//        intent.putExtra("title", strtitle);
//        intent.putExtra("text", strtext);

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,default_notification_channel_id)
                .setSmallIcon(R.drawable.playstore_logo)
                .setVibrate( new long []{ 1000 , 1000 , 1000 , 1000 , 1000 })
                .setAutoCancel(true) .setContentIntent(pIntent)
                .setContent(remoteViews);
        remoteViews.setImageViewResource(R.id.icon,R.drawable.playstore_logo);


        remoteViews.setTextViewText(R.id.title,strtitle);
        remoteViews.setTextViewText(R.id.message,strtext);
        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        NotificationManager mNotificationManager = (NotificationManager)
                getSystemService(Context. NOTIFICATION_SERVICE ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_LOW ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis () ,
                builder.build()) ;
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