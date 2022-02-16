package com.app.hiltplayground.utils

import java.lang.Exception

sealed class DataState<out R> {
    data class success<out T>(val data: T): DataState<T>()
    data class Error(val exception: Exception): DataState<Nothing>()
    object Loading: DataState<Nothing>()
}