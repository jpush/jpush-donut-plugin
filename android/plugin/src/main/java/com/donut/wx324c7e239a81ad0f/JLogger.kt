package com.donut.wx324c7e239a81ad0f

import android.util.Log

class JLogger {
    companion object {
        private const val TAG = "JLogger"
        private var isDebug = true

        fun setDebug(debug: Boolean) {
            isDebug = debug
        }

        fun d(tag: String, message: String) {
            if (isDebug) {
                Log.d(tag, message)
            }
        }

        fun i(tag: String, message: String) {
            if (isDebug) {
                Log.i(tag, message)
            }
        }

        fun w(tag: String, message: String) {
            if (isDebug) {
                Log.w(tag, message)
            }
        }

        fun e(tag: String, message: String) {
            if (isDebug) {
                Log.e(tag, message)
            }
        }

        fun d(message: String) {
            if (isDebug) {
                Log.d(TAG, message)
            }
        }

        fun i(message: String) {
            if (isDebug) {
                Log.i(TAG, message)
            }
        }

        fun w(message: String) {
            if (isDebug) {
                Log.w(TAG, message)
            }
        }

        fun e(message: String) {
            if (isDebug) {
                Log.e(TAG, message)
            }
        }
    }
}