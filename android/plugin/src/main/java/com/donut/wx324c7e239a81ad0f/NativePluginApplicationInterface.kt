package com.donut.wx324c7e239a81ad0f

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.tencent.luggage.wxa.SaaA.plugin.NativePluginApplicationInterface


class TestNativePluginApplication: NativePluginApplicationInterface {
    private var TAG = "TestNativePluginApplication"

    companion object {
        var applicationContext: Context? = null
    }

    override fun getPluginID(): String {
        android.util.Log.e(TAG, "getPluginID")
        return BuildConfig.PLUGIN_ID
    }

    override fun onCreate(application: Application) {
        android.util.Log.e(TAG, "oncreate!")
        applicationContext = application.applicationContext
        application.registerActivityLifecycleCallbacks(object: Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                android.util.Log.e(TAG, "onActivityCreated")
            }

            override fun onActivityStarted(p0: Activity) {
                android.util.Log.e(TAG, "onActivityStarted")
            }

            override fun onActivityResumed(p0: Activity) {
                android.util.Log.e(TAG, "onActivityResumed")
            }

            override fun onActivityPaused(p0: Activity) {
                android.util.Log.e(TAG, "onActivityPaused")
            }

            override fun onActivityStopped(p0: Activity) {
                android.util.Log.e(TAG, "onActivityStopped")
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
                android.util.Log.e(TAG, "onActivitySaveInstanceState")
            }

            override fun onActivityDestroyed(p0: Activity) {
                android.util.Log.e(TAG, "onActivityDestroyed")
            }
        })
    }

}
