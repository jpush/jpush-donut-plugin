package com.donut.wx324c7e239a81ad0f

class JConstants {
    companion object {
        const val DEBUG = "debug"

        const val REGISTRATION_ID = "registerID"

        const val CODE = "code"
        const val CODE_SUCESS = 0
        const val CHANNEL = "channel"
        const val IMEI = "imei"
        const val IMSI = "imsi"
        const val MAC = "mac"
        const val WIFI = "wifi"
        const val BSSID = "bssid"
        const val SSID = "ssid"
        const val CELL = "cell"
        const val CHANNEL_ID = "channel_id"
        const val SOUND = "sound"
        const val SEQUENCE = "sequence"
        const val CONNECT_ENABLE = "connectEnable"

        //电话号码
        const val MOBILE_NUMBER = "mobileNumber"

        //pushTime
        const val PUSH_TIME_DAYS = "pushTimeDays"
        const val PUSH_TIME_START_HOUR = "pushTimeStartHour"
        const val PUSH_TIME_END_HOUR = "pushTimeEndHour"

        //silenceTime
        const val SILENCE_TIME_START_HOUR = "silenceTimeStartHour"
        const val SILENCE_TIME_START_MINUTE = "silenceTimeStartMinute"
        const val SILENCE_TIME_END_HOUR = "silenceTimeEndHour"
        const val SILENCE_TIME_END_MINUTE = "silenceTimeEndMinute"

        //消息
        const val MESSAGE_ID = "messageID"
        const val TITLE = "title"
        const val CONTENT = "content"
        const val PLATFORM = "platform"
        const val EXTRAS = "extras"
        const val ANDROID = "android" //透传所有消息字段

        const val INAPPCLICKACTION = "inAppClickAction"
        const val INAPPEXTRAS = "inAppExtras"
        const val INAPPSHOWTARGET = "inAppShowTarget"

        //消息事件类型
        const val NOTIFICATION_ARRIVED = "notificationArrived"
        const val NOTIFICATION_OPENED = "notificationOpened"
        const val NOTIFICATION_DISMISSED = "notificationDismissed"
        const val NOTIFICATION_UNSHOW = "notificationUnShow"

        //应用内消息事件类型
        const val INAPP_MESSAGE_SHOW = "show"
        const val INAPP_MESSAGE_OPENED = "click"
        const val INAPP_MESSAGE_DISMISSED = "disappear"
        const val INAPP_MESSAGE_EVENT_TYPE = "eventType"
        const val INAPP_MESSAGE_TYPE = "messageType"

        //通知消息
        const val NOTIFICATION_EVENT_TYPE = "notificationEventType"
        const val NOTIFICATION_MAX_NUMBER = "notificationMaxNumber"
        const val NOTIFICATION_ID = "notificationId"

        //cmd消息
        const val COMMAND = "command"
        const val COMMAND_EXTRA = "commandExtra"
        const val COMMAND_RESULT = "commandResult"
        const val COMMAND_MESSAGE = "commandMessage"

        //地理围栏
        const val GEO_FENCE_ID = "geoFenceID"
        const val GEO_FENCE_INTERVAL = "geoFenceInterval"
        const val GEO_FENCE_MAX_NUMBER = "geoFenceMaxNumber"

        //tag alias
        const val TAG = "tag"
        const val TAGS = "tags"
        const val ALIAS = "alias"
        const val TAG_ENABLE = "tagEnable"

        //error
        const val PARAMS_NULL = "params cant be null"
        const val PARAMS_ILLEGAL = "params illegal"
        const val PARAMS_ILLEGAL_CHANNEL = "params illegal,channel and channel id must config"
        const val CALLBACK_NULL = "callback cant be null"

        //
        const val INTENT_ACTION_NAME = "JPUSH_INTENT"
        const val EVENT_NAME = "eventName"
        const val EVENT_DATA = "eventData"

        //event
        const val CONNECT_EVENT = "ConnectEvent"
        const val NOTIFICATION_ARRIVED_EVENT = "onNotificationArrived"
        const val NOTIFICATION_CLICK_EVENT = "onNotificationClicked"
        const val NOTIFICATION_DELETE_EVENT = "onNotificationDeleted"
        const val NOTIFICATION_DISMISSED_EVENT = "onNotificationDismissed"
        const val NOTIFICATION_UNSHOW_EVENT = "onNotificationUnShow"
        const val CUSTOM_MESSAGE_EVENT = "onCustomMessage"
        const val LOCAL_NOTIFICATION_EVENT = "LocalNotificationEvent"
        const val TAG_EVENT = "onTagMessage"
        const val ALIAS_EVENT = "onAliasMessage"
        const val MOBILE_NUMBER_EVENT = "MobileNumberEvent"
        const val COMMAND_EVENT = "CommandEvent"
        const val INAPP_MESSAGE_SHOW_EVENT = "onInAppMessageShow"
        const val INAPP_MESSAGE_CLICK_EVENT = "onInAppMessageClick"
    }
}