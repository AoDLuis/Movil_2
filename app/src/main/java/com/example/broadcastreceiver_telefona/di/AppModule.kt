package com.example.broadcastreceiver_telefona.di

import android.app.Application
import android.content.Context
import com.example.broadcastreceiver_telefona.Data.AutoReplyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
public class AppModule {

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
    @Provides
    @Singleton
    fun provideAutoReplyRepository(@ApplicationContext context: Context): AutoReplyRepository {
        return AutoReplyRepository(context)
    }
}
