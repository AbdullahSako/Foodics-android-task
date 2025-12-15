package com.sako.foodics_android_task.utils.resultWrapper

sealed class Result<T>(val data:T? = null,val errorType: NetworkError? = null){
    class Loading<T>: Result<T>()
    class Success<T>(data:T):Result<T>(data = data)
    class Error<T>(errorType: NetworkError): Result<T>(errorType =errorType )
}