package com.example.aiagent.di

import com.example.aiagent.domain.lm_repository.LlmClientRepository
import com.example.aiagent.domain.use_case.AgentUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainRepositoryModule {

    @Provides
    fun provideAgentUseCase(repository: LlmClientRepository): AgentUseCase {
        return AgentUseCase(repository)
    }

}