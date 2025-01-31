package com.example.marsphotos.data

import com.example.marsphotos.network.MarsApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindMarsPhotosRepository(
        networkMarsPhotosRepository: NetworkMarsPhotosRepository
    ): MarsPhotosRepository

    companion object {
        @Provides
        @Singleton
        fun provideApiService(): MarsApiService {
            return Retrofit.Builder()
                .baseUrl("https://android-kotlin-fun-mars-server.appspot.com")
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(MarsApiService::class.java)
        }
    }
}

