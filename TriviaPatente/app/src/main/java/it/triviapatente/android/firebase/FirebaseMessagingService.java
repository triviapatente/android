package it.triviapatente.android.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Map;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.SharedTPPreferences;
import it.triviapatente.android.app.views.main_page.MainPageActivity;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.auth.User;
import it.triviapatente.android.models.game.Game;
import it.triviapatente.android.socket.modules.base.BaseSocketManager;

/**
 * Created by donadev on 15/12/17.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(SharedTPPreferences.currentUser() == null) return;
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getData(), remoteMessage.getNotification());
        }
    }
    //ne cancella solo una, perchè è impossibile che ci sia più di una notifica per gameId
    public static void clearNotifications(Context context, Long gameId) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(gameId.intValue());
    }
    public static void sendNotificationSTUB(Context context) {
        Intent intent = new Intent(context, MainPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("Prova")
                .setContentText("Test")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = builder.setSmallIcon(R.mipmap.logo).setColor(context.getResources().getColor(R.color.mainColor));
        } else {
            builder = builder.setSmallIcon(R.mipmap.logo);
        }
        Notification notification = builder.build();

        notification.defaults |= Notification.DEFAULT_ALL;

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //fa il replace di quelle di game diversi
        notificationManager.notify(0, notification);
    }
    private void sendNotification(Map<String, String> data, RemoteMessage.Notification fcmNotification) {
        String  gameData = data.get(getString(R.string.firebase_message_game_key)),
                opponentData = data.get(getString(R.string.firebase_message_opponent_key));
        Game game = RetrofitManager.gson.fromJson(gameData, Game.class);
        if(game.id.equals(BaseSocketManager.joinedRooms.get("game"))) return;
        Intent intent = new Intent(this, MainPageActivity.class);

        intent.putExtra(getString(R.string.extra_firebase_game_param), gameData);
        intent.putExtra(getString(R.string.extra_firebase_opponent_param), opponentData);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new Notification.Builder(this)
                                                .setSmallIcon(R.mipmap.logo)
                                                .setContentTitle(fcmNotification.getTitle())
                                                .setContentText(fcmNotification.getBody())
                                                .setAutoCancel(true)
                                                .setSound(defaultSoundUri)
                                                .setContentIntent(pendingIntent)
                                                .build();

        notification.defaults |= Notification.DEFAULT_ALL;

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //fa il replace di quelle di game diversi
        notificationManager.cancel(game.id.intValue());
        notificationManager.notify(game.id.intValue(), notification);
    }
}
