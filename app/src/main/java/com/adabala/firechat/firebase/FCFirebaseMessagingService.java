package com.adabala.firechat.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.adabala.firechat.R;
import com.adabala.firechat.contacts.ContactsActivity;
import com.adabala.firechat.database.ApplicationAccess;
import com.adabala.firechat.di.Injector;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import javax.inject.Inject;

import timber.log.Timber;

public class FCFirebaseMessagingService extends FirebaseMessagingService {

    @Inject
    ApplicationAccess applicationAccess;

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.INSTANCE.getAppComponent().inject(FCFirebaseMessagingService.this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String senderId = remoteMessage.getData().get("senderId");
        String message = remoteMessage.getData().get("message");

        if(applicationAccess.showNotification) {
            Timber.d("Show Push App is not active : %s", remoteMessage);
            showNotification(senderId, message);
        } else {
            Timber.d("Silent Push App is active : %s", remoteMessage);
        }
    }

    private void showNotification(String senderId, String message) {
        final NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        builder.setContentTitle(senderId);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setContentText(message);
        builder.setAutoCancel(true);

        Intent notificationIntent = new Intent(this, ContactsActivity.class);
        int uniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        Notification updatedNotification = builder.build();
        updatedNotification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;

        notificationManager.notify(0, updatedNotification);
    }
}
