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
import com.example.kiosckoutback.Activity.CartActivity
import com.example.kiosckoutback.Activity.MainActivity


// 바인드 - > 데이터 오는것 class.add

class MyService : Service() {
    val SC = "myService"
    var noti : Notification? = null
    var notiManager: NotificationManager? = null
    lateinit var cartClass: CartClass
    var totalCal=""
    val iBinder = MyBinder()


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

    var type=""
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent!!.getStringExtra("test")!=null) {
            type = intent!!.getStringExtra("test")!!
        }
        Notification()
        if(intent!!.getStringExtra("stop")=="stop"){
            stopForegroundService()
        }
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onBind(intent: Intent): IBinder {
        if (intent.getSerializableExtra("DATA")as CartClass? !=null){
            cartClass=intent.getSerializableExtra("DATA")as CartClass
            totalCal=cartClass.totalCal().toString()
            Log.d("result",cartClass.totalCal().toString())
        }
        return iBinder
    }
    fun bindServiceReturn(): CartClass {
        return cartClass
    }
    private fun stopForegroundService() {
        stopForeground(true)
        stopSelf()
    }
}