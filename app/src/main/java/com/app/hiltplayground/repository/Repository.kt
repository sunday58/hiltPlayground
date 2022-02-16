package com.app.hiltplayground.repository

import com.app.hiltplayground.apiSource.BlogRetrofit
import com.app.hiltplayground.apiSource.NetworkMapper
import com.app.hiltplayground.localStorage.BlogDao
import com.app.hiltplayground.localStorage.CacheMapper
import com.app.hiltplayground.model.Blog
import com.app.hiltplayground.utils.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception



class Repository
    constructor(
        private val blogDao: BlogDao,
        private val blogRetrofit: BlogRetrofit,
        private val cacheMapper: CacheMapper,
        private val networkMapper: NetworkMapper
    )
{
        suspend fun getBlog(): Flow<DataState<List<Blog>>> = flow {
            emit(DataState.Loading)
            delay(timeMillis = 1000)
            try {
                val  networkBlogs = blogRetrofit.get()
                val blogs = networkMapper.mapFromEntityList(networkBlogs)
                    for (blog in blogs){
                       blogDao.insert(cacheMapper.mapToEntity(blog))
                    }
                val cacheBlogs = blogDao.get()
                emit(DataState.success(cacheMapper.mapFromEntityList(cacheBlogs)))
            }catch (e: Exception){
                emit(DataState.Error(e))
            }
        }
    }
