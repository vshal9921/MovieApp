package com.kinected.myapplication.common

import android.content.Context
import com.kinected.myapplication.BuildConfig
import com.kinected.myapplication.data.ApiConst
import com.kinected.myapplication.data.MovieDao
import com.kinected.myapplication.data.LocalDb
import com.kinected.myapplication.data.MovieRepo
import com.kinected.myapplication.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit{

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply{
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Adding header in API
        val httpClient = OkHttpClient().newBuilder().apply {
            addInterceptor(httpLoggingInterceptor)

            addInterceptor { chain ->
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder()
                    .addHeader(ApiConst.AUTHORIZATION, "Bearer " + BuildConfig.TMDB_API_KEY)
                chain.proceed(requestBuilder.build())
            }
        }

        httpClient.apply {
            readTimeout(60, TimeUnit.SECONDS)
        }

        return Retrofit.Builder()
                .baseUrl(ApiConst.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesCountryRepo(apiService: ApiService, movieDao: MovieDao): MovieRepo{
        return MovieRepo(apiService, movieDao)
    }

    // Room Database
    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): LocalDb{
        return LocalDb.getInstance(context)
    }

    @Provides
    @Singleton
    fun providesMovieDao(localDb: LocalDb): MovieDao {
        return localDb.movieDao()
    }
}