package com.example.broadcastreceiver_telefona

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast


class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("CallReceiver", "Recibio un evento de llamada")

        if (context == null || intent == null) return

        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val incomingNumber = getIncomingNumber(telephonyManager)

            Log.d("CallReceiver", "Estado: $state, Numero entrante: $incomingNumber")

            val sharedPreferences = context.getSharedPreferences("AutoReplyPrefs", Context.MODE_PRIVATE)
            val savedNumber = sharedPreferences.getString("savedNumber", "")
            val savedMessage = sharedPreferences.getString("savedMessage", "")

            if (incomingNumber != null && incomingNumber == savedNumber) {
                Log.d("CallReceiver", "Numero coincide con uno guardado. Enviando SMS...")
                sendAutoReply(context, incomingNumber, savedMessage ?: "Estoy ocupado, me pongo en contacto luego.")
            } else {
                Log.d("CallReceiver", "Numero no coincide. No se enviara mensaje SMS.")
            }
        }
    }

    private fun getIncomingNumber(telephonyManager: TelephonyManager): String? {
        return try {
            val clazz = Class.forName(telephonyManager.javaClass.name)
            val method = clazz.getDeclaredMethod("getLine1Number")
            method.invoke(telephonyManager) as String
        } catch (e: Exception) {
            null
        }
    }
}


    private fun sendAutoReply(context: Context, phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(context, "Mensaje enviado a $phoneNumber :)", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error al enviar el mensaje", Toast.LENGTH_SHORT).show()
        }
    }

