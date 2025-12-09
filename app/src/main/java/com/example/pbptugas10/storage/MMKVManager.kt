package com.example.pbptugas10.storage

import android.content.Context
import com.tencent.mmkv.MMKV

object MMKVManager {
    private const val LOGIN_KEY = "login_info"
    private const val USER_EMAIL = "user_email"
    private const val USER_UID = "user_uid"
    private const val USER_NAME = "user_name"
    
    private var kv: MMKV? = null
    
    fun init(context: Context) {
        MMKV.initialize(context)
        kv = MMKV.defaultMMKV()
    }
    
    fun saveLoginInfo(email: String, uid: String, name: String = "") {
        kv?.encode(USER_EMAIL, email)
        kv?.encode(USER_UID, uid)
        kv?.encode(USER_NAME, name)
    }
    
    fun getEmail(): String? {
        return kv?.decodeString(USER_EMAIL)
    }
    
    fun getUid(): String? {
        return kv?.decodeString(USER_UID)
    }
    
    fun getUserName(): String? {
        return kv?.decodeString(USER_NAME)
    }
    
    fun clearLoginInfo() {
        kv?.removeValueForKey(USER_EMAIL)
        kv?.removeValueForKey(USER_UID)
        kv?.removeValueForKey(USER_NAME)
    }
    
    fun isUserLoggedIn(): Boolean {
        return !getUid().isNullOrEmpty()
    }
}
