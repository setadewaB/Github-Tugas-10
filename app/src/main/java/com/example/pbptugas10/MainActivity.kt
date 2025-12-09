package com.example.pbptugas10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pbptugas10.storage.MMKVManager
import com.example.pbptugas10.ui.screens.LoginScreen
import com.example.pbptugas10.ui.screens.MahasiswaListScreen
import com.example.pbptugas10.ui.theme.PBPTugas10Theme
import com.example.pbptugas10.viewmodel.AuthViewModel
import com.example.pbptugas10.viewmodel.MahasiswaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize MMKV
        MMKVManager.init(this)
        
        enableEdgeToEdge()
        setContent {
            PBPTugas10Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    AppContent()
                }
            }
        }
    }
}

@Composable
fun AppContent() {
    val authViewModel: AuthViewModel = viewModel()
    val mahasiswaViewModel: MahasiswaViewModel = viewModel()
    
    val authState by authViewModel.authState.collectAsState()
    
    if (authState.isLoggedIn) {
        MahasiswaListScreen(
            mahasiswaViewModel = mahasiswaViewModel,
            onLogout = {
                authViewModel.logout()
            }
        )
    } else {
        LoginScreen(
            authViewModel = authViewModel,
            onLoginSuccess = { }
        )
    }
}
