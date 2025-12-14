package com.sako.foodics_android_task.utils.ext

import android.util.Log

fun Any?.logd(tag:String = "TESTLOG",prefix:String=""){
    Log.d(tag,"$prefix $this")
}

fun Any?.loge(tag:String = "TESTLOG",prefix:String=""){
    Log.e(tag,"$prefix $this")
}