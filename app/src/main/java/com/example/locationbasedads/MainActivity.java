package com.example.locationbasedads;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    private static int gpsPermission = 0;
    GPSTracker gps;
    // update location time
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000;//2000 milli seconds

    private static int f1 = 0, f2 = 0, f3 = 0, f4 = 0, f5 = 0;
    private double FClat = 16.792976, FClong = 80.823341, FcDist;

//    private double Gammalat = 16.790655, Gammalong = 80.825180, GammaDist1;
//    private double Gammaltlat=16.790847,Gammalblat=16.790552,Gammartlat=16.790756,Gammarblat=16.790462;
//    private double Gammaltlong=80.825054,Gammalblong=80.824976,Gammartlong=80.825395,Gammarblong=80.825305;
    private double Gammalat[]=new double[]{16.790655,16.790847,16.790552,16.790756,16.790462};
    private double Gammalong[]=new double[]{80.825180,80.825054,80.824976,80.825395,80.825305};
    private double[] GammaDist=new double[5];

////    Eta points
//    private double Etalat = 16.791075, Etalong = 80.825459, EtaDist;
//
    private double Etalat[]=new double[]{16.791141,16.791323,16.791020,16.791248,16.790938};
    private double Etalong[]=new double[]{80.825307,80.825185,80.825106,80.825506,80.825430};
    private double EtaDist[]=new double[5];

    private double Lambdalat = 16.791560, Lambdalong = 80.825642, LambdaDist;
    private double Kappalat = 16.791720, Kappalong = 80.824952, KappaDist;

    // checking with four coordinates
    // end of 4 coordinates

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
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    // \n is for new line


                // Check if the user's location is inside the rectangle area

//                        if (latitude >= bottomLat && latitude <= topLat && longitude >= leftLng && longitude <= rightLng) {
//                            // User is inside the rectangle area
//                            // Do something here, such as showing a message or triggering an action
//                            Toast.makeText(MainActivity.this,"Hii vishuuuu",Toast.LENGTH_SHORT).show();
//                            Intent intent2=new Intent(MainActivity.this, MenuList.class);
//                            setNotificationContent("Vishuu location","For the love of only eating sleeping",intent2);
//                            CustomNotification("Vishuu location","For the love of only eating sleeping",intent2);
//                        } else {
//                            // User is outside the rectangle area
//                            Toast.makeText(MainActivity.this,"Byee vishuuuu",Toast.LENGTH_SHORT).show();
//                        }

            // end of rectangle area testing

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
//        LocDist = calDistance(latitude,longitude,Loclat,Loclong);
//        HomeDist = calDistance(latitude,longitude,Homelat,Homelong);

        for(int i=0;i<GammaDist.length;i++) {
            GammaDist[i] = calDistance(latitude, longitude, Gammalat[i], Gammalong[i]);
            EtaDist[i]=calDistance(latitude,longitude,Etalat[i],Etalong[i]);
        }

        LambdaDist=calDistance(latitude,longitude,Lambdalat,Lambdalong);
        KappaDist=calDistance(latitude,longitude,Kappalat,Kappalong);

//        Toast.makeText(MainActivity.this," Gamma "+GammaDist+" FC "+FcDist+" Kappa "+KappaDist+" Eta dist "+EtaDist+" Lambda "+LambdaDist,Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this," Gamma "+GammaDist[0]+" Gamma "+GammaDist[1]+" Gamma "+GammaDist[2]+" Gamma "+GammaDist[3]+" Gamma "+GammaDist[4],Toast.LENGTH_SHORT).show();
        // G  F  K  E  L
        // 35 240 31 35 31

        if (FcDist <= 52 && f1 == 0) {
            f1 = 1;
            Intent intent2=new Intent(this, MenuList.class);
            setNotificationContent("FC","For the love of delicious food...",intent2);
            CustomNotification("FC Food","For the love of delicious food...",intent2);
        }

        if (FcDist > 52) {
            f1 = 0;
        }

        int c=0,c2=0;//for cheking distance range
        for(int i=1;i<5;i++){
            if (GammaDist[i]<=10)
                c=1;
            if(EtaDist[i]<=10)
                c2=1;
        }
//        (GammaDist[0] <= 35 || GammaDist[1] <= 35 || GammaDist[2] <= 35 || GammaDist[3] <= 35 || GammaDist[4] <= 35 )
        if ((GammaDist[0]<=26 || c==1) && f2 == 0) {
            f2 = 1;
            Intent intent2=new Intent(this, GammaEvents.class);
            setNotificationContent("Gamma cluster","Don't Quit",intent2);
            CustomNotification("Gamma cluster","Don't Quit",intent2);
        }
        if(c==0 || GammaDist[0]>26){
            f2 = 0;
        }

        if ( EtaDist[0]<= 28 || c2==1 && f3 == 0) {

            f3 = 1;
            Intent intent2=new Intent(this, EtaEvents.class);
            setNotificationContent("Eta cluster","Keep going",intent2);
            CustomNotification("Eta cluster","Keep going",intent2);

        }
        if (EtaDist[0] > 28 || c2==0) {
            f3 = 0;
        }

        if ( LambdaDist<= 31 && f4 == 0) {

            f4 = 1;
            Intent intent2=new Intent(this, LambdaEvents.class);
            setNotificationContent("Lambda cluster","Keep going",intent2);
            CustomNotification("Lambda cluster","Keep going",intent2);

        }
        if (LambdaDist > 31) {
            f4 = 0;
        }

        if ( KappaDist<= 31 && f5 == 0) {

            f5 = 1;
            Intent intent2=new Intent(this, KappaEvents.class);
            setNotificationContent("Kappa cluster","Cutting edge with techies",intent2);
            CustomNotification("Kappa cluster","Cutting edge with techies",intent2);

        }
        if (KappaDist > 31) {
            f5 = 0;
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