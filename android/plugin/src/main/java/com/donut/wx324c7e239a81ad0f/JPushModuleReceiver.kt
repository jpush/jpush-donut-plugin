package com.donut.wx324c7e239a81ad0f

import android.content.Context;
import android.content.Intent
import android.util.Log

import cn.jpush.android.api.CmdMessage
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.api.JPushMessage
import cn.jpush.android.api.NotificationMessage
import cn.jpush.android.service.JPushMessageReceiver
import kotlinx.serialization.json.json
import org.json.JSONObject


class JPushModuleReceiver: JPushMessageReceiver() {
    override fun onMessage(context: Context?, customMessage: CustomMessage) {
        JLogger.d("onMessage:$customMessage")
        val jsonObject = JPushHelper.convertCustomMessage(customMessage)
        sendEvent(context,JConstants.CUSTOM_MESSAGE_EVENT, jsonObject)
    }

    override fun onNotifyMessageArrived(
        context: Context?,
        notificationMessage: NotificationMessage
    ) {
        JLogger.d("onNotifyMessageArrived:$notificationMessage")
        val jsonObject = JPushHelper.convertNotificationToMap(
            JConstants.NOTIFICATION_ARRIVED,
            notificationMessage
        )
        sendEvent(context,JConstants.NOTIFICATION_ARRIVED_EVENT,jsonObject)
    }


    override fun onNotifyMessageOpened(
        context: Context?,
        notificationMessage: NotificationMessage
    ) {
        JLogger.d("onNotifyMessageOpened:$notificationMessage")
//        if (!JPushModule.isAppForeground) JPushHelper.launchApp(context)
        val jsonObject = JPushHelper.convertNotificationToMap(
            JConstants.NOTIFICATION_OPENED,
            notificationMessage
        )
        sendEvent(context,JConstants.NOTIFICATION_CLICK_EVENT, jsonObject)
//        JPushHelper.saveOpenNotifiData(jsonObject, notificationMessage.notificationType)
    }

    override fun onNotifyMessageUnShow(
        context: Context?,
        notificationMessage: NotificationMessage
    ) {
        JLogger.d("onNotifyMessageUnShow:$notificationMessage")
        val jsonObject = JPushHelper.convertNotificationToMap(
            JConstants.NOTIFICATION_UNSHOW,
            notificationMessage
        )
        sendEvent(context,JConstants.NOTIFICATION_UNSHOW_EVENT, jsonObject)
    }

    override fun onNotifyMessageDismiss(
        context: Context?,
        notificationMessage: NotificationMessage
    ) {
        JLogger.d("onNotifyMessageDismiss:$notificationMessage")
        val jsonObject = JPushHelper.convertNotificationToMap(
            JConstants.NOTIFICATION_DISMISSED,
            notificationMessage
        )
        sendEvent(context,JConstants.NOTIFICATION_DISMISSED_EVENT, jsonObject)
    }

    override fun onRegister(context: Context?, registrationId: String) {
        JLogger.d("onRegister:$registrationId")
    }

    override fun onConnected(context: Context?, state: Boolean) {
        JLogger.d("onConnected state:$state")
        val jsonObject = HashMap<String, Any>()
        jsonObject[JConstants.CONNECT_ENABLE] = state
        sendEvent(context,JConstants.CONNECT_EVENT, jsonObject)
    }

    override fun onCommandResult(context: Context?, message: CmdMessage) {
        JLogger.d("onCommandResult:$message")
//        val jsonObject = JSONObject()
//        jsonObject.put(JConstants.COMMAND, message.cmd)
//        jsonObject.put(JConstants.COMMAND_EXTRA, message.extra.toString())
//        jsonObject.put(JConstants.COMMAND_MESSAGE, message.msg)
//        jsonObject.put(JConstants.COMMAND_RESULT, message.errorCode)
//        JPushHelper.sendEvent(JConstants.COMMAND_EVENT, jsonObject)
    }

    override fun onTagOperatorResult(context: Context?, jPushMessage: JPushMessage) {
        JLogger.d("onTagOperatorResult:$jPushMessage")
        val jsonObject = JPushHelper.convertJPushMessageToMap(1, jPushMessage)
        sendEvent(context,JConstants.TAG_EVENT, jsonObject)
    }

    override fun onCheckTagOperatorResult(context: Context?, jPushMessage: JPushMessage) {
        JLogger.d("onCheckTagOperatorResult:$jPushMessage")
        val jsonObject = JPushHelper.convertJPushMessageToMap(2, jPushMessage)
        sendEvent(context,JConstants.TAG_EVENT, jsonObject)
    }

    override fun onAliasOperatorResult(context: Context?, jPushMessage: JPushMessage) {
        JLogger.d("onAliasOperatorResult:$jPushMessage")
        val jsonObject = JPushHelper.convertJPushMessageToMap(3, jPushMessage)
        sendEvent(context,JConstants.ALIAS_EVENT, jsonObject)
    }

    override fun onMobileNumberOperatorResult(context: Context?, jPushMessage: JPushMessage) {
        JLogger.d("onMobileNumberOperatorResult:$jPushMessage")
        val jsonObject = HashMap<String, Any>()
        jsonObject[JConstants.CODE] = jPushMessage.errorCode
        jsonObject[JConstants.SEQUENCE] = jPushMessage.sequence
        sendEvent(context,JConstants.MOBILE_NUMBER_EVENT, jsonObject)
    }


    override fun onInAppMessageShow(context: Context?, notificationMessage: NotificationMessage) {
        JLogger.d("onInAppMessageShow:$notificationMessage")
        val jsonObject =
            JPushHelper.convertInAppMessageToMap(JConstants.INAPP_MESSAGE_SHOW, notificationMessage)
        sendEvent(context,JConstants.INAPP_MESSAGE_SHOW_EVENT, jsonObject)
    }

    override fun onInAppMessageClick(context: Context?, notificationMessage: NotificationMessage) {
        JLogger.d("onInAppMessageClick:$notificationMessage")
        val jsonObject = JPushHelper.convertInAppMessageToMap(
            JConstants.INAPP_MESSAGE_OPENED,
            notificationMessage
        )
        sendEvent(context,JConstants.INAPP_MESSAGE_CLICK_EVENT, jsonObject)
    }

    fun sendEvent(context: Context?,eventName: String, data: HashMap<String, Any>?){
        JLogger.d("JPushModuleReceiver sendEvent:$eventName, $data")
        var jsonObject = HashMap<String, Any>()
        if (data != null) {
            jsonObject = data;
        }
//        if (data != null) {
//            TestNativePlugin.sendPluginEvent(eventName, data)
//        }else {
//            TestNativePlugin.sendPluginEvent(eventName, jsonObject)
//        }
        val jsonStr = jsonObject.toString()
        val testIntent = Intent(JConstants.INTENT_ACTION_NAME)
        testIntent.putExtra(JConstants.EVENT_NAME, eventName)
        testIntent.putExtra(JConstants.EVENT_DATA, jsonStr)
        context?.sendBroadcast(testIntent)
        JLogger.d("JPushModuleReceiver sendEvent:$context")
    }
}