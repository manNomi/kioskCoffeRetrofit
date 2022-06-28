package com.example.kiosckoutback

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.kiosckoutback.Activity.MainActivity

class MyService : Service() {
    val SC = "myService"
    var noti : Notification? = null
    var notiManager: NotificationManager? = null

    fun Notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intentMainLanding = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intentMainLanding, 0)

            if (notiManager == null) {
                notiManager =
                    this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            val builder = NotificationCompat.Builder(this, SC)

            builder.setContentTitle(
                StringBuilder(resources.getString(R.string.app_name)+" 총 금액은 : ${totalCal}")
                )
                .setTicker(
                    StringBuilder(("실행중"))
                )
                .setSmallIcon(R.mipmap.outback_logo)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setOngoing(true)

            noti = builder.build()
            startForeground(10, noti)
        }
    }


    inner class MyBinder : Binder() {
        fun getService(): MyService = this@MyService
    }

    lateinit var cartClass: CartClass

    var totalCal=""


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        cartClass = intent!!.getSerializableExtra("DATA") as CartClass
        totalCal=cartClass.totalCal().toString()
        Log.d("qwe",totalCal)
        Notification()
        return super.onStartCommand(intent, flags, startId)
    }


    val iBinder = MyBinder()
    override fun onBind(intent: Intent): IBinder {



        return iBinder
    }
}

//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.Service
//import android.content.Context
//import android.content.Intent
//import android.graphics.Color
//import android.os.Binder
//import android.os.Build
//import android.os.IBinder
//import android.util.Log
//import androidx.core.app.NotificationCompat
//import com.example.kiosckoutback.R
//
//class MyService : Service() {
//    lateinit var notificationManager: NotificationManager
//    lateinit var builder : NotificationCompat.Builder
//    companion object {
//        const val NOTIFICATION_ID = 10
//        const val CHANNEL_ID = "primary_notification_channel"
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        noti()
//        return super.onStartCommand(intent, flags, startId)
//    }
//
//    fun noti(){
//        builder = NotificationCompat.Builder(this, "channel")
//        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createNotificationChannel()
//            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("장바구니")
//                .setSmallIcon(R.mipmap.outback_logo)
//                .setContentText("총 ${123}개  ${456}원입니다.")
//                .build()
//            Log.d("Test", "start foreground")
//            startForeground(NOTIFICATION_ID, notification)
//        }
//    }
//
//    private fun createNotificationChannel() {
//        val notificationChannel = NotificationChannel(
//            CHANNEL_ID,
//            "MyApp notification",
//            NotificationManager.IMPORTANCE_HIGH
//        )
//        notificationChannel.enableLights(true)
//        notificationChannel.lightColor = Color.RED
//        notificationChannel.enableVibration(true)
//        notificationChannel.description = "AppApp Tests"
//
//        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(
//            notificationChannel)
//    }
//
//    override fun onBind(p0: Intent?): IBinder? {
//        return null
//    }
//
//    inner class MyBinder : Binder()  {
//        fun getService() : MyService {
//            return this@MyService
//        }
//    }
//
//}