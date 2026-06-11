package com.example.smartcart.helpers;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class NotificationsHandler extends Worker {

    final String channelId = "SmartCartNotificationsChannel";

    public NotificationsHandler(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        sendNotification();
        return Result.success();
    }

    private void sendNotification() {
        NotificationManager manager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();



        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle("SmartCart Reminder")
                .setContentText("come and check your shopping list")
                .setSmallIcon(android.R.drawable.ic_dialog_info);


            manager.notify(1, builder.build());

    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Work Notifications";
            String description = "Notifications for WorkManager tasks";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public boolean isNotificationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void schedule(Context context) {
        Calendar now = Calendar.getInstance();
        Calendar nextNotificationTime = Calendar.getInstance();
        nextNotificationTime.set(Calendar.HOUR_OF_DAY, 8);
        nextNotificationTime.set(Calendar.MINUTE, 0);
        nextNotificationTime.set(Calendar.SECOND, 0);

        if(nextNotificationTime.before(now)) {
            nextNotificationTime.add(Calendar.DAY_OF_MONTH, 1);
        }

        long delay = nextNotificationTime.getTimeInMillis() - now.getTimeInMillis();

        PeriodicWorkRequest notificationWork = new PeriodicWorkRequest.Builder(NotificationsHandler.class, 1, TimeUnit.DAYS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "DailyNotificationWork",
                ExistingPeriodicWorkPolicy.KEEP,
                notificationWork
        );

    }

    public static void cancel(Context context) {
        WorkManager.getInstance(context).cancelUniqueWork("DailyNotificationWork");
    }







}
