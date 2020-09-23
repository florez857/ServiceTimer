package com.developergunda.timerlifecycleservice.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.developergunda.timerlifecycleservice.MainActivity
import com.developergunda.timerlifecycleservice.R
import com.developergunda.timerlifecycleservice.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped


@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ) = NotificationManagerCompat.from(context)

    @ServiceScoped
    @Provides
    fun provideNotificationBuilde(
        @ApplicationContext context: Context,
        pendingIntent: PendingIntent
    ) =
        NotificationCompat.Builder(context, Constant.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)//mantiene la notificacion arriba de todas las notificaciones
            .setSmallIcon(R.drawable.twotone_alarm_black_24)
            .setContentTitle("Servicio de timer ejecutando")
            .setContentText("00:00:00:00")
            .setContentIntent(pendingIntent)

    @ServiceScoped
    @Provides
    fun providePendingIntent(@ApplicationContext context: Context) = PendingIntent.getActivity(
        context,
        143,
        Intent(context, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )

}