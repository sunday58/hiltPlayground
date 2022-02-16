package com.app.hiltplayground.repository

import com.app.hiltplayground.apiSource.BlogRetrofit
import com.app.hiltplayground.apiSource.NetworkMapper
import com.app.hiltplayground.localStorage.BlogDao
import com.app.hiltplayground.localStorage.CacheMapper
import com.app.hiltplayground.model.Blog
import com.app.hiltplayground.utils.DataState
import com.skydoves.sandwich.*
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
            DataState.Loading
            delay(timeMillis = 1000)
            val response = blogRetrofit.get()
            response.suspendOnSuccess {
                val  networkBlogs = blogRetrofit.get()
                val blogs = networkMapper.mapFromEntityList(data)
                for (blog in blogs){
                    blogDao.insert(cacheMapper.mapToEntity(blog))
                }
                val cacheBlogs = blogDao.get()
                emit(DataState.success(cacheMapper.mapFromEntityList(cacheBlogs)))
            }
            response.suspendOnError{
                when (statusCode){
                    StatusCode.Unauthorized -> emit(DataState.otherError("token time out"))
                    StatusCode.BadGateway -> emit(DataState.otherError("Something went wrong"))
                    StatusCode.GatewayTimeout -> emit(DataState.otherError("Unable to fetch data, please try again"))
                    else -> emit(DataState.otherError(message()))
                }
            }
            response.suspendOnException {
                emit(DataState.Error(exception))
            }


        }
    }
