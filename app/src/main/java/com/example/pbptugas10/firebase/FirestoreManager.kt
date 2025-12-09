package com.example.pbptugas10.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.example.pbptugas10.models.Mahasiswa
import kotlinx.coroutines.tasks.await

object FirestoreManager {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private const val COLLECTION = "mahasiswa"
    
    suspend fun fetchMahasiswa(): Result<List<Mahasiswa>> {
        return try {
            val snapshot = db.collection(COLLECTION).get().await()
            val mahasiswaList = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Mahasiswa::class.java)?.copy(id = doc.id)
            }
            Result.success(mahasiswaList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun fetchMahasiswaById(id: String): Result<Mahasiswa?> {
        return try {
            val doc = db.collection(COLLECTION).document(id).get().await()
            val mahasiswa = doc.toObject(Mahasiswa::class.java)?.copy(id = doc.id)
            Result.success(mahasiswa)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addMahasiswa(mahasiswa: Mahasiswa): Result<String> {
        return try {
            val ref = db.collection(COLLECTION).add(mahasiswa).await()
            Result.success(ref.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateMahasiswa(id: String, mahasiswa: Mahasiswa): Result<Unit> {
        return try {
            db.collection(COLLECTION).document(id).set(mahasiswa).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteMahasiswa(id: String): Result<Unit> {
        return try {
            db.collection(COLLECTION).document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
