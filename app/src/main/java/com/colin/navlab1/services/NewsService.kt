package com.colin.navlab1.services

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import com.colin.navlab1.R
import com.colin.navlab1.activities.MainActivity
import com.colin.navlab1.common.Coroutines
import com.colin.navlab1.mvvm.NewsViewModel
import com.colin.navlab1.repositories.NewsRepository
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.coroutines.Job
import kotlin.random.Random

class NewsService : Service() {
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntentService: PendingIntent
    lateinit var manager: NotificationManager
    override fun onCreate() {
        super.onCreate()
        Log.d("SERVICE", "CREATED")

        val serviceChannel = NotificationChannel(
            1.toString(),
            "Foreground Service Channel",
            NotificationManager.IMPORTANCE_LOW
        )

        manager = getSystemService(NotificationManager::class.java)

        manager.createNotificationChannel(serviceChannel)


    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE)
            }

        val notification: Notification = Notification.Builder(this, "1")
            .setContentTitle("News service running")
            .setContentText("Looking for news")
            .setSmallIcon(R.drawable.heart)
            .setContentIntent(pendingIntent)
            .setTicker("111")
            .build()
        startForeground(1, notification)
        val intentService = Intent(this, UpdateReciever::class.java)

        pendingIntentService = PendingIntent.getBroadcast(this, 1, intentService,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        Coroutines.ioThenMain(
            { NewsRepository.getNews() }, { NewsRepository.setNews(it!!)})

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            1000,
            pendingIntentService
        )
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmManager.cancel(pendingIntentService)
        stopForeground(false)
        stopSelf()
        Log.d("SERVICE", "STOPPED")
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}

class UpdateReciever: BroadcastReceiver(), SensorEventListener {
    private lateinit var job: Job
    private lateinit var mSensorManager: SensorManager
    private lateinit var contxt: Context

    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("SERVICE", "GOt_BROADCAST")
        contxt = p0!!
        mSensorManager = p0.getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (sensor != null) {
            mSensorManager.registerListener(this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(p0, "No accelerometer", Toast.LENGTH_LONG).show()
        }


        NewsRepository.setLoading()
        job = Coroutines.ioThenMain(
            { NewsRepository.getNews() },
            {
                NewsRepository.setNews(it!!)
                val notification: Notification = Notification.Builder(p0, "1")
                    .setContentTitle("News")
                    .setContentText("Update!")
                    .setSmallIcon(R.drawable.heart)
                    .setTicker("upd")
                    .build()
                val manager = getSystemService(p0, NotificationManager::class.java)
                manager?.notify(Random.nextInt(), notification)
                mSensorManager.unregisterListener(this)
            }
        )
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        val accelerometerData = p0!!.values[0]
        Toast.makeText(contxt, accelerometerData.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

}