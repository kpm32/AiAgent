package com.example.aiagent.di

import com.example.aiagent.data.mapper.LLMMapper
import com.example.aiagent.data.network.LmStudioApi
import com.example.aiagent.data.repository_impl.LlmClientRepositoryImpl
import com.example.aiagent.domain.lm_repository.LlmClientRepository
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
        lmStudioApi: LmStudioApi,
        llmMapper: LLMMapper
    ): LlmClientRepository =
        LlmClientRepositoryImpl(
            lmStudioApi = lmStudioApi,
            mapper = llmMapper
        )
}