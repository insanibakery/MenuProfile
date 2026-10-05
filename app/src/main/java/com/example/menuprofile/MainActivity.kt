package com.example.menuprofile

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// ==========================================
// 1. MODEL DATA ANGGOTA KELOMPOK
// ==========================================
data class Member(
    val id: Int,
    val name: String,
    val nim: String,
    val role: String,
    val imageRes: Int, // Gunakan resource R.drawable.foto_kalian
    val bioAndHobby: String,
    val videoUrl: String
)

// ==========================================
// 2. DATA 4 ANGGOTA KELOMPOK
// ==========================================
val groupMembers = listOf(
    Member(
        id = 0,
        name = "Anggota 1 (Nama Kamu)",
        nim = "202401001",
        role = "Project Manager",
        imageRes = android.R.drawable.ic_menu_gallery,
        bioAndHobby = "Saya menyukai pengembangan aplikasi Android dan fotografi lanskap. Di waktu luang saya suka mendaki gunung.",
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
    ),
    Member(
        id = 1,
        name = "Anggota 2",
        nim = "202401002",
        role = "UI/UX Designer",
        imageRes = android.R.drawable.ic_menu_gallery,
        bioAndHobby = "Hobi saya merancang antarmuka antarmuka aplikasi dengan Material 3. Suka menggambar ilustrasi digital.",
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
    ),
    Member(
        id = 2,
        name = "Anggota 3",
        nim = "202401003",
        role = "Frontend Developer",
        imageRes = android.R.drawable.ic_menu_gallery,
        bioAndHobby = "Fokus pada pembuatan komponen UI interaktif menggunakan Jetpack Compose. Hobi bermain game catur.",
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
    ),
    Member(
        id = 3,
        name = "Anggota 4",
        nim = "202401004",
        role = "Backend & QA",
        imageRes = android.R.drawable.ic_menu_gallery,
        bioAndHobby = "Tertarik pada arsitektur API dan integrasi data. Di waktu senggang suka berolahraga basket.",
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
    )
)

// ==========================================
// 3. MAIN ACTIVITY
// ==========================================
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

// ==========================================
// 4. NAVHOST (PERPINDAHAN HALAMAN)
// ==========================================
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("detail/{memberId}") { backStackEntry ->
            val memberId = backStackEntry.arguments?.getString("memberId")?.toIntOrNull() ?: 0
            val member = groupMembers.find { it.id == memberId } ?: groupMembers[0]
            MemberDetailScreen(member = member, navController = navController)
        }
    }
}

// ==========================================
// 5. HALAMAN UTAMA (NAMA KELOMPOK + 4 CARD ANGGOTA)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Kelompok 5 - Mobile Dev",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Daftar Anggota Kelompok",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Menampilkan Kartu 4 Anggota
            groupMembers.forEach { member ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            navController.navigate("detail/${member.id}")
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = member.imageRes),
                            contentDescription = member.name,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = member.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "${member.nim} • ${member.role}",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 6. HALAMAN DETAIL PROFIL ANGGOTA (3 UNSUR)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDetailScreen(member: Member, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profil Anggota") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // UNSUR 1: GAMBAR (Foto Profil)
            Image(
                painter = painterResource(id = member.imageRes),
                contentDescription = member.name,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            // UNSUR 2: TEXT (Data Pribadi & Cerita Hobi)
            Text(
                text = member.name,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
            Text(
                text = "${member.nim} | ${member.role}",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Cerita & Hobi",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = member.bioAndHobby,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // UNSUR 3: VIDEO SINGKAT
            Text(
                text = "Video Singkat Profil",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Player Video AndroidView
            AndroidView(
                factory = { context ->
                    VideoView(context).apply {
                        setVideoURI(member.videoUrl.toUri())
                        val mediaController = MediaController(context)
                        mediaController.setAnchorView(this)
                        setMediaController(mediaController)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }
    }
}