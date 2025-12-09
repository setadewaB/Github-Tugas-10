package com.example.pbptugas10.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pbptugas10.firebase.FirestoreManager
import com.example.pbptugas10.models.Mahasiswa
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MahasiswaState(
    val mahasiswaList: List<Mahasiswa> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class MahasiswaViewModel : ViewModel() {
    private val _mahasiswaState = MutableStateFlow(MahasiswaState())
    val mahasiswaState: StateFlow<MahasiswaState> = _mahasiswaState
    
    fun fetchMahasiswa() {
        viewModelScope.launch {
            _mahasiswaState.value = _mahasiswaState.value.copy(isLoading = true)
            val result = FirestoreManager.fetchMahasiswa()
            result.onSuccess { mahasiswaList ->
                _mahasiswaState.value = MahasiswaState(
                    mahasiswaList = mahasiswaList,
                    isLoading = false
                )
            }
            result.onFailure { exception ->
                _mahasiswaState.value = MahasiswaState(
                    isLoading = false,
                    error = exception.message ?: "Failed to fetch data"
                )
            }
        }
    }
    
    fun deleteMahasiswa(id: String) {
        viewModelScope.launch {
            val result = FirestoreManager.deleteMahasiswa(id)
            result.onSuccess {
                fetchMahasiswa()
            }
            result.onFailure { exception ->
                _mahasiswaState.value = _mahasiswaState.value.copy(
                    error = exception.message ?: "Failed to delete"
                )
            }
        }
    }
    
    fun clearError() {
        _mahasiswaState.value = _mahasiswaState.value.copy(error = null)
    }
}
