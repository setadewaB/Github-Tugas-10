package com.example.pbptugas10.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pbptugas10.models.Mahasiswa
import com.example.pbptugas10.viewmodel.MahasiswaViewModel

@Composable
fun MahasiswaListScreen(
    mahasiswaViewModel: MahasiswaViewModel,
    onLogout: () -> Unit
) {
    val mahasiswaState by mahasiswaViewModel.mahasiswaState.collectAsState()
    
    LaunchedEffect(Unit) {
        mahasiswaViewModel.fetchMahasiswa()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Daftar Mahasiswa",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Button(onClick = onLogout) {
                Text("Logout")
            }
        }
        
        when {
            mahasiswaState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            mahasiswaState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = mahasiswaState.error ?: "Error",
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(
                            onClick = { mahasiswaViewModel.fetchMahasiswa() },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }
            mahasiswaState.mahasiswaList.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tidak ada data mahasiswa")
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(mahasiswaState.mahasiswaList) { mahasiswa ->
                        MahasiswaCard(
                            mahasiswa = mahasiswa,
                            onDelete = { mahasiswaViewModel.deleteMahasiswa(mahasiswa.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MahasiswaCard(
    mahasiswa: Mahasiswa,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = mahasiswa.nama,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "NIM: ${mahasiswa.nim}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "Email: ${mahasiswa.email}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "Prodi: ${mahasiswa.prodi}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "Semester: ${mahasiswa.semester}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
