package com.example.moviebrowserapp.network

import com.example.moviebrowserapp.hilt.AppModule
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton


object Network {

    val baseUrl = "https://api.themoviedb.org/3/"
    val token =
        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiMGMxMzcxNjhlNTExZTk2NjZmNjYwMmJiMmQzNmUwOSIsIm5iZiI6MTc1NDQ5MDQ2NS41MjQ5OTk5LCJzdWIiOiI2ODkzNjY2MTUzYWIzYzJjOTNkYmZjZDYiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.KKUdyrqti7SS5L-EB2xfbmEOPH1PJR49GPKFyMoxFAo"


//    inline fun<reified T> mixRetrofitAndOkhttps(retrofit: Retrofit = AppModule.retrofit()):T{
//
//        return retrofit.create(T::class.java)
//    }


    fun provideLogIntercept( ): HttpLoggingInterceptor{
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}