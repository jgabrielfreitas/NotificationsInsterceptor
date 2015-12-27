package com.guardian.notifications;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.guardian.log.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotificationInterceptorService extends NotificationListenerService {

    private static final String TAG = "Interceptor";

    public NotificationInterceptorService() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        killTimNotification();
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        try {

            Log.e(TAG, "PackageName: " + sbn.getPackageName());
            Log.e(TAG, "TicketText: " + sbn.getNotification().tickerText);
            Log.e(TAG, "Content: " + sbn.getNotification().extras.getString("android.text"));
            Log.e(TAG, "Others: " + sbn.getNotification().extras.toString());

//            Logger logger = new Logger();
//            logger.setPackageName(sbn.getPackageName());
//            logger.setTicket("" + sbn.getNotification().tickerText);
//            logger.setContent(sbn.getNotification().extras.getString("android.text"));
//            logger.setBundleExtra(sbn.getNotification().extras.toString());
//            logger.save();

            // always call the killer
//            killTimNotification();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void killTimNotification() {

        try {

            for (StatusBarNotification statusBarNotification : this.getActiveNotifications()) {
                if (statusBarNotification.getNotification().extras.getString("android.text").toUpperCase().contains("MENU")) {

                    ActivityManager am = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);

                    Log.e(TAG, "TIM found, killing in 3 seconds.. ");
                    Thread.sleep(3000);
                    Log.e(TAG, "isClearable? " + statusBarNotification.isClearable());
                    am.killBackgroundProcesses(statusBarNotification.getPackageName());
                    Log.e(TAG, String.format("Process %s dead.", statusBarNotification.getPackageName()));

                    stopForeground(true);
                    cancelNotification(statusBarNotification.getKey());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO put it on a Broadcast
    private void applicationsDump() {

        PackageManager packageManager = this.getPackageManager();
        List<ApplicationInfo> applist = packageManager.getInstalledApplications(0);
        List<String> strings = new ArrayList<>();

        for (ApplicationInfo applicationInfo : applist) {
            ApplicationInfo pk = applicationInfo;

            String appname = packageManager.getApplicationLabel(pk).toString();
            if (strings.contains(appname) == false)
                strings.add(appname);

        }

        for (String string : strings) {

            Log.e(TAG, "Installed: " + string);
        }
        Log.e(TAG, "Total of installed: " + strings.size());
    }
}
