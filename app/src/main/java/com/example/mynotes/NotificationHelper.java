package com.example.mynotes;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    private NotificationManager mManager;
    int id;
    Note note;
    private List<Note> notesList;
    int position;


    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;

    }




    public NotificationCompat.Builder getChannelNotification() {
        Intent resultIntent = new Intent(this,MainActivity.class);
      Note note = notesList.get(position);
       resultIntent.putExtra("id",note.getId());
        PendingIntent pendingresultIntent = PendingIntent.getActivities(this,1, new Intent[]{resultIntent},PendingIntent.FLAG_UPDATE_CURRENT);



        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("New Notification!!")
                .setContentText((CharSequence) resultIntent.putExtra("id",note.getId()))
                .setSmallIcon(R.drawable.ic_photo_camera_black_material)
                .setContentIntent(pendingresultIntent)
                .setAutoCancel(true)
                ;
    }

}
