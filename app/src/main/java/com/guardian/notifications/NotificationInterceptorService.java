package com.guardian.notifications;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.*;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.guardian.log.Logger;
import com.jaredrummler.android.processes.ProcessManager;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

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

            log("<--- NEW TASK");
            log("PackageName: " + sbn.getPackageName());
            log("TicketText: " + sbn.getNotification().tickerText);
            log("Content: " + sbn.getNotification().extras.getString("android.text"));
            log("<--- END TASK");

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
                if (statusBarNotification.getNotification().extras.getString("android.text") != null) {
                    if (statusBarNotification.getNotification().extras.getString("android.text").toUpperCase().contains("TIM")) {

                        log("TIM found, killing .. ");

                        List<AndroidAppProcess> processes = ProcessManager.getRunningAppProcesses();
                        boolean dead = false;
                        int processid = 0;

                        for (AndroidAppProcess process : processes) {

                            log("Current process name: " + process.getPackageName() + ". Searching for: " + statusBarNotification.getPackageName());
                            if (process.getPackageName().equals(statusBarNotification.getPackageName())) {
                                processid = process.stat().getPid();
                                log("The " + statusBarNotification.getPackageName() + " PID to kill is: " + processid);
                                android.os.Process.killProcess(processid);
                                log("MOTHERFUCKER !!!");
                                dead = true;
                                break;
                            }
                        }

                        log("Dead? " + dead);

                    }
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

    private void log(String toLog) {
        Log.e(TAG, toLog);
    }
}
