package com.socialsirius.messenger.base


import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.socialsirius.messenger.base.providers.Encryption
import com.socialsirius.messenger.models.User
import com.socialsirius.messenger.utils.HashUtils
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
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

    fun setPin(pin: String?) {
        //  editor.putString("token", encryption.encryptOrNull(userModel.getToken()))
        with(prefs.edit()) {
            putString(
                getEncryptionDefault().encryptOrNull("pin") ?: "",
                getEncryptionDefault().encryptOrNull(pin) ?: ""
            )
            apply()
        }
    }

    fun setUseBiometric(useBiometric: Boolean) {
        //  editor.putString("token", encryption.encryptOrNull(userModel.getToken()))
        with(prefs.edit()) {
            putBoolean(
                getEncryptionDefault().encryptOrNull("use_biometric") ?: "",
                useBiometric
            )
            apply()
        }
    }

    fun getUseBiometric(): Boolean {
        val tokenKey = getEncryptionDefault().encryptOrNull("use_biometric") ?: ""
        return prefs.getBoolean(tokenKey, false)
    }


    fun getPin(): String {
        val tokenKey = getEncryptionDefault().encryptOrNull("pin") ?: ""
        val string = prefs.getString(tokenKey, "") ?: ""
        return getEncryptionDefault().decryptOrNull(string)
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
        Log.d("mylog2090", "getUser()?.uid=" + getUser()?.uid)
        Log.d("mylog2090", "getUser()?.name=" + getUser()?.name)
        Log.d("mylog2090", "getUser()?.pass=" + getUser()?.pass)
        return isLoggedIn
    }

    fun getBlockType(): Int {
        return getPrefMain().getInt("block_type", 0)
    }

    fun getTimeByBlockType(): Long {
        val type = getBlockType()
        when (type) {
            0 -> return 30 * 1000
            1 -> return 90 * 1000
            2 -> return 60 * 3 * 1000
            3 -> return 60 * 5 * 1000
            4 -> return 1000
        }
        return 1000
    }

    fun setBlockType(blockType: Int) {
        getPrefMain().edit().putInt("block_type", blockType).apply()
    }

    fun setSyncContacts(isSync: Boolean) {
        getPrefMain().edit().putBoolean("sync_contacts", isSync).apply()
    }

    fun isSyncContacts(): Boolean {
        return getPrefMain().getBoolean("sync_contacts", true)
    }

    fun getUserCodeTryCount(): Int {
        try {
            val defValue: String = HashUtils.generateStorngPasswordHash(3.toString() + "")
            val value = getPrefMain().getString("hash_code_try", defValue)
            if (TextUtils.isEmpty(value)) {
                return 3
            }
            val isZero: Boolean = HashUtils.validatePassword(0.toString() + "", value)
            val isOne: Boolean = HashUtils.validatePassword(1.toString() + "", value)
            val isTwo: Boolean = HashUtils.validatePassword(2.toString() + "", value)
            val isThree: Boolean = HashUtils.validatePassword(3.toString() + "", value)
            if (isZero) {
                return 0
            }
            if (isOne) {
                return 1
            }
            if (isTwo) {
                return 2
            }
            if (isThree) {
                return 3
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        }
        return 3
    }

    fun setUserCodeTryCount(count: Int) {
        try {
            val defValue: String = HashUtils.generateStorngPasswordHash(count.toString() + "")
            getPrefMain().edit().putString("hash_code_try", defValue).apply()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        }
    }

}