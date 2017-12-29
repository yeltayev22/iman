package zerotoonecom.n17r.mubr;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import java.util.Locale;

public class Mote extends BroadcastReceiver{

    private NotificationManager nm;

    public void onReceive(Context context, Intent intent) {

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

        String chosenLang = settings.getString(context.getString(R.string.language_key), "ru");

        Locale locale = new Locale(chosenLang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        String azan = settings.getString(context.getString(R.string.azan_key), "something");

        Uri alarmSound = null;

        if (azan.compareTo(context.getString(R.string.no_value)) == 0){
            String strRingtonePreference = settings.getString(context.getString(R.string.ringtone_key), "ringtone");
            alarmSound =  Uri.parse(strRingtonePreference);
        } else if (azan.compareTo(context.getString(R.string.azan_kasym_value)) == 0) {
            alarmSound =  Uri.parse("android.resource://"
                        + context.getPackageName() + "/" + R.raw.kasym);
        } else if (azan.compareTo(context.getString(R.string.azan_kasym_1_value)) == 0) {
            alarmSound =  Uri.parse("android.resource://"
                    + context.getPackageName() + "/" + R.raw.kasym1);
        }

        String namaz_name = intent.getStringExtra("namaz_name");

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle(context.getString(R.string.azan))
                        .setContentText(namaz_name)
                        .setSound(alarmSound);

        Intent notificationIntent = new Intent(context, MainActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);
        //builder.setAutoCancel(true);
        builder.setStyle(new NotificationCompat.InboxStyle());
        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
        if(settings.getBoolean(context.getString(R.string.vibrate_key), false)){
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(1000);
        }

    }



}