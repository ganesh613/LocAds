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
    private static int flag =0;
    private static int gpsPermission=0;

    GPSTracker gps;

    // update location time
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 5000;

    double FClat=16.7930064;
    double FClong=80.8231883;
    double Liblat=16.790896;
    double Liblong=80.825397;
    double FcDist,LibDist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //loading screen
//
//        Thread welcomeThread = new Thread() {
//
//            @Override
//            public void run() {
//                try {
//                    super.run();
//                    sleep(10000);  //Delay of 10 seconds
//                } catch (Exception e) {
//
//                } finally {
//
//                    Intent i = new Intent(MainActivity.this, MenuList.class);
//                    startActivity(i);
//                    finish();
//                }
//            }
//        };
//        welcomeThread.start();
        //end loading

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
    private void matchLocationNotification(double latitude, double longitude) {
//        Location currentLocation = new Location("locationA");
//        currentLocation.setLatitude(FClat);
//        currentLocation.setLongitude(FClong);
//        Location destination = new Location("locationB");
//        destination.setLatitude(latitude);
//        destination.setLongitude(longitude);
        FcDist= calDistance(latitude,longitude,Liblat,Liblong);
       // LibDist=calDistance(latitude,longitude,Liblat,Liblong);

        //Toast.makeText(this, "Distance is : " + FcDist, Toast.LENGTH_SHORT).show();
        if (FcDist <= 100 && flag==0)  {
        flag=1;
            //createNotification();
            //createNotification();
            CustomNotification();
        }
       if(FcDist> 100 ){
           flag=0;
        }
    }
    //start notification custom
//    public RemoteViews getCustomDesign(String title, String message) {
//        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_style);
//        remoteViews.setTextViewText(R.id.title, title);
//        remoteViews.setTextViewText(R.id.message, message);
//        remoteViews.setImageViewResource(R.id.icon, R.mipmap.logo_lba);
//        return remoteViews;
//    }
    //end notification

    private void createNotification() {
//getCustomDesign("Faaaa","random");
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            NotificationChannel channel = new NotificationChannel("My notification","My notification",NotificationManager.IMPORTANCE_DEFAULT);
//            channel.setDescription("Notification desc");
//            channel.enableVibration(true);
//
//            NotificationManager manager= (NotificationManager) getSystemService(NotificationManager.class);
//            if(manager == null){
//
//            }
//
//            manager.createNotificationChannel(channel);
//        }else
//      correctly executed code
//        NotificationManager notificationManager;
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "My Notification");
//
//            builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
//
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.journaldev.com/"));
//            Intent intent = new Intent(MainActivity.this,MenuList.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//            builder.setContentIntent(pendingIntent);
//            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
//            builder.setContentTitle("Notifications Title");
//            builder.setContentText("Your notification content here.");
//            builder.setSubText("Tap to view the website.");
//
//            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//            // Will display the notification in the notification bar
//            notificationManager.notify(1, builder.build());

// start of working code
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity. this, default_notification_channel_id );
        mBuilder.setSmallIcon(R.drawable.logo_lba);
        Intent intent = new Intent(MainActivity.this,MenuList.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setContentTitle( "Food Court" );
                mBuilder.setStyle( new NotificationCompat.InboxStyle());
                mBuilder.setContentText( "For the love of delicious food..." ) ;
//end of working code

        NotificationManager mNotificationManager = (NotificationManager)
                getSystemService(Context. NOTIFICATION_SERVICE ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_LOW ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis () ,
                mBuilder.build()) ;
    }
    public void CustomNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_style);
        String strtitle = "Food Court";
        String strtext = "For the love of delicious food...";
        //String fc = "FC";
        Intent intent = new Intent(this, MenuList.class);
        intent.putExtra("title", strtitle);
        intent.putExtra("text", strtext);
        //intent.putExtra("fc", fc);
        //intent.putExtra("lat",FClat);
        //intent.putExtra("long",FClong);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,default_notification_channel_id)
                .setSmallIcon(R.drawable.playstore_logo)
                .setVibrate( new long []{ 500 , 1000 })
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