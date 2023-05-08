package com.genesiswtech.lkwb.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.genesiswtech.lkwb.R
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.Navigation
import com.genesiswtech.lkwb.ui.blog.BlogActivity
import com.genesiswtech.lkwb.ui.login.LoginActivity
import com.genesiswtech.lkwb.ui.login.model.LoginDataResponse
import com.genesiswtech.lkwb.ui.main.MainActivity
import com.genesiswtech.lkwb.utils.*
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.eventbus.EventBus
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.util.PublicSuffixMatcherLoader.getDefault
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class LKWBMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "LKWBMessagingService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")
        Log.d(TAG, "Data: ${remoteMessage.data}")
        sendNotification(remoteMessage)
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
    }

    private fun sendNotification(messageBody: RemoteMessage) {

        val data = messageBody.data
        val intent: Intent
        val userData = LKWBPreferencesManager.get<LoginDataResponse>(LKWBConstants.KEY_USER)
        if (userData != null) {
            if (data["type"] == "Blog") {
                intent = Intent(this, BlogActivity::class.java)
            } else {
                intent = Intent(this, MainActivity::class.java)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                intent.setAction(Intent.ACTION_MAIN)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                if (data["type"] == "Set" || data["type"] == "Package")
                    intent.putExtra(LKWBConstants.DATA_TYPE, 1)
                else if (data["type"] == "Grammar")
                    intent.putExtra(LKWBConstants.DATA_TYPE, 2)
                else if (data["type"] == "Word")
                    intent.putExtra(LKWBConstants.DATA_TYPE, 3)
                else
                    intent.putExtra(LKWBConstants.DATA_TYPE, 0)
                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)

            }
        } else {
            intent = Intent(this, LoginActivity::class.java)
        }

        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent,
//            PendingIntent.FLAG_NO_CREATE
//        )

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "my_channel_id_01"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            // Configure the notification channel.
//            notificationChannel.description = remoteMessage.notification?.body
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.lkwb_notification_icon)
            .setColor(ContextCompat.getColor(this, R.color.button_red))
            .setTicker(getString(R.string.app_name)) //     .setPriority(Notification.PRIORITY_MAX)
            .setContentTitle(data["title"].toString())
            .setContentText(data["body"].toString())
            .setSound(soundUri)
            .setContentInfo(data["type"].toString())
            .setContentIntent(pendingIntent)

        notificationManager.notify(1, notificationBuilder.build())


//        var args = Bundle()
//        args.putString("arg", "hello")
//        val deeplink = Navigation.findNavController(R.id.nav_tab_main).createDeepLink()
//            .setDestination(R.id.homeFragment)
//            .setArguments(args)
//            .createPendingIntent()
//        val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationManager.createNotificationChannel(NotificationChannel(
//                channelId, "Deep Links", NotificationManager.IMPORTANCE_HIGH))
//        }
//        val builder = NotificationCompat.Builder(
//            context!!, channelId)
//            .setContentTitle("Navigation")
//            .setContentText("Deep link to Android")
//            .setSmallIcon(R.drawable.ic_menu_camera)
//            .setContentIntent(deeplink)
//            .setAutoCancel(true)
//        notificationManager.notify(0, builder.build())

//        val pendingIntent = NavDeepLinkBuilder(context)
//            .setComponentName(MainActivity::class.java)
//            .setGraph(R.navigation.nav_graph)
//            .setDestination(R.id.destination)
//            .setArguments(bundle)
//            .createPendingIntent()
//
//
//
//        notificationBuilder.setContentIntent(pendingIntent)

    }


    private fun getNotificationIcon(): Int {
        val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        return if (useWhiteIcon) R.drawable.ic_notification else R.drawable.ic_notification
    }

}