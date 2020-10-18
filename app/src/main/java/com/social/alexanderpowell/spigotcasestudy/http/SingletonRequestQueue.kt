package com.social.alexanderpowell.spigotcasestudy.http

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/*
 * Singleton pattern used to instantiate Volley RequestQueue
 * to survive the lifecycle of the application
 */
class SingletonRequestQueue constructor(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: SingletonRequestQueue? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SingletonRequestQueue(context).also {
                    INSTANCE = it
                }
            }
    }

    val requestQueue: RequestQueue by lazy {
        // Must use ApplicationContext to survive the life of the application
        // This will prevent memory leaks
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }

    fun cancelRequestQueue(tag: String) {
        requestQueue.cancelAll(tag)
    }
}