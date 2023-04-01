package com.getstream.di

import android.app.Application
import com.getstream.data.DataStoreRepository
import com.getstream.data.local.DataStoreRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun providesDataStoreRepository(context: Application): DataStoreRepository {
        return DataStoreRepositoryImpl(context)
    }

}

