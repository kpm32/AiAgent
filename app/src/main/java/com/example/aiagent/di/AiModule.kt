package com.example.aiagent.di

import com.example.aiagent.BuildConfig
import com.example.aiagent.data.LmStudioApi
import com.example.aiagent.data.LmStudioLlmClient
import com.example.aiagent.domain.lm_repository.LlmClientRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object AiModule {

    @Provides
    @Singleton
    fun moshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides @Singleton
    fun lmOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.MINUTES)
        .writeTimeout(10, TimeUnit.MINUTES)
        .callTimeout(0, TimeUnit.SECONDS) // 0 = без общего лимита
        .retryOnConnectionFailure(true)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .build()


    @Provides @Singleton
    fun lmApi(m: Moshi, c: OkHttpClient): LmStudioApi =
        Retrofit.Builder()
            .baseUrl(BuildConfig.LM_STUDIO_BASE_URL.trim())
            .client(c)
            .addConverterFactory(MoshiConverterFactory.create(m))
            .build()
            .create(LmStudioApi::class.java)


    @Provides @Singleton
    fun llmClient(api: LmStudioApi, m: Moshi): LlmClientRepository =
        LmStudioLlmClient(api, BuildConfig.LM_STUDIO_MODEL.trim(), m)
}