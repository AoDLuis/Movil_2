package com.example.broadcastreceiver_telefona.Data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AutoReplyRepository @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AutoReplyPrefs", Context.MODE_PRIVATE)

    fun saveConfig(number: String, message: String) {
        sharedPreferences.edit().apply {
            putString("savedNumber", number)
            putString("savedMessage", message)
            apply()
        }
    }

    fun getSavedNumber(): String = sharedPreferences.getString("savedNumber", "") ?: ""
    fun getSavedMessage(): String = sharedPreferences.getString("savedMessage", "") ?: ""
}
