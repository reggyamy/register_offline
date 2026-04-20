package com.reggya.registeroffline.presentation.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.reggya.registeroffline.presentation.utils.UiState
import com.reggya.registeroffline.presentation.ui.viewmodel.UserViewModel
import com.reggya.registeroffline.presentation.theme.BackgroundGray
import com.reggya.registeroffline.presentation.theme.CardWhite
import com.reggya.registeroffline.presentation.theme.DividerColor
import com.reggya.registeroffline.presentation.theme.ErrorRed
import com.reggya.registeroffline.presentation.theme.PrimaryBlue
import com.reggya.registeroffline.presentation.theme.TextPrimary
import com.reggya.registeroffline.presentation.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: UserViewModel = hiltViewModel()
) {
    val profileState by viewModel.profile.collectAsState()
    val userData = (profileState as? UiState.Success)?.data
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Logout dialog
    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                showLogoutDialog = false
                viewModel.logout()
                onLogout()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    Scaffold(
        containerColor = BackgroundGray,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CardWhite
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = TextPrimary
                        )
                    }
                },
                title = {
                    Text(
                        text = "Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))


            ProfileAvatarSection(
                fullName = userData?.name ?: "",
                email = userData?.email ?: ""
            )

            Spacer(modifier = Modifier.height(32.dp))


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    ProfileMenuItem(
                        icon = Icons.Outlined.Password,
                        label = "Ganti Password",
                        iconTint = PrimaryBlue,
                        onClick = { /* no-op */}
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = DividerColor
                    )

                    ProfileMenuItem(
                        icon = Icons.Outlined.Help,
                        label = "Bantuan",
                        iconTint = PrimaryBlue,
                        onClick = { /* no-op */ }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = DividerColor
                    )

                    ProfileMenuItem(
                        icon = Icons.Outlined.Logout,
                        label = "Keluar",
                        iconTint = ErrorRed,
                        labelColor = ErrorRed,
                        showChevron = true,
                        onClick = { showLogoutDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "v1.0.1",
                fontSize = 12.sp,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}
@Composable
fun ProfileAvatarSection(
    fullName: String,
    email: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Avatar circle
        Surface(
            shape = CircleShape,
            modifier = Modifier.size(90.dp),
            color = PrimaryBlue.copy(alpha = 0.12f),
            border = BorderStroke(3.dp, PrimaryBlue.copy(alpha = 0.3f))
        ) {
            Box(contentAlignment = Alignment.Center) {

                val initials = fullName
                    .split(" ")
                    .take(2)
                    .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                    .joinToString("")

                if (initials.isNotEmpty()) {
                    Text(
                        text = initials,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                } else {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }

        // Nama
        Text(
            text = fullName.ifEmpty { "Nama Pengguna" },
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        // Email
        Text(
            text = email.ifEmpty { "email@example.com" },
            fontSize = 13.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    label: String,
    iconTint: Color = PrimaryBlue,
    labelColor: Color = TextPrimary,
    showChevron: Boolean = true,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Icon
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = iconTint.copy(alpha = 0.1f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Label
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = labelColor,
                modifier = Modifier.weight(1f)
            )

            // Chevron
            if (showChevron) {
                Icon(
                    Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardWhite,
        shape = RoundedCornerShape(20.dp),
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Icon
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = ErrorRed.copy(alpha = 0.1f),
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Outlined.Logout,
                            contentDescription = null,
                            tint = ErrorRed,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Text(
                    text = "Keluar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Text(
                text = "Apakah kamu yakin ingin keluar? Data yang ada di draft-mu mungkin akan hilang. Kami sarankan untuk upload terlebih dahulu.",
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue
                )
            ) {
                Text(
                    text = "Ya, keluar",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, DividerColor)
            ) {
                Text(
                    text = "Batal",
                    color = TextPrimary,
                    fontSize = 14.sp
                )
            }
        }
    )
}