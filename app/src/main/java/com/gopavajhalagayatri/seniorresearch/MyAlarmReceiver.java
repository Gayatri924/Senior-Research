package com.gopavajhalagayatri.seniorresearch;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.core.app.NotificationCompat;

public class MyAlarmReceiver extends BroadcastReceiver {

    int fifteenMin = 900000;
    String TAG = "PETRICHOR";
    DatabaseHelper db = new DatabaseHelper(null);

    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager pm= context.getPackageManager();
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long currentTime = System.currentTimeMillis(); // Get current time in milliseconds
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1); // Set year to beginning of desired period.
        long beginTime = cal.getTimeInMillis(); // Get begin time in milliseconds
        List<UsageStats> queryUsageStats=usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, currentTime);
        int count = 0;
        int socialVal = 0;
        for(UsageStats val: queryUsageStats){
            long fiveMin = currentTime - fifteenMin;
            if(val.getLastTimeUsed() > fiveMin) {
                count += 1;
                ApplicationInfo appInfo = null;
                try {
                    appInfo = pm.getApplicationInfo(val.getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (appInfo.category == 4) {
                    socialVal++;
                } else {
                    socialVal--;
                }
            }
        }
        Log.i(TAG, count + "  " + socialVal);
        //ArrayList<Task> tasks = db.getAllTasks();
        String name = "";
        if(count == 0){
            return;
        }
        /*if(count <=0){
            for(Task item: tasks){
                if((item.time < 15 || item.name.contains("email")) && !item.state){
                    name = item.name;
                    break;
                }
            }
        }else {
            for (Task item : tasks) {
                if (item.time > 15 && !item.state) {
                    name = item.name;
                    break;
                }
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(null, "Channel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("productivity")
                .setContentText(name)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);*/
    }
}
