package com.app.hiltplayground.di

import com.app.hiltplayground.apiSource.BlogRetrofit
import com.app.hiltplayground.apiSource.NetworkMapper
import com.app.hiltplayground.localStorage.BlogDao
import com.app.hiltplayground.localStorage.CacheMapper
import com.app.hiltplayground.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun providesMainRepository(
        blogDao: BlogDao,
        retrofit: BlogRetrofit,
        cacheMapper: CacheMapper,
        networkMapper: NetworkMapper
    ): Repository{
        return Repository(blogDao, retrofit, cacheMapper, networkMapper)
    }
}