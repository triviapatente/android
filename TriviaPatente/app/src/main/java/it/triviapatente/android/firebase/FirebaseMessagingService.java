package it.triviapatente.android.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
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
        Notification notification = new NotificationCompat.Builder(this)
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

        notificationManager.notify(game.id.intValue(), notification);
    }
}
