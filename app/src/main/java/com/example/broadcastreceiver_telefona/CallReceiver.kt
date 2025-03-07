package com.example.broadcastreceiver_telefona

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//usar broadcatsreceover, detectar el numero guardado para enviar el mens al mismo


class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("CallReceiver", "Recibio un evento de llamada :0")

        if (context == null || intent == null) return

        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            Log.d("CallReceiver", "Estado: $state")
            Log.d("CallReceiver", "Numero entrante obtenid del intent: $incomingNumber")

            if (incomingNumber == null) {
                Log.e("CallReceiver", "¿Hola?... Adios-")
                return
            }

            val sharedPreferences = context.getSharedPreferences("AutoReplyPrefs", Context.MODE_PRIVATE)
            val savedNumber = sharedPreferences.getString("savedNumber", "")
            val savedMessage = sharedPreferences.getString("savedMessage", "")

            Log.d("CallReceiver", "---------- Numero guardado: $savedNumber")
            Log.d("CallReceiver", "---------- Mensaje a enviar: $savedMessage")

            if (state == TelephonyManager.EXTRA_STATE_RINGING && incomingNumber == savedNumber) {
                Log.d("CallReceiver", "Guardando numero de la llamada activa...")
                sharedPreferences.edit().putString("lastCallNumber", incomingNumber).apply()
            }

            if (state == TelephonyManager.EXTRA_STATE_IDLE) {
                val lastCallNumber = sharedPreferences.getString("lastCallNumber", "") ?: ""

                if (lastCallNumber == savedNumber) {
                    Log.d("CallReceiver", "Llamada finalizada con el numero guardado >:). Enviando SMS...")
                    EnvioMensajeAutom(context, lastCallNumber, savedMessage ?: "Claramente estoy ocupada, te caigo con tu llamada en cuanto pueda.")

                    sharedPreferences.edit().remove("lastCallNumber").apply()
                }
            }
        }
    }

    private fun EnvioMensajeAutom(context: Context, phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(context, "Mensaje enviado a $phoneNumber :)", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("CallReceiver", "Error al enviar el mensaje :C: ${e.message}")
            Toast.makeText(context, "Error al enviar el mensaje :C", Toast.LENGTH_SHORT).show()
        }
    }
}