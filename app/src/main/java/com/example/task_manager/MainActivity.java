package com.example.task_manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    TextView rmStat, battery_health , battery_Temp ;
    Handler handler =  new Handler();
    Runnable updateTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        rmStat = findViewById(R.id.ram_status);
        battery_health = findViewById(R.id.batteryHealthText);
        battery_Temp = findViewById(R.id.batt_Temp);

       battery_health.setText("Battery Health: " + getBatteryHealth(this)); // showing Battery Health

        battery_Temp.setText("Battery Temperature : " +getBatteryTemp(this) + " °C"); // Showing Battery Temp

        updateTask = new Runnable() {
            @Override
            public void run() {
                showRamUsage();
                handler.postDelayed(this, 1000) ;
            }
        } ;

           handler.post(updateTask);
    }

    void showRamUsage() { // RAM use percantage
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo() ;
        activityManager.getMemoryInfo(memoryInfo);

        long total = memoryInfo.totalMem ;
        long free = memoryInfo.availMem ;
        long used = total - free ;

        int usedPercantage = (int) ((used *100.0)/total) ;

        rmStat.setText("RAM used : " +usedPercantage +"%") ;
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(updateTask); // Stop when app closes
        super.onDestroy();
    }

    private String getBatteryHealth(Context context) { // Battery health status
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        if (batteryStatus == null) return "Unknown";

        int health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        String healthString;

        Log.d("BatteryHealthRaw", "Health code = " + health);


        switch (health) {
            case BatteryManager.BATTERY_HEALTH_GOOD:
                healthString = "Good";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                healthString = "Overheat";
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                healthString = "Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                healthString = "Over Voltage";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                healthString = "Failure";
                break;
            default:
                healthString = "Unknown";
                break;
        }

        return healthString;
    }


    private String getBatteryTemp(Context context) {

        Intent intent =  context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED)) ;

        if(intent != null) {
            int temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) ;

            return String.valueOf((temp/10.0f)) ;
        }

        return "Not available" ; // error case
    }



}