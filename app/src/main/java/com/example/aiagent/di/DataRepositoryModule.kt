package com.example.aiagent.di

import com.example.aiagent.BuildConfig
import com.example.aiagent.data.network.LmStudioApi
import com.example.aiagent.data.repository_impl.LlmClientRepositoryImpl
import com.example.aiagent.domain.lm_repository.LlmClientRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import jakarta.inject.Singleton
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataRepositoryModule {

    @Provides
    @Singleton
    fun provideLlmRepository(
        api: LmStudioApi,
        moshi: Moshi
    ): LlmClientRepository =
        LlmClientRepositoryImpl(
            api = api,
            model = BuildConfig.LM_STUDIO_MODEL.trim(),
            moshi = moshi
        )
}