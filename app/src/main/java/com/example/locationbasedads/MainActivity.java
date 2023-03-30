package com.example.locationbasedads;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity{

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    private static int gpsPermission=0;

    GPSTracker gps;

    // update location time
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000;

//    private static int f1 =0,f2=0,f3=0,f4=0,f5=0;
    private static int f[]=new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    double FClat=16.7930064,FClong=80.8231883,FcDist;
    double Gammalat=16.790551,Gammalong=80.825317,GammaDist;
    double Etalat=16.791075,Etalong=80.825459,EtaDist;
    double Lambdalat=16.791557,Lambdalong=80.825653,LambdaDist;
    double Kappalat=16.791720,Kappalong=80.824952,KappaDist;
    double Prefablat=16.790399,Prefablong=80.825571,PrefabDist;
    double Muelat=16.792029,Muelong=80.825798,MueDist;
    double Omegalat=16.792199,Omegalong=80.825134,OmegaDist;
    double Saclat=16.793966,Saclong=80.827151,SacDist;
    double Groundlat=16.794296,Groundlong=80.826092,GroundDist;
    double ABlat=16.791746,ABlong=80.821225,ABDist;
    double Hospitallat=16.793463,Hospitallong=80.824586,HospitalDist;
    double Atmlat=16.793674,Atmlong=80.825338,AtmDist;

    private Button viewButton;
    //for notification message

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewButton=findViewById(R.id.customButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String referenceName = coursesLV2.getItemAtPosition(position).toString();
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                //   String referenceName = intent.getStringExtra("reference_name");
                //Toast.makeText(MainActivity.this, "ref "+referenceName, Toast.LENGTH_SHORT).show();

                startActivity(intent);

            }
        });

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

////        Toast.makeText(MainActivity.this,""+distance,Toast.LENGTH_SHORT).show();

        return distance;
    }
    private void matchLocationNotification(double latitude, double longitude) {
        FcDist = calDistance(latitude, longitude, FClat, FClong);
//        LocDist = calDistance(latitude,longitude,Loclat,Loclong);
//        HomeDist = calDistance(latitude,longitude,Homelat,Homelong);
        GammaDist = calDistance(latitude,longitude,Gammalat,Gammalong);
        EtaDist=calDistance(latitude,longitude,Etalat,Etalong);
        LambdaDist=calDistance(latitude,longitude,Lambdalat,Lambdalong);
        KappaDist=calDistance(latitude,longitude,Kappalat,Kappalong);
        PrefabDist=calDistance(latitude,longitude,Prefablat,Prefablong);
        MueDist=calDistance(latitude,longitude,Muelat,Muelong);
        OmegaDist=calDistance(latitude,longitude,Omegalat,Omegalong);
        SacDist=calDistance(latitude,longitude,Saclat,Saclong);
        GroundDist=calDistance(latitude,longitude,Groundlat,Groundlong);
        ABDist=calDistance(latitude,longitude,ABlat,ABlong);
        HospitalDist=calDistance(latitude,longitude,Hospitallat,Hospitallong);
        AtmDist=calDistance(latitude,longitude,Atmlat,Atmlong);


//        Toast.makeText(MainActivity.this,"kappa "+Math.round(KappaDist)+"\n Gamma "+Math.round(GammaDist)+"\n Eta "+Math.round(EtaDist)+"\n Fc "+Math.round(FcDist)+"\n Lambda "+Math.round(LambdaDist)+"\n Prefab "+Math.round(PrefabDist)+"\n Mue "+Math.round(MueDist)+"\n Omega "+Math.round(OmegaDist)+"\n SAC "+Math.round(SacDist)+"\n Ground "+Math.round(GroundDist)+"\n AB "+Math.round(ABDist)+"\n Hospital "+Math.round(HospitalDist)+"\n ATM "+Math.round(AtmDist)+"\n Hhand "+Math.round(HhandDist),Toast.LENGTH_SHORT).show();


        if (FcDist <= 50 && f[0] == 0) {
            f[0] = 1;
            Intent intent2=new Intent(this, MenuList.class);
            setNotificationContent("Food-court","For the love of delicious food...",intent2);
            CustomNotification("Food-court","For the love of delicious food...",intent2);
        }

        if (FcDist > 50) {
            f[0] = 0;
        }

        if (GammaDist <= 35 && f[1] == 0) {

            f[1] = 1;
            Intent intent2=new Intent(this, GammaEvents.class);
            setNotificationContent("Gamma cluster","Experts were once beginners",intent2);
            CustomNotification("Gamma cluster","Experts were once beginners",intent2);

        }
        if (GammaDist > 35) {
            f[1] = 0;
        }

        if ( EtaDist<= 35 && f[2] == 0) {

            f[2] = 1;
            Intent intent2=new Intent(this, EtaEvents.class);
            setNotificationContent("Eta cluster","Experts were once beginners",intent2);
            CustomNotification("Eta cluster","Experts were once beginners",intent2);

        }
        if (EtaDist > 35) {
            f[2] = 0;
        }

        if ( LambdaDist<= 35 && f[3] == 0) {

            f[3] = 1;
            Intent intent2=new Intent(this, LambdaEvents.class);
            setNotificationContent("Lambda cluster","Experts were once beginners",intent2);
            CustomNotification("Lambda cluster","Experts were once beginners",intent2);

        }
        if (LambdaDist > 35) {
            f[3] = 0;
        }

       if ( KappaDist<= 31 && f[4] == 0) {

            f[4] = 1;
            Intent intent2=new Intent(this, KappaEvents.class);
            setNotificationContent("Kappa cluster","Experts were once beginners",intent2);
            CustomNotification("Kappa cluster","Experts were once beginners",intent2);

        }
        if (KappaDist > 31) {
            f[4] = 0;
        }

        if ( PrefabDist<= 30 && f[5] == 0) {

            f[5] = 1;
            Intent intent2=new Intent(this, PrefabEvents.class);
            setNotificationContent("Prefab","Enjoy the moments",intent2);
            CustomNotification("Prefab ","Enjoy the moments",intent2);

        }
        if (PrefabDist > 30) {
            f[5] = 0;
        }

        if ( MueDist<= 30 && f[6] == 0) {

            f[6] = 1;
            Intent intent2=new Intent(this, MueActivity.class);
            setNotificationContent("Mue cluster","Experts were once beginners",intent2);
            CustomNotification("Mue cluster","Experts were once beginners",intent2);

        }
        if (MueDist > 30) {
            f[6] = 0;
        }

        if ( OmegaDist<= 30 && f[7] == 0) {

            f[7] = 1;
            Intent intent2=new Intent(this, OmegaEventa.class);
            setNotificationContent("Omega cluster","Experts were once beginners",intent2);
            CustomNotification("Omega cluster","Experts were once beginners",intent2);

        }
        if (OmegaDist > 30) {
            f[7] = 0;
        }

        if ( SacDist<= 80 && f[8] == 0) {

            f[8] = 1;
            Intent intent2=new Intent(this, SacEvents.class);
            setNotificationContent("SAC","Keep playing",intent2);
            CustomNotification("SAC","Keep playing",intent2);

        }
        if (SacDist > 80) {
            f[8] = 0;
        }

        if ( GroundDist<= 70 && f[9] == 0) {

            f[9] = 1;
            Intent intent2=new Intent(this, GroundEvents.class);
            setNotificationContent("Ground","Enjoy the moments",intent2);
            CustomNotification("Ground ","Enjoy the moments",intent2);

        }
        if (GroundDist > 70) {
            f[9] = 0;
        }
// 140
        if ( ABDist<= 140 && f[10] == 0) {

            f[10] = 1;
            Intent intent3=new Intent(MainActivity.this, ABEvents.class);
            setNotificationContent("Academic blocks","Enjoy the moments",intent3);
            CustomNotification("Academic blocks","Enjoy the moments",intent3);
        }
        if (ABDist > 140) {
            f[10] = 0;
        }

        if ( HospitalDist<= 40 && f[11] == 0) {

            f[11] = 1;
            Intent intent2=new Intent(this, HospitalEvents.class);
            setNotificationContent("Hospital","Enjoy the moments",intent2);
            CustomNotification("Hospital","Enjoy the moments",intent2);

        }
        if (HospitalDist > 40) {
            f[11] = 0;
        }

        if ( AtmDist<= 30 && f[12] == 0) {

            f[12] = 1;
            Intent intent2=new Intent(this, AtmEvents.class);
            setNotificationContent("ATM","Enjoy the moments",intent2);
            CustomNotification("ATM","Enjoy the moments",intent2);

        }
        if (AtmDist > 30) {
            f[12] = 0;
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
//                .setColor(ContextCompat.getColor(this,R.color.notification_text_color))
                .setContent(remoteViews);

//        ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.notification_text_color);
//        // Set the text color of the notification in both light and dark themes
//        remoteViews.setTextColor(R.id.title, 1);
//        remoteViews.setTextColor(R.id.message, 2);
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