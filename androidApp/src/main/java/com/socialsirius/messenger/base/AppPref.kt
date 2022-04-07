package com.socialsirius.messenger.base

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.socialsirius.messenger.base.providers.Encryption
import com.socialsirius.messenger.models.User


import java.util.*


class AppPref {
    private val PREF_APP = "app_pref_secure"
    private val PREFS_MAIN = "PREFS_MAIN"
    val prefs = getPrefApp()
    val prefsMain = getPrefMain()
    var encryption: Encryption? = null

    fun getEncryptionDefault(): Encryption {
        if (encryption == null) {
            encryption = Encryption.getDefault(getDeviceId(), getSalt(), ByteArray(16))
        }
        return encryption!!
    }

    init {

    }

    companion object {
        @Volatile
        private var instance: AppPref? = null

        @JvmStatic
        fun getInstance(): AppPref {
            if (instance == null) {
                synchronized(AppPref::class.java) {
                    if (instance == null) {
                        instance = AppPref()
                    }
                }
            }
            return instance!!
        }

    }


    private fun getPrefApp(): SharedPreferences {
        return App.getContext().getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)

    }

    private fun getPrefMain(): SharedPreferences {
        return App.getContext().getSharedPreferences(PREFS_MAIN, Context.MODE_PRIVATE)
    }

    fun getDeviceId(): String {
        var deviceId = prefsMain.getString("deviceId", "") ?: ""
        if (deviceId.isEmpty()) {
            deviceId = UUID.randomUUID().toString()
            prefsMain.edit().putString("deviceId", deviceId).apply()
        }
        return deviceId
    }

    fun isTutorialDone(): Boolean {
        return prefs.getBoolean("tutorialIsDone", false)
    }

    fun setTutorialDone(isDone: Boolean) {
        prefs.edit().putBoolean("tutorialIsDone", isDone).apply()
    }

    fun isShowFaceCredential(): Boolean {
        return prefs.getBoolean("showFaceCredential", true)
    }

    fun setShowFaceCredential(show: Boolean) {
        prefs.edit().putBoolean("showFaceCredential", show).apply()
    }

    fun getSalt(): String {
        return getDeviceId().substring(5)
    }

    fun setToken(token: String?) {
        //  editor.putString("token", encryption.encryptOrNull(userModel.getToken()))
        with(prefs.edit()) {
            putString(
                getEncryptionDefault().encryptOrNull("token") ?: "",
                getEncryptionDefault().encryptOrNull(token) ?: ""
            )
            apply()
        }
    }

    fun getToken(): String {
        val tokenKey = getEncryptionDefault().encryptOrNull("token") ?: ""
        val string = prefs.getString(tokenKey, "") ?: ""
        return getEncryptionDefault().decryptOrNull(string)
    }

    fun cleanAll() {
        prefs.edit().clear().apply()
    }

  fun setUser(user: User?) {
        val userKey = getEncryptionDefault().encryptOrNull("user") ?: ""
        val userJson = Gson().toJson(user)
        with(prefs.edit()) {
            putString(userKey, getEncryptionDefault().encryptOrNull(userJson) ?: "")
            apply()
        }
    }

   fun getUser(): User? {
        try {
            val userKey = getEncryptionDefault().encryptOrNull("user") ?: ""
            val userJsonCrypt = prefs.getString(userKey, "")
            val userJson = getEncryptionDefault().decryptOrNull(userJsonCrypt)
            return Gson().fromJson(
                userJson,
                User::class.java
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    fun isLoggedIn(): Boolean {
        val isLoggedIn = getUser()?.uid != null
        Log.d("mylog2090","getUser()?.uid="+getUser()?.uid)
        Log.d("mylog2090","getUser()?.name="+getUser()?.name)
        Log.d("mylog2090","getUser()?.pass="+getUser()?.pass)
        return isLoggedIn
    }


    fun setPoliceUser(user: User?) {
        val userKey = getEncryptionDefault().encryptOrNull("user_police") ?: ""
        val userJson = Gson().toJson(user)
        with(prefs.edit()) {
            putString(userKey, getEncryptionDefault().encryptOrNull(userJson) ?: "")
            apply()
        }
    }

    fun getPoliceUser(): User? {
        try {
            val userKey = getEncryptionDefault().encryptOrNull("user_police") ?: ""
            val userJsonCrypt = prefs.getString(userKey, "")
            val userJson = getEncryptionDefault().decryptOrNull(userJsonCrypt)
            return Gson().fromJson(
                userJson,
                User::class.java
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    fun isPoliceLoggedIn(): Boolean {
        val isLoggedIn = getPoliceUser()?.uid != null
        return isLoggedIn
    }

}