package com.example.kiosckoutback

import android.app.*
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.kiosckoutback.Activity.MainActivity

class MyService : Service() {
    val SC = "myService"
    var noti : Notification? = null
    var notiManager: NotificationManager? = null

    fun Notification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val intentMainLanding = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intentMainLanding, 0)
            val nc = NotificationChannel(
                SC,
                "My Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val nm = getSystemService(NotificationManager::class.java)

            nm.createNotificationChannel(nc)

            val builder = NotificationCompat.Builder(this, "service_channel")

            builder.setContentTitle(
                StringBuilder(resources.getString(R.string.app_name)).append(
                    " 총 금액은 : ${1230}"
                ).toString()
            )
                .setTicker(
                    StringBuilder(resources.getString(R.string.app_name)).append("service is running")
                        .toString()
                )
                .setContentText("Touch to open") //
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setWhen(0)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent) //누를시 이동할 액티비티
                .setOngoing(true)

            noti = builder.build()
            startForeground(1, noti)
        }
    }

    inner class MyBinder : Binder() {
        fun getService(): MyService = this@MyService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Notification()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }
}