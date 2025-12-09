package com.example.pbptugas10.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.pbptugas10.storage.MMKVManager
import kotlinx.coroutines.tasks.await

object FirebaseAuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
    
    suspend fun register(email: String, password: String, name: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                MMKVManager.saveLoginInfo(user.email ?: "", user.uid, name)
                Result.success(user)
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                MMKVManager.saveLoginInfo(user.email ?: "", user.uid)
                Result.success(user)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun logout(): Result<Unit> {
        return try {
            auth.signOut()
            MMKVManager.clearLoginInfo()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}
