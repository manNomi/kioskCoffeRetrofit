package com.example.kiosckoutback

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.kiosckoutback.Activity.CartActivity
import com.example.kiosckoutback.Activity.MainActivity
import com.example.kiosckoutback.dataBase.CartClass


// 바인드 - > 데이터 오는것 class.add

class MyService : Service() {
    val SC = "myService"
    var noti : Notification? = null
    var notiManager: NotificationManager? = null
    lateinit var cartClass: CartClass
    var totalCal=""
    val iBinder = MyBinder()
    var type=""

//    노티 만들기
//    알림창 연결하는 부분을 다르게 만든다
//    패딩인텐트로 가는 액티비티를 다르게 만들고
//    매니페스트에는 런치모드를 싱글태스크로 이용해 백그라운드 계속 삭제 방식
fun Notification() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        var intentMainLanding:Intent
        if (type=="main") {
            intentMainLanding = Intent(this, MainActivity::class.java)
        }
        else{
            intentMainLanding = Intent(this, CartActivity::class.java)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intentMainLanding, 0)
        createNotificationChannel()
        if (notiManager == null) {
            notiManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
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
        Log.d("result","빌더 ${builder} 노티 ${noti} 노티 ${notiManager}")
        startForeground(10, noti)
    }
}

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            Companion.CHANNEL_ID,
            "MyApp notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "AppApp Tests"

        val notificationManager = applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            notificationChannel)
    }

//    바인드 서비스
    inner class MyBinder : Binder() {
        fun getService(): MyService = this@MyService
    }

//    포그라운드 서비스 시작시 실행되는곳
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent!!.getStringExtra("type")!=null) {
            type = intent!!.getStringExtra("type")!!
        }
        Notification()
        if(intent!!.getStringExtra("stop")=="stop"){
            stopForegroundService()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    var id=""
//    바인드 서비스 연결시 시작되는 곳
//    연속으로 연결해도 되지만 값이 들어오지 않는 문제 생기므로 언바인드 필요
    override fun onBind(intent: Intent): IBinder {
        if (intent.getSerializableExtra("DATA")as CartClass? !=null){
            cartClass=intent.getSerializableExtra("DATA")as CartClass
            totalCal=cartClass.totalCal().toString()
            Log.d("result",cartClass.totalCal().toString())
        }
        id=intent.getStringExtra("id")!!
        return iBinder
    }
    fun bindServiceReturn(): Array<Any>{
        return (arrayOf(cartClass,id))
    }

//    포그라운드 서비스 멈춤
    private fun stopForegroundService() {
        stopForeground(true)
        stopSelf()
    }

    companion object {
        const val CHANNEL_ID = "primary_notification_channel"
    }
}