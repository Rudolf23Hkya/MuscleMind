package com.elte_r532ov.musclemind.data.sessionManagement

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

//Context means the object calling the function(pu this in place)
@Singleton
class SessionManagement @Inject constructor(@ApplicationContext private val context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        "AppSessionPreferences",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    //After Login
    fun saveSessionToken(token: String) {
        with(sharedPreferences.edit()) {
            putString("session_token", token)
            apply()
        }
    }
    // Clears the session token, effectively logging out the user
    fun delSessionToken() {
        with(sharedPreferences.edit()) {
            remove("session_token")
            apply()
        }
    }

    // Retrieves the session token
    fun getSessionToken(): String? {
        return sharedPreferences.getString("session_token", null)
    }

    // Checks if a session token exists, indicating if the user is logged in
    fun isLoggedIn(): Boolean {
        return sharedPreferences.contains("session_token")
    }
    //Generates a random session token - Client side session token generation is only temporary
    fun generateSessionToken(): String {
        return UUID.randomUUID().toString()
    }
}