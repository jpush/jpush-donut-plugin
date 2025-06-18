package com.donut.wx324c7e239a81ad0f


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.api.JPushMessage
import cn.jpush.android.api.NotificationMessage
import org.json.JSONArray
import org.json.JSONObject


class JPushHelper {
    companion object {

        fun convertNotificationToMap(
            eventType: String?,
            message: NotificationMessage
        ): HashMap<String, Any>? {
            val hashMap = HashMap<String, Any>()
            hashMap[JConstants.NOTIFICATION_EVENT_TYPE] = eventType ?: ""
            hashMap[JConstants.MESSAGE_ID] = message.msgId
            hashMap[JConstants.TITLE] = message.notificationTitle
            hashMap[JConstants.CONTENT] = message.notificationContent
            convertExtras(message.notificationExtras, hashMap)
            return hashMap
        }

        fun convertThirdOpenNotificationToMap(
            msgId: String?,
            title: String?,
            content: String?,
            extra: String,
            android: String?
        ): HashMap<String, Any>? {
            val hashMap = HashMap<String, Any>()
            hashMap[JConstants.NOTIFICATION_EVENT_TYPE] = JConstants.NOTIFICATION_OPENED
            hashMap[JConstants.MESSAGE_ID] = msgId ?: ""
            hashMap[JConstants.TITLE] = title ?: ""
            hashMap[JConstants.CONTENT] = content ?: ""
            convertExtras(extra, hashMap)
            return hashMap
        }

        fun convertInAppMessageToMap(
            eventType: String?,
            message: NotificationMessage
        ): HashMap<String, Any>? {
            val hashMap = HashMap<String, Any>()
            hashMap[JConstants.INAPP_MESSAGE_EVENT_TYPE] = eventType ?: ""
            hashMap[JConstants.MESSAGE_ID] = message.msgId
            hashMap[JConstants.TITLE] = message.inAppMsgTitle
            hashMap[JConstants.CONTENT] = message.inAppMsgContentBody
            hashMap[JConstants.INAPPCLICKACTION] = message.inAppClickAction
            hashMap[JConstants.INAPPEXTRAS] = message.inAppExtras
            hashMap[JConstants.INAPPSHOWTARGET] = message.inAppShowTarget
            convertExtras(message.notificationExtras, hashMap)
            return hashMap
        }

        fun convertNotificationBundleToMap(eventType: String?, bundle: Bundle): HashMap<String, Any>? {
            val hashMap = HashMap<String, Any>()
            hashMap[JConstants.NOTIFICATION_EVENT_TYPE] = eventType ?: ""
            hashMap[JConstants.MESSAGE_ID] = bundle.getString("cn.jpush.android.MSG_ID", "")
            hashMap[JConstants.TITLE] = bundle.getString("cn.jpush.android.NOTIFICATION_CONTENT_TITLE", "")
            hashMap[JConstants.CONTENT] = bundle.getString("cn.jpush.android.ALERT", "")
            convertExtras(bundle.getString("cn.jpush.android.EXTRA", ""), hashMap)
            return hashMap
        }

        fun convertCustomMessage(customMessage: CustomMessage): HashMap<String, Any>? {
            val hashMap = HashMap<String, Any>()
            hashMap[JConstants.MESSAGE_ID] = customMessage.messageId
            hashMap[JConstants.TITLE] = customMessage.title
            hashMap[JConstants.CONTENT] = customMessage.message
            convertExtras(customMessage.extra, hashMap)
            return hashMap
        }

        fun convertJPushMessageToMap(type: Int, message: JPushMessage): HashMap<String, Any>? {
            val hashMap = HashMap<String, Any>()
            hashMap[JConstants.CODE] = message.errorCode
            hashMap[JConstants.SEQUENCE] = message.sequence
            when (type) {
                1 -> {
                    val tags = message.tags
                    val tagsList = mutableListOf<String>()
                    if (tags == null || tags.isEmpty()) {
                        JLogger.d("tags is empty")
                    } else {
                        for (tag in tags) {
                            tagsList.add(tag)
                        }
                    }
                    hashMap[JConstants.TAGS] = tagsList
                }
                2 -> {
                    hashMap[JConstants.TAG_ENABLE] = message.tagCheckStateResult
                    hashMap[JConstants.TAG] = message.checkTag
                }
                3 -> hashMap[JConstants.ALIAS] = message.alias
            }
            return hashMap
        }

        fun convertExtras(extras: String, hashMap: HashMap<String, Any>) {
            if (TextUtils.isEmpty(extras) || extras == "{}") return
            try {
//                val extrasObject = JSONObject.parseObject(extras)
                hashMap[JConstants.EXTRAS] = extras
            } catch (throwable: Throwable) {
                JLogger.w("convertExtras error:" + throwable.message)
            }
        }

        fun launchApp(context: Context) {
            try {
                val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                intent!!.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                context.startActivity(intent)
            } catch (throwable: Throwable) {
                JLogger.e("")
            }
        }

        var OPEN_NOTIFICATION_DATA: HashMap<String, Any>? = null
        var OPEN_NOTIFICATION_TYPE = 0
        var IS_DESTROY = true // 标识当前插件是否被销毁


        /**
         * 缓存通知点击信息，再用户注册监听后返回给用户
         *
         * @param hashMap
         */
        fun saveOpenNotifiData(hashMap: HashMap<String, Any>, type: Int) {
            if (IS_DESTROY) {
                JLogger.d("saveOpenNotifiData:$hashMap")
                OPEN_NOTIFICATION_DATA = hashMap
                OPEN_NOTIFICATION_TYPE = type
            }
        }

        fun sendCacheOpenNotifiToUser(type: Int) {
//            if (type == 0 && OPEN_NOTIFICATION_TYPE == 1) {
//                return
//            }
//            if (!IS_DESTROY && OPEN_NOTIFICATION_DATA != null) {
//                JLogger.d("sendCacheOpenNotifiToUser:$OPEN_NOTIFICATION_DATA")
//                sendNotifactionEvent(OPEN_NOTIFICATION_DATA, OPEN_NOTIFICATION_TYPE)
//                OPEN_NOTIFICATION_DATA = null
//            }
        }
    }
}