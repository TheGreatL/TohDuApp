package com.example.tohdu;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.tohdu.Page.GeneralTodoListPage;

public class AlarmReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        int id = intent.getIntExtra("id", -1);
        int requestCode = intent.getIntExtra("reqCode", -1);


        Intent nextActivity = new Intent(context, GeneralTodoListPage.class);
        nextActivity.putExtra("data0","ViewAlarm");
        nextActivity.putExtra("data1",id);
        nextActivity.putExtra("data2","NOT DONE");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, nextActivity, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);


        // Create a VibrationEffect with the specified duration
        VibrationEffect effect = VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE);
        vibrator.vibrate(effect);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "TohDuApp")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Time To Do Your Task: " + title)
                .setContentText(description)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Please allow all permissions!!", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("SampleDebugTag", "ID: " + id);
        notificationManagerCompat.notify(id, builder.build());
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.remind_ringtone);

        Ringtone r = RingtoneManager.getRingtone(context, sound);
        r.play();

        new Handler().postDelayed(r::stop,5000);


    }
}