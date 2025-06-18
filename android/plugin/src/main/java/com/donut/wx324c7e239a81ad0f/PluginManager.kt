package com.donut.wx324c7e239a81ad0f

import com.tencent.luggage.wxa.SaaA.plugin.NativePluginMainProcessTask
import kotlinx.android.parcel.Parcelize

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.nfc.Tag
import android.os.Build
import android.os.Parcel
import android.util.Log
import cn.jpush.android.api.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.ilink.dev.ota.IlinkOtaCallbackInterface
import com.tencent.luggage.wxa.SaaA.plugin.AsyncJsApi
import com.tencent.luggage.wxa.SaaA.plugin.NativePluginBase
import com.tencent.luggage.wxa.SaaA.plugin.NativePluginInterface
import com.tencent.luggage.wxa.SaaA.plugin.SyncJsApi
import org.json.JSONObject
import java.lang.reflect.Type

class MsgReceiver(private val nativePlugin: TestNativePlugin) : BroadcastReceiver() {
    private var TAG = "MessageReceiverManager.MsgReceiver"

    val clickedJsonStrs = mutableListOf<String>()
    val showedJsonStrs = mutableListOf<String>()

    override fun onReceive(context: Context?, intent: Intent) {
        Log.d(TAG, "onReceive called with action: ${intent.action}")
        
        val event = intent.getStringExtra(JConstants.EVENT_NAME)
        val dataStr = intent.getStringExtra(JConstants.EVENT_DATA)
        Log.d(TAG, "onReceive - event: $event, dataStr: $dataStr")
        
        if (!dataStr.isNullOrEmpty()) {
            try {
                val jsonObject = JSONObject(dataStr)
                val map = HashMap<String, Any>()
                jsonObject.keys().forEach { key ->
                    map[key] = jsonObject.get(key)
                }
                Log.d(TAG, "Sending event to plugin: $event, data: $map")
                this.nativePlugin.sendMiniPluginEventOut(map)
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing JSON data: ${e.message}")
            }
        } else {
            Log.w(TAG, "dataStr is null or empty")
        }
    }
}


@Parcelize
class TestTask(private var valToSync1: String, private var valToSync2: String) :
    NativePluginMainProcessTask() {
    private var clientCallback: ((Any) -> Unit)? = null
    fun setClientCallback(callback: (data: Any) -> Unit) {
        this.clientCallback = callback
    }
    /**
     * 运行在主进程的逻辑，不建议在主进程进行耗时太长的操作
     */
    override fun runInMainProcess() {
        android.util.Log.e("MainProcess", "runInMainProcess, valToSync1:${valToSync1}, valToSync2:${valToSync2}")
        // 如果需要把主进程的数据回调到小程序进程，就赋值后调用 callback 函数

        if (valToSync1.equals("setupJPush")) {
            Log.d("MainProcess", "MainProcess - setupJPush ${TestNativePluginApplication.applicationContext}");
            JPushMethod.init()
        } else if (valToSync1.equals("addTags")) {
            val jsonObject = JSONObject(valToSync2)
            Log.d("MainProcess", "MainProcess - addTags ${TestNativePluginApplication.applicationContext}");
            JPushMethod.addTags(jsonObject)
        } else if (valToSync1.equals("setTags")) {
            val jsonObject = JSONObject(valToSync2)
            Log.d("MainProcess", "MainProcess - setTags ${TestNativePluginApplication.applicationContext}");
            JPushMethod.setTags(jsonObject)
        } else if (valToSync1.equals("getAllTags")) {
            val jsonObject = if (valToSync2.isNotEmpty()) JSONObject(valToSync2) else null
            Log.d("MainProcess", "MainProcess - getAllTags ${TestNativePluginApplication.applicationContext}");
            JPushMethod.getAllTags(jsonObject)
        } else if (valToSync1.equals("checkTagBindState")) {
            val jsonObject = JSONObject(valToSync2)
            Log.d("MainProcess", "MainProcess - checkTagBindState ${TestNativePluginApplication.applicationContext}");
            JPushMethod.checkTagBindState(jsonObject)
        } else if (valToSync1.equals("deleteTags")) {
            val jsonObject = JSONObject(valToSync2)
            Log.d("MainProcess", "MainProcess - deleteTags ${TestNativePluginApplication.applicationContext}");
            JPushMethod.deleteTags(jsonObject)
        } else if (valToSync1.equals("setAlias")) {
            val jsonObject = JSONObject(valToSync2)
            Log.d("MainProcess", "MainProcess - setAlias ${TestNativePluginApplication.applicationContext}");
            JPushMethod.setAlias(jsonObject)
        } else if (valToSync1.equals("deleteAlias")) {
            val jsonObject = if (valToSync2.isNotEmpty()) JSONObject(valToSync2) else null
            Log.d("MainProcess", "MainProcess - deleteAlias ${TestNativePluginApplication.applicationContext}");
            JPushMethod.deleteAlias(jsonObject)
        } else if (valToSync1.equals("getAlias")) {
            val jsonObject = if (valToSync2.isNotEmpty()) JSONObject(valToSync2) else null
            Log.d("MainProcess", "MainProcess - getAlias ${TestNativePluginApplication.applicationContext}");
            JPushMethod.getAlias(jsonObject)
        }

        this.callback() // callback函数会同步主进程的task数据，并在子进程调用runInClientProcess
    }
    /**
     * 运行在小程序进程的逻辑
     */
    override fun runInClientProcess() {
        android.util.Log.e("ClientProcess", "valToSync1: ${valToSync1}, valToSync2:${valToSync2}")
        this.clientCallback?.let { callback ->
           callback(valToSync1)
        }
    }
    override fun parseFromParcel(mainProcessData: Parcel?) {
        // 如果需要获得主进程数据，需要重写parseFromParcel，手动解析Parcel
        this.valToSync1 = mainProcessData?.readString() ?: ""
        this.valToSync2 = mainProcessData?.readString() ?: ""
    }
}

class JPushMethod {
    companion object {
        var plugin: TestNativePlugin? = null

        fun init() {
            JPushInterface.init(TestNativePluginApplication.applicationContext)
        }
        fun addTags(data: JSONObject?) {
            Log.d("JPushMethod", "JPushMethod - addTags ${TestNativePluginApplication.applicationContext}");
            val tags = data?.getJSONArray("tags") ?: run {
//            callback(createErrorResult("tags is required"))
                Log.d("JPushMethod", "tags is required - addTags ${TestNativePluginApplication.applicationContext}");
                return
            }
            val tagSet = mutableSetOf<String>()
            for (i in 0 until tags.length()) {
                tagSet.add(tags.getString(i))
            }
            JPushInterface.addTags(TestNativePluginApplication.applicationContext, 0, tagSet)
        }

        fun setTags(data: JSONObject?) {
            val tags = data?.getJSONArray("tags") ?: run {
//                callback(createErrorResult("tags is required"))
                return
            }
            val tagSet = mutableSetOf<String>()
            for (i in 0 until tags.length()) {
                tagSet.add(tags.getString(i))
            }
            JPushInterface.setTags(TestNativePluginApplication.applicationContext, 0, tagSet)

        }

        fun getAllTags(data: JSONObject?) {
            JPushInterface.getAllTags(TestNativePluginApplication.applicationContext, 0)
        }

        fun checkTagBindState(data: JSONObject?) {
            val tag = data?.getString("tag") ?: run {
//                callback(createErrorResult("tag is required"))
                return
            }

            JPushInterface.checkTagBindState(TestNativePluginApplication.applicationContext, 0, tag);
        }

        fun deleteTags(data: JSONObject?) {
            val tags = data?.getJSONArray("tags") ?: run {
//                callback(createErrorResult("tags is required"))
                return
            }
            val tagSet = mutableSetOf<String>()
            for (i in 0 until tags.length()) {
                tagSet.add(tags.getString(i))
            }
            JPushInterface.deleteTags(TestNativePluginApplication.applicationContext, 0, tagSet)
        }

        fun setAlias(data: JSONObject?) {
            val alias = data?.getString("alias") ?: run {
//                callback(createErrorResult("alias is required"))
                return
            }
            JPushInterface.setAlias(TestNativePluginApplication.applicationContext, 0, alias)
        }

        fun deleteAlias(data: JSONObject?) {
            JPushInterface.deleteAlias(TestNativePluginApplication.applicationContext,0)
        }

        fun getAlias(data: JSONObject?) {
            JPushInterface.getAlias(TestNativePluginApplication.applicationContext, 0);
        }
    }
}

class TestNativePlugin: NativePluginBase(), NativePluginInterface {
    private val TAG = "JPushPlugin"

    var msgReceiver: MsgReceiver = MsgReceiver(this)

//    companion object {
//        private var instance: TestNativePlugin? = null
//
//        fun getInstance(): TestNativePlugin {
//            if (instance == null) {
//                instance = TestNativePlugin()
//            }
//            return instance!!
//        }
//
//        fun sendPluginEvent(eventName: String, data: HashMap<String, Any>) {
//            val eventData = HashMap<String, Any>()
//            eventData["eventName"] = eventName
//            eventData["event_data"] = data
////            getInstance().sendMiniPluginEvent(eventData)
//            if (JPushMethod.plugin != null) {
//                JPushMethod.plugin?.sendMiniPluginEvent(eventData)
//            }else {
//                Log.d("snsn","no JPushMethod.plugin ");
//            }
//
////            val testTask = TestTask("sendMiniPluginEvent", "")
////            testTask.runInClientProcess()
//        }
//
//        fun sendEvent(eventName: String) {
//
//            val notificationData = HashMap<String, Any>()
//        notificationData["title"] = eventName
//        notificationData["content"] = "You have a new message"
//
//            val eventData = HashMap<String, Any>()
//            eventData["eventName"] = eventName
//            eventData.putAll(notificationData)
//            getInstance().sendMiniPluginEvent(eventData)
//        }
//    }

    override fun getPluginID(): String {
        Log.e(TAG, "getPluginID")
        return BuildConfig.PLUGIN_ID
    }

    fun  sendMiniPluginEventOut(msg:HashMap<String, Any>) {
        Log.e(TAG, "sendMiniPluginEventOut")
        this.sendMiniPluginEvent(msg)
    }

    // 同步方法
    @SyncJsApi(methodName = "setupJPush")
    fun setupJPush(data: JSONObject?, activity: Activity): String {
        JPushMethod.plugin = this
        Log.d("snsnsn - plugin", "$JPushMethod.plugin");
        registerMsgReceiver(activity)
        val testTask = TestTask("setupJPush", "")
        testTask.execAsync()
        return createSuccessResult()
    }

    private fun registerMsgReceiver(context: Context) {
        try {
            val intentFilter = android.content.IntentFilter()
            intentFilter.addAction(JConstants.INTENT_ACTION_NAME)
            context.registerReceiver(msgReceiver, intentFilter)
            Log.d(TAG, "MsgReceiver registered successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register MsgReceiver: ${e.message}")
        }
    }

    private fun unregisterMsgReceiver(context: Context) {
        try {
            context.unregisterReceiver(msgReceiver)
            Log.d(TAG, "MsgReceiver unregistered successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to unregister MsgReceiver: ${e.message}")
        }
    }


    @SyncJsApi(methodName = "setBadge")
    fun setBadge(data: JSONObject?, activity: Activity): String {
        val badge = data?.getInt("badge") ?: return createErrorResult("badge is required")
        JPushInterface.setBadgeNumber(activity, badge)
        return createSuccessResult()
    }

    @SyncJsApi(methodName = "setLogLevel")
    fun setLogLevel(data: JSONObject?, activity: Activity): String {
        val enable = data?.getBoolean("enable") ?: return createErrorResult("enable is required")
        JPushInterface.setDebugMode(enable)
        return createSuccessResult()
    }

    @SyncJsApi(methodName = "setSmartPushEnable")
    fun setSmartPushEnable(data: JSONObject?, activity: Activity): String {
        val enable = data?.getBoolean("enable") ?: return createErrorResult("enable is required")
        JPushInterface.setSmartPushEnable(activity,enable)
        return createSuccessResult()
    }

    @SyncJsApi(methodName = "pageEnterTo")
    fun pageEnterTo(data: JSONObject?, activity: Activity): String {
        JPushInterface.onResume(activity)
        return createSuccessResult()
    }

    @SyncJsApi(methodName = "pageLeave")
    fun pageLeave(data: JSONObject?, activity: Activity): String {
        JPushInterface.onPause(activity)
        return createSuccessResult()
    }

    @SyncJsApi(methodName = "addTags")
    fun addTags(data: JSONObject?, activity: Activity) {
        val jsonString = data.toString()
        val testTask = TestTask("addTags", jsonString)
        testTask.execAsync()
    }

    @SyncJsApi(methodName = "setTags")
    fun setTags(data: JSONObject?, activity: Activity) {
        val jsonString = data.toString()
        val testTask = TestTask("setTags", jsonString)
        testTask.execAsync()
    }

    @SyncJsApi(methodName = "getAllTags")
    fun getAllTags(data: JSONObject?, activity: Activity) {
        val jsonString = data.toString()
        val testTask = TestTask("getAllTags", jsonString)
        testTask.execAsync()
    }

    @SyncJsApi(methodName = "checkTagBindState")
    fun checkTagBindState(data: JSONObject?, activity: Activity) {
        val jsonString = data.toString()
        val testTask = TestTask("checkTagBindState", jsonString)
        testTask.execAsync()
    }

    @SyncJsApi(methodName = "deleteTags")
    fun deleteTags(data: JSONObject?, activity: Activity) {
        val jsonString = data.toString()
        val testTask = TestTask("deleteTags", jsonString)
        testTask.execAsync()
    }

    @SyncJsApi(methodName = "setAlias")
    fun setAlias(data: JSONObject?, activity: Activity) {
        val jsonString = data.toString()
        val testTask = TestTask("setAlias", jsonString)
        testTask.execAsync()
    }

    @SyncJsApi(methodName = "deleteAlias")
    fun deleteAlias(data: JSONObject?, activity: Activity) {
        val jsonString = data.toString()
        val testTask = TestTask("deleteAlias", jsonString)
        testTask.execAsync()
    }

    @SyncJsApi(methodName = "getAlias")
    fun getAlias(data: JSONObject?, activity: Activity) {
        val jsonString = data.toString()
        val testTask = TestTask("getAlias", jsonString)
        testTask.execAsync()
    }

    @AsyncJsApi(methodName = "getRegistrationID")
    fun getRegistrationID(data: JSONObject?, callback: (data: Any) -> Unit, activity: Activity) {
        val registrationID = JPushInterface.getRegistrationID(activity)
        callback(JSONObject().apply {
            put("code", 0)
            put("registrationID", registrationID)
        })
    }

    @AsyncJsApi(methodName = "setPushSwitch")
    fun setPushSwitch(data: JSONObject?, callback: (data: Any) -> Unit, activity: Activity) {
        val enable = data?.getBoolean("enable") ?: run {
            callback(createErrorResult("enable is required"))
            return
        }
        
        JPushInterface.stopPush(activity)
        if (enable) {
            JPushInterface.resumePush(activity)
        }
        callback(createSuccessResult())
    }

    @AsyncJsApi(methodName = "checkNotificationAuthorization")
    fun checkNotificationAuthorization(data: JSONObject?, callback: (data: Any) -> Unit, activity: Activity) {
        val granted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
        callback(JSONObject().apply {
            put("code", 0)
            put("granted", granted)
        })
    }

    @AsyncJsApi(methodName = "openSettings")
    fun openSettings(data: JSONObject?, callback: (data: Any) -> Unit, activity: Activity) {
        JPushInterface.goToAppNotificationSettings(activity)
        if (callback == null) {
            return
        }
        callback(createSuccessResult())
    }


    // 工具方法
    private fun createSuccessResult(): String {
        return JSONObject().apply {
            put("code", 0)
            put("message", "success")
        }.toString()
    }

    private fun createErrorResult(message: String): String {
        return JSONObject().apply {
            put("code", -1)
            put("message", message)
        }.toString()
    }

    @SyncJsApi(methodName = "mySyncFunc")
    fun test(data: JSONObject?, activity: Activity): String {
        Log.i(TAG, data.toString())

        val componentName = activity.componentName.toString()
        Log.i(TAG, componentName)

        JPushInterface.setDebugMode(true);

        return "test"
    }

    @AsyncJsApi(methodName = "myAsyncFuncwithCallback")
    fun testAsync(data: JSONObject?, callback: (data: Any) -> Unit, activity: Activity) {
        Log.i(TAG, data.toString())

        // 有需要的时候向 js 发送消息
        val values1 = HashMap<String, Any>()
        values1["status"] = "testAsync start"
        this.sendMiniPluginEvent(values1)

        callback("async testAsync")
    }
}