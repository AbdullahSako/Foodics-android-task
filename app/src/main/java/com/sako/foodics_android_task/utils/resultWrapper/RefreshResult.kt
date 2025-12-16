package com.sako.foodics_android_task.utils.resultWrapper

sealed class RefreshResult( val errorType: NetworkError? = null){

    class Success(): RefreshResult()
    class Error(errorType: NetworkError): RefreshResult(errorType =errorType )
}