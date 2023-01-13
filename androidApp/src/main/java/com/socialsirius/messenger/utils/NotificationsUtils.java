package com.socialsirius.messenger.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;


import static android.media.AudioManager.STREAM_NOTIFICATION;

import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnRequest;
import com.socialsirius.messenger.R;
import com.socialsirius.messenger.base.App;
import com.socialsirius.messenger.models.Chats;
import com.socialsirius.messenger.ui.activities.main.MainActivity;
import com.socialsirius.messenger.ui.activities.message.MessageActivity;


/**
 * Created by John on 22.09.2016.
 */

public class NotificationsUtils {


    private static long timestamp;
    private static long timestamp2;

    public static void removeMessageNotify(Context context, String jidFrom) {
        int hash = jidFrom.hashCode();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(hash);

    }


    public static void removeAllMessageNotify(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

    }

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    public static final String MESSAGE_GROUP = "messages";
    public static final String ACTIVITY_GROUP = "activity";
    public static final String REMINDER_GROUP = "reminder";
    public static final String REQUEST_GROUP = "request";
    public static final String CHANNELID_REQUEST_SOUND_VIBRO = "request_sound_vibration";
    public static final String CHANNELID_MESSAGE_SOUND_VIBRO = "messages_sound_vibration";
    public static final String CHANNELID_MESSAGE_SILENT = "messages_silent";
    public static final String CHANNELID_MESSAGE_ONLY_SOUND = "messages_only_sound";
    public static final String CHANNELID_MESSAGE_ONLY_VIBRO = "messages_only_vibration";

    public static final String CHANNELID_ACTIVITY_SOUND_VIBRO = "activity_sound_vibration";
    public static final String CHANNELID_ACTIVITY_SILENT = "activity_silent";
    public static final String CHANNELID_ACTIVITY_ONLY_SOUND = "activity_only_sound";
    public static final String CHANNELID_ACTIVITY_ONLY_VIBRO = "activity_only_vibration";

    public static final String CHANNELID_REMINDER_SOUND_VIBRO = "reminder_sound_vibration";
    public static final String CHANNELID_REMINDER_SILENT = "reminder_silent";
    public static final String CHANNELID_REMINDER_ONLY_SOUND = "reminder_only_sound";
    public static final String CHANNELID_REMINDER_ONLY_VIBRO = "reminder_only_vibration";
    public static final String CHANNELID_SHORTCUT_BADGER = "shortcut_badger_channel";


    public static void createOneChannel(String groupName, String groupTitle, String channelName,
                                        String title, boolean enableVibration, boolean enableSound, int soundRaw) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) App.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            Uri path = Uri.parse("android.resource://" + App.getContext().getPackageName() + "/" + soundRaw);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(STREAM_NOTIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            NotificationChannel channel = new NotificationChannel(channelName, title, importance);
            channel.enableLights(true);
            channel.setShowBadge(true);
            channel.setBypassDnd(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setLightColor(Color.RED);
            if (enableSound) {
                channel.setSound(path, audioAttributes);
            }
            channel.enableVibration(enableVibration);
            if (notificationManager != null) {
                NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup(groupName, groupTitle);
                notificationManager.createNotificationChannelGroup(notificationChannelGroup);
                channel.setGroup(groupName);
                notificationManager.createNotificationChannel(channel);
            }
        }

    }

    public static void createChannelsForNotifications() {
        //CHANNELS FOR  MESSAGE

        String requestGroupName = App.getContext().getString(R.string.settings_request_notification);
        createOneChannel(REQUEST_GROUP, requestGroupName, CHANNELID_REQUEST_SOUND_VIBRO,
                App.getContext().getString(R.string.settings_audio_vibration), true, true, R.raw.message_sound);


        //cahnnel for Normal with sound and vibro
        String mesageGroupName = App.getContext().getString(R.string.settings_messages_notification);
        createOneChannel(MESSAGE_GROUP, mesageGroupName, CHANNELID_MESSAGE_SOUND_VIBRO,
                App.getContext().getString(R.string.settings_audio_vibration), true, true, R.raw.new_chat_message);
        //channel with only sound
        createOneChannel(MESSAGE_GROUP, mesageGroupName, CHANNELID_MESSAGE_ONLY_SOUND,
                App.getContext().getString(R.string.settings_audio_only), false, true, R.raw.new_chat_message);
        //channel with only vibro
        createOneChannel(MESSAGE_GROUP, mesageGroupName, CHANNELID_MESSAGE_ONLY_VIBRO,
                App.getContext().getString(R.string.settings_vibration_only), true, false, R.raw.new_chat_message);
        //channel for many in one time - silent
        createOneChannel(MESSAGE_GROUP, mesageGroupName, CHANNELID_MESSAGE_SILENT,
                App.getContext().getString(R.string.settings_no_sound), false, false, R.raw.new_chat_message);


        //CHANNELS FOR  ACTIVITY
        String mesageGroupName2 = App.getContext().getString(R.string.settings_activity_notification);
        createOneChannel(ACTIVITY_GROUP, mesageGroupName2, CHANNELID_ACTIVITY_SOUND_VIBRO,
                App.getContext().getString(R.string.settings_audio_vibration), true, true, R.raw.task_comment);
        //channel with only sound
        createOneChannel(ACTIVITY_GROUP, mesageGroupName2, CHANNELID_ACTIVITY_ONLY_SOUND,
                App.getContext().getString(R.string.settings_audio_only), false, true, R.raw.task_comment);
        //channel with only vibro
        createOneChannel(ACTIVITY_GROUP, mesageGroupName2, CHANNELID_ACTIVITY_ONLY_VIBRO,
                App.getContext().getString(R.string.settings_vibration_only), true, false, R.raw.task_comment);
        //channel for many in one time - silent
        createOneChannel(ACTIVITY_GROUP, mesageGroupName2, CHANNELID_ACTIVITY_SILENT,
                App.getContext().getString(R.string.settings_no_sound), false, false, R.raw.task_comment);

        //CHANNELS FOR  REMINDER
        String mesageGroupName3 = App.getContext().getString(R.string.settings_reminder_notification);
        createOneChannel(REMINDER_GROUP, mesageGroupName3, CHANNELID_REMINDER_SOUND_VIBRO,
                App.getContext().getString(R.string.settings_audio_vibration), true, true, R.raw.task);
        //channel with only sound
        createOneChannel(REMINDER_GROUP, mesageGroupName3, CHANNELID_REMINDER_ONLY_SOUND,
                App.getContext().getString(R.string.settings_audio_only), false, true, R.raw.task);
        //channel with only vibro
        createOneChannel(REMINDER_GROUP, mesageGroupName3, CHANNELID_REMINDER_ONLY_VIBRO,
                App.getContext().getString(R.string.settings_vibration_only), true, false, R.raw.task);
        //channel for many in one time - silent
        createOneChannel(REMINDER_GROUP, mesageGroupName3, CHANNELID_REMINDER_SILENT,
                App.getContext().getString(R.string.settings_no_sound), false, false, R.raw.task);

    }




   /* public static void callRequestNotify(Context context, FirebaseIndy firebaseIndy) {
        int hash = firebaseIndy.getDid().hashCode();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        long time = System.currentTimeMillis();
        String channelId = CHANNELID_REQUEST_SOUND_VIBRO;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setSmallIcon(R.drawable.ic_lock);
        builder.setContentTitle(App.getContext().getString(R.string.request_notification_title));
        // builder.set
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        String text = String.format(App.getContext().getString(R.string.request_notification_text), firebaseIndy.getOwner());
        builder.setContentText(text);
        Intent notificationIntent = new Intent(context, RequestSiriusDataActivity.class);
        notificationIntent.setAction(StaticFields.NOTIFY_REQUEST_MESSAGE);
        notificationIntent.putExtra(StaticFields.BUNDLE_DATA, firebaseIndy);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, hash, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(notificationPendingIntent);
        Notification nt = builder.build();
        nt.flags |= Notification.FLAG_AUTO_CANCEL;
        nt.flags |= Notification.FLAG_SHOW_LIGHTS;
        nt.ledARGB = Color.RED;
        nt.ledOffMS = 500;
        nt.ledOnMS = 500;

        if (notificationManager != null) {
            notificationManager.notify(hash, nt);
        }
    }*/




    public static void callMessageNotify(String title, String text, String jidFrom, Chats chats) {
        int hash = jidFrom.hashCode();
        NotificationManager notificationManager = (NotificationManager) App.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        long time = System.currentTimeMillis();
        String channelId = CHANNELID_MESSAGE_SOUND_VIBRO;
      //  if (AppPref.getInstance().getIsSoundNewMessage() && AppPref.getInstance().isSoundNewMessageVibro()) {
            channelId = CHANNELID_MESSAGE_SOUND_VIBRO;
      /*  } else if (AppPref.getInstance().getIsSoundNewMessage() && !AppPref.getInstance().isSoundNewMessageVibro()) {
            channelId = CHANNELID_MESSAGE_ONLY_SOUND;
        } else if (!AppPref.getInstance().getIsSoundNewMessage() && AppPref.getInstance().isSoundNewMessageVibro()) {
            channelId = CHANNELID_MESSAGE_ONLY_VIBRO;
        } else {
            channelId = CHANNELID_MESSAGE_SILENT;
        }
*/
        if (timestamp == 0 || time - timestamp > 15000L) {

        } else {
            channelId = CHANNELID_MESSAGE_SILENT;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getContext(), channelId);
        builder.setSmallIcon(R.drawable.logo_sirius);
        builder.setContentTitle(title);

        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        /*if (text != null) {
            if (text.contains(SERVICE_MESSAGE_START_TAG)) {
                text = context.getString(R.string.document);
            } else if (text.startsWith(StaticFields.BOT_SERVICE_MESSAGE_START_TAG)) {
                text = context.getString(R.string.service_message);
            } else if (text.startsWith(StaticFields.BOT_HTML_MESSAGE_START_TAG)) {
                text = context.getString(R.string.service_message);
            } else if (text.startsWith(StaticFields.INDY_TRANSPORT_START_TAG)) {
                text = context.getString(R.string.service_message);
            }else if(text.startsWith(StaticFields.INDY_CREDENTIAL_START_TAG)){
                text = context.getString(R.string.credential_message);
            }else if(text.startsWith(StaticFields.INDY_PRESENT_PROOF_START_TAG)){
                text = context.getString(R.string.proof_request_message);
            }
            text = text.replace("<br/>", "\n");
        }*/


        builder.setContentText(text);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        boolean isSecret = false;


        Intent notificationIntent = new Intent(App.getContext(), MainActivity.class);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(App.getContext());
        stackBuilder.addNextIntent(notificationIntent);

        if (chats != null) {
            Intent resultIntent = new Intent(App.getContext(), MessageActivity.class);
            resultIntent.putExtra("chat", chats);
            stackBuilder.addNextIntent(resultIntent);
        }

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(hash, PendingIntent.FLAG_ONE_SHOT);
        //PendingIntent notificationPendingIntent = PendingIntent.getActivities(context, hash, intents, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(resultPendingIntent);
        Notification nt = builder.build();
        if (timestamp == 0 || time - timestamp > 15000L) {
            timestamp = time;
           // if (AppPref.getInstance().getIsSoundNewMessage()) {
                Uri path = Uri.parse("android.resource://" + App.getContext().getPackageName() + "/" + R.raw.new_chat_message);
                nt.sound = path;
           // }
           // if (AppPref.getInstance().isSoundNewMessageVibro()) {
                nt.defaults |= Notification.DEFAULT_VIBRATE;
          //  }
        }
        nt.flags |= Notification.FLAG_AUTO_CANCEL;
        nt.flags |= Notification.FLAG_SHOW_LIGHTS;
        nt.ledARGB = Color.RED;
        nt.ledOffMS = 500;
        nt.ledOnMS = 500;

        if (notificationManager != null) {
            notificationManager.notify(hash, nt);
        }
    }

    public static void callNewConnectionNotify(ConnRequest connRequest ) {
        String title = connRequest.getLabel();
        String jidFrom = connRequest.theirDid();
        int hash = jidFrom.hashCode();
        NotificationManager notificationManager = (NotificationManager) App.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        long time = System.currentTimeMillis();
        String channelId = CHANNELID_MESSAGE_SOUND_VIBRO;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getContext(), channelId);
        builder.setSmallIcon(R.drawable.logo_sirius);
        builder.setContentTitle(App.getContext().getString(R.string.notification_new_connection_title));
        String text =  String.format(App.getContext().getString(R.string.notification_new_connection),title);
        builder.setContentText(text);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setContentText(text);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        Intent notificationIntent = new Intent(App.getContext(), MainActivity.class);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.putExtra("invitation",connRequest.getId());

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(App.getContext(), hash, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(notificationPendingIntent);
        Notification nt = builder.build();

            timestamp = time;
            // if (AppPref.getInstance().getIsSoundNewMessage()) {
            Uri path = Uri.parse("android.resource://" + App.getContext().getPackageName() + "/" + R.raw.new_chat_message);
            nt.sound = path;
            // }
            // if (AppPref.getInstance().isSoundNewMessageVibro()) {
            nt.defaults |= Notification.DEFAULT_VIBRATE;
            //  }

        nt.flags |= Notification.FLAG_AUTO_CANCEL;
        nt.flags |= Notification.FLAG_SHOW_LIGHTS;
        nt.ledARGB = Color.RED;
        nt.ledOffMS = 500;
        nt.ledOnMS = 500;

        if (notificationManager != null) {
            notificationManager.notify(hash, nt);
        }
    }


    static final long[] DEFAULT_VIBRATE_PATTERN = {0, 250, 250, 250};




}
