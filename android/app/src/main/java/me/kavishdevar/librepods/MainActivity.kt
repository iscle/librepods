/*
    LibrePods - AirPods liberated from Apple’s ecosystem
    Copyright (C) 2025 LibrePods contributors

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package me.kavishdevar.librepods

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.edit
import dagger.hilt.android.AndroidEntryPoint
import me.kavishdevar.librepods.ui.LibrePodsApp
import me.kavishdevar.librepods.ui.theme.LibrePodsTheme
import kotlin.io.encoding.Base64

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        init {
            System.loadLibrary("l2c_fcr_hook")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LibrePodsTheme {
                LibrePodsApp()
            }
        }

        handleIncomingIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingIntent(intent)
    }

    private fun handleIncomingIntent(intent: Intent) {
        val data = intent.data

        if (data?.scheme == "librepods") {
            when (data.host) {
                "add-magic-keys" -> {
                    val queryParams = data.queryParameterNames
                    queryParams.forEach { param ->
                        val value = data.getQueryParameter(param)
                        Log.d("LibrePods", "Parameter: $param = $value")
                    }

                    handleAddMagicKeys(data)
                }
            }
        }
    }

    private fun handleAddMagicKeys(uri: Uri) {
        val sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)

        val irkHex = uri.getQueryParameter("irk")
        val encKeyHex = uri.getQueryParameter("enc_key")

        try {
            if (irkHex != null && validateHexInput(irkHex)) {
                val irkBytes = hexStringToByteArray(irkHex)
                val irkBase64 = Base64.encode(irkBytes)
                sharedPreferences.edit {putString("IRK", irkBase64)}
            }

            if (encKeyHex != null && validateHexInput(encKeyHex)) {
                val encKeyBytes = hexStringToByteArray(encKeyHex)
                val encKeyBase64 = Base64.encode(encKeyBytes)
                sharedPreferences.edit { putString("ENC_KEY", encKeyBase64)}
            }

            Toast.makeText(this, "Magic keys added successfully!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error processing magic keys: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun validateHexInput(input: String): Boolean {
        val hexPattern = Regex("^[0-9a-fA-F]{32}$")
        return hexPattern.matches(input)
    }

    private fun hexStringToByteArray(hex: String): ByteArray {
        val result = ByteArray(16)
        for (i in 0 until 16) {
            val hexByte = hex.substring(i * 2, i * 2 + 2)
            result[i] = hexByte.toInt(16).toByte()
        }
        return result
    }
}
