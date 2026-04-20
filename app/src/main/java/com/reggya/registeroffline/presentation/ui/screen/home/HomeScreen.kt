package com.reggya.registeroffline.presentation.ui.screen.home

import android.R
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.reggya.registeroffline.domain.model.Member
import com.reggya.registeroffline.presentation.ui.viewmodel.MemberViewModel
import com.reggya.registeroffline.presentation.utils.UiState
import com.reggya.registeroffline.presentation.ui.viewmodel.UserViewModel
import com.reggya.registeroffline.presentation.theme.AccentBlue
import com.reggya.registeroffline.presentation.theme.BackgroundGray
import com.reggya.registeroffline.presentation.theme.CardWhite
import com.reggya.registeroffline.presentation.theme.DividerColor
import com.reggya.registeroffline.presentation.theme.PrimaryBlue
import com.reggya.registeroffline.presentation.theme.TextPrimary
import com.reggya.registeroffline.presentation.theme.TextSecondary
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddMember: () -> Unit = {},
    onEditMember: (Long) -> Unit = {},
    onUploadMember: (Long) -> Unit = {},
    onProfileClick: () -> Unit = {},
    userViewModel: UserViewModel = hiltViewModel(),
    viewModel: MemberViewModel = hiltViewModel()
) {
    val profileState by userViewModel.profile.collectAsStateWithLifecycle()
    val memberState by viewModel.memberList.collectAsStateWithLifecycle()
    val user = (profileState as? UiState.Success)?.data
    var selectedTab by remember { mutableIntStateOf(0) }
    var draftMembers by remember { mutableStateOf<List<Member>?>(emptyList())}
    var memberUploaded by remember { mutableStateOf<List<Member>?>(emptyList())}
    val tabs = listOf("Draft", "Sudah Di-Upload")


    LaunchedEffect(user) {
        viewModel.getDraftMemberList()
        viewModel.draftMemberList.collect {
            if (it is UiState.Success) {
                draftMembers = it.data
            }
        }
    }

    LaunchedEffect(memberState) {
            if (memberState is UiState.Success<List<Member>>) {
                memberUploaded = (memberState as UiState.Success<List<Member>>).data
            }

    }

    println("user 1 $user")
    Scaffold(
        containerColor = BackgroundGray,
        topBar = {
            HomeTopBar(
                userName = user?.name ?: "",
                onProfileClick = onProfileClick
            )
        },
        bottomBar = {
            HomeBottomBar(
                draftCount = draftMembers?.size ?: 0,
                onAddMember = onAddMember,
                onUploadAll = { viewModel.uploadAll() },
                isUploading = false
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            HomeTabRow(
                selectedTab = selectedTab,
                tabs = tabs,
                onTabSelected = { selectedTab = it }
            )


            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally { it } + fadeIn() togetherWith
                                slideOutHorizontally { -it } + fadeOut()
                    } else {
                        slideInHorizontally { -it } + fadeIn() togetherWith
                                slideOutHorizontally { it } + fadeOut()
                    }
                },
                label = "tab_animation"
            ) { tab ->

                when (tab) {
                    0 -> DraftTab(
                        members = draftMembers ?: emptyList(),
                        onEdit = onEditMember,
                        onUpload = onUploadMember
                    )
                    1 -> {
                        LaunchedEffect(Unit) {
                            viewModel.getMemberList()
                        }
                        UploadedTab(
                            members = memberUploaded ?: emptyList()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    userName: String,
    onProfileClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CardWhite
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountBox,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Register Offline",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary
                )
            }
        },
        actions = {
            Surface(
                onClick = onProfileClick,
                shape = RoundedCornerShape(20.dp),
                color = BackgroundGray,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = userName.ifEmpty { "User" },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                    Surface(
                        shape = CircleShape,
                        color = PrimaryBlue,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxSize()
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun HomeTabRow(
    selectedTab: Int,
    tabs: List<String>,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTab,
        containerColor = CardWhite,
        contentColor = PrimaryBlue,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                height = 3.dp,
                color = PrimaryBlue
            )
        },
        divider = {
            HorizontalDivider(color = DividerColor)
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                },
                selectedContentColor = PrimaryBlue,
                unselectedContentColor = TextSecondary
            )
        }
    }
}

@Composable
fun DraftTab(
    members: List<Member>,
    onEdit: (Long) -> Unit,
    onUpload: (Long) -> Unit
) {
    if (members.isEmpty()) {
        EmptyState(
            message = "Belum ada data",
            subtitle = "Klik \"Tambah Data\" untuk menambahkan data calon anggota"
        )
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            InfoBanner(
                message = "Nomor Handphone, NIK, dan Foto KTP wajib diisi sebelum di-upload"
            )

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(members) { index, member ->
                    DraftMemberCard(
                        index = index + 1,
                        member = member,
                        onEdit = { onEdit(member.id) },
                        onUpload = { onUpload(member.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun DraftMemberCard(
    index: Int,
    member: Member,
    onEdit: () -> Unit,
    onUpload: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Surface(
                    shape = CircleShape,
                    color = BackgroundGray,
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "$index",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = BackgroundGray,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountBox,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = member.nik.maskNik(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = member.phone,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                ) {
                    Text(
                        text = "Draft",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = DividerColor
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, DividerColor),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Edit",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Button(
                    onClick = onUpload,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue
                    ),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Upload,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Upload",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun UploadedTab(members: List<Member>) {
    if (members.isEmpty()) {
        EmptyState(
            message = "Belum ada data yang di-upload",
            subtitle = "Data yang sudah di-upload akan muncul di sini"
        )
    } else {
        Column(modifier = Modifier.fillMaxSize()) {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Data-data ini sudah dikirimkan ke admin verifikator.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(members) { index, member ->
                    UploadedMemberCard(index = index + 1, member = member)
                }
            }
        }
    }
}

@Composable
fun UploadedMemberCard(
    index: Int,
    member: Member
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = BackgroundGray,
                modifier = Modifier.size(28.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$index",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = BackgroundGray,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccountBox,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = member.nik.maskNik(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = TextPrimary
                )
                Text(
                    text = member.phone,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }


            Surface(
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "Di-upload",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
fun HomeBottomBar(
    draftCount: Int,
    onAddMember: () -> Unit,
    onUploadAll: () -> Unit,
    isUploading: Boolean
) {
    Surface(
        shadowElevation = 8.dp,
        color = CardWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onAddMember,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue
                ),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Tambah Data",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }

            OutlinedButton(
                onClick = onUploadAll,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, DividerColor),
                contentPadding = PaddingValues(vertical = 14.dp),
                enabled = draftCount > 0 && !isUploading
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = PrimaryBlue
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Mengupload...",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Upload,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = if (draftCount > 0) TextPrimary else TextSecondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (draftCount > 0) "Upload Semua ($draftCount)" else "Upload Semua",
                        fontSize = 14.sp,
                        color = if (draftCount > 0) TextPrimary else TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun InfoBanner(message: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        color = AccentBlue.copy(alpha = 0.08f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = null,
                tint = AccentBlue,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = message,
                fontSize = 12.sp,
                color = AccentBlue,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun EmptyState(message: String, subtitle: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.FolderOpen,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = TextSecondary.copy(alpha = 0.4f)
            )
            Text(
                text = message,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun UploadConfirmationDialog(
    count: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardWhite,
        shape = RoundedCornerShape(16.dp),
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Upload,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Upload Semua Data",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Text(
                text = "Apakah kamu yakin ingin upload semua data? Pastikan kamu sudah mengisi semua data yang diperlukan dengan benar, ya!",
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
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text(
                    text = "Ya, Upload Semua ($count)",
                    fontWeight = FontWeight.SemiBold
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
                Text(text = "Batal", color = TextPrimary)
            }
        }
    )
}

fun String.maskNik(): String {
    return if (length >= 16) {
        "${take(3)}*********${takeLast(3)}"
    } else this
}