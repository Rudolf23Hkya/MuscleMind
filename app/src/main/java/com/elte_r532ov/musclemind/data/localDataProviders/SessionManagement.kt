package com.elte_r532ov.musclemind.data.localDataProviders

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.elte_r532ov.musclemind.data.enums.ExperienceLevel
import com.elte_r532ov.musclemind.data.enums.Gender
import com.elte_r532ov.musclemind.data.api.responses.UserData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

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

    // Saves both access token and refresh token after login
    fun saveTokens(accessToken: String, refreshToken: String) {
        with(sharedPreferences.edit()) {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            apply()
        }
    }

    // Clears both the access token and refresh token, logging out the user
    fun deleteTokens() {
        with(sharedPreferences.edit()) {
            remove("access_token")
            remove("refresh_token")
            apply()
        }
    }

    // Storing user data
    fun storeUserData(userData: UserData) {
        with(sharedPreferences.edit()) {
            putString("email", userData.email)
            putString("username", userData.username)
            putString("gender", userData.gender.toString())
            putString("experienceLevel", userData.experiencelevel.toString())
            putString("age", userData.age.toString())
            putString("height", userData.height.toString())
            putString("weight", userData.weight.toString())

            apply()
        }
    }

    // Retrieves the access token
    fun getAccessToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }
    // Retrieves the access token with Bearer before it
    fun getBearerToken(): String {
        val accessToken = sharedPreferences.getString("access_token", null)
        return "Bearer $accessToken"
    }

    // Retrieves the refresh token
    fun getRefreshToken(): String? {
        return sharedPreferences.getString("refresh_token", null)
    }

    // Checks if a session token exists, indicating if the user is logged in
    fun isLoggedIn(): Boolean {
        return sharedPreferences.contains("access_token")
    }

    // Retrieving user data
    fun getUserData(): UserData {
        return UserData(
            email = sharedPreferences.getString("email", null) ?: "",
            username = sharedPreferences.getString("username", null) ?: "",
            gender = sharedPreferences.getString("gender", null)?.let
            { enumValueOf<Gender>(it) } ?: Gender.MALE,
            experiencelevel = sharedPreferences.getString("experienceLevel", null)?.let
            { enumValueOf<ExperienceLevel>(it) } ?: ExperienceLevel.NEW,
            age = sharedPreferences.getString("age", null)?.toIntOrNull() ?: 0,
            weight = sharedPreferences.getString("weight", null)?.toDoubleOrNull() ?: 0.0,
            height = sharedPreferences.getString("height", null)?.toDoubleOrNull() ?: 0.0
        )
    }
}
