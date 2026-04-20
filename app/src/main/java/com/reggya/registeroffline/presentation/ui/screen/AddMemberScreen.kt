package com.reggya.registeroffline.presentation.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.reggya.registeroffline.domain.model.Member
import com.reggya.registeroffline.presentation.ui.viewmodel.MemberViewModel
import com.reggya.registeroffline.presentation.utils.UiState
import com.reggya.registeroffline.presentation.theme.AccentBlue
import com.reggya.registeroffline.presentation.theme.BackgroundGray
import com.reggya.registeroffline.presentation.theme.CardWhite
import com.reggya.registeroffline.presentation.theme.DividerColor
import com.reggya.registeroffline.presentation.theme.ErrorRed
import com.reggya.registeroffline.presentation.theme.PrimaryBlue
import com.reggya.registeroffline.presentation.theme.TextHint
import com.reggya.registeroffline.presentation.theme.TextPrimary
import com.reggya.registeroffline.presentation.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemberScreen(
    memberId: Long = 0L,
    onBack: () -> Unit = {},
    onSaved: () -> Unit = {},
    viewModel: MemberViewModel = hiltViewModel()
) {
    val isEdit = memberId != 0L
    val saveState by viewModel.uploadAllState.collectAsState()

    var phone by remember { mutableStateOf("") }
    var nik by remember { mutableStateOf("") }
    var ktpFileUri by remember { mutableStateOf<Uri?>(null) }
    var ktpFileSecondaryUri by remember { mutableStateOf<Uri?>(null) }
    var fullName by remember { mutableStateOf("") }
    var birthPlace by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var province by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var subDistrict by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var sameAsKtp by remember { mutableStateOf(true) }
    var domicileAddress by remember { mutableStateOf("") }
    var domicileProvince by remember { mutableStateOf("") }
    var domicileCity by remember { mutableStateOf("") }
    var domicileDistrict by remember { mutableStateOf("") }
    var domicileSubDistrict by remember { mutableStateOf("") }
    var domicilePostalCode by remember { mutableStateOf("") }
    var isEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(saveState) {
        if (saveState is UiState.Success) onSaved()
    }

    LaunchedEffect(phone, nik, ktpFileUri, ktpFileSecondaryUri) {
        isEnabled = phone.isNotBlank() && nik.isNotBlank() && ktpFileUri != null && ktpFileSecondaryUri != null
    }


    Scaffold(
        containerColor = BackgroundGray,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite),
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
                        text = if (isEdit) "Edit Data" else "Tambah Data",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )
                }
            )
        },
        bottomBar = {
            AddMemberBottomBar(
                isEnabled = isEnabled,
                isLoading = saveState is UiState.Loading,
                onSaveDraft = {
                    viewModel.registerMember(
                        buildMember(
                            phone, nik, ktpFileUri, ktpFileSecondaryUri,
                            fullName, birthPlace, birthDate, status, occupation,
                            address, province, city, district, subDistrict, postalCode,
                            sameAsKtp, domicileAddress, domicileProvince, domicileCity,
                            domicileDistrict, domicileSubDistrict, domicilePostalCode
                        )
                    )
                    onSaved()
                },
                onUpload = {
                    viewModel.uploadMember(
                        buildMember(
                            phone, nik, ktpFileUri, ktpFileSecondaryUri,
                            fullName, birthPlace, birthDate, status, occupation,
                            address, province, city, district, subDistrict, postalCode,
                            sameAsKtp, domicileAddress, domicileProvince, domicileCity,
                            domicileDistrict, domicileSubDistrict, domicilePostalCode
                        )
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            InfoBannerForm()

            FormSection(title = "Data Utama") {

                // Phone
                AppTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = "Masukkan nomor handphone",
                    label = "Nomor Handphone",
                    required = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Phone, null,
                            tint = TextHint, modifier = Modifier.size(18.dp)
                        )
                    }
                )

                // NIK
                AppTextField(
                    value = nik,
                    onValueChange = { if (it.length <= 16) nik = it },
                    placeholder = "16 digit no KTP",
                    label = "NIK",
                    required = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    supportingText = "${nik.length}/16",
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Badge, null,
                            tint = TextHint, modifier = Modifier.size(18.dp)
                        )
                    }
                )

                // Foto KTP
                KtpPhotoSection(
                    primaryUri = ktpFileUri,
                    secondaryUri = ktpFileSecondaryUri,
                    onPrimarySelected = { ktpFileUri = it },
                    onSecondarySelected = { ktpFileSecondaryUri = it }
                )
            }

            FormSection(title = "Informasi Lainnya") {

                AppTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = "Masukkan nama sesuai KTP",
                    label = "Nama Lengkap",
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Person, null,
                            tint = TextHint, modifier = Modifier.size(18.dp)
                        )
                    }
                )

                AppTextField(
                    value = birthPlace,
                    onValueChange = { birthPlace = it },
                    placeholder = "Masukkan tempat lahir sesuai KTP",
                    label = "Tempat Lahir",
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.LocationOn, null,
                            tint = TextHint, modifier = Modifier.size(18.dp)
                        )
                    }
                )

                AppTextField(
                    value = birthDate,
                    onValueChange = { birthDate = it },
                    placeholder = "DD/MM/YY",
                    label = "Tanggal Lahir",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    trailingIcon = {
                        Icon(
                            Icons.Outlined.CalendarMonth, null,
                            tint = TextHint, modifier = Modifier.size(18.dp)
                        )
                    }
                )

                DropdownField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = "Jenis Kelamin",
                    options = listOf("Laki-laki", "Perempuan"),
                    placeholder = "Pilih jenis kelamin"
                )

                DropdownField(
                    value = status,
                    onValueChange = { status = it },
                    label = "Status",
                    options = listOf("Belum Menikah", "Menikah", "Cerai Hidup", "Cerai Mati"),
                    placeholder = "Pilih status sesuai KTP"
                )

                DropdownField(
                    value = occupation,
                    onValueChange = { occupation = it },
                    label = "Pekerjaan",
                    options = listOf(
                        "Pegawai Negeri Sipil", "Pegawai Swasta", "Wiraswasta",
                        "Pelajar/Mahasiswa", "TNI/Polri", "Petani", "Nelayan",
                        "Buruh", "Pensiunan", "Lainnya"
                    ),
                    placeholder = "Pilih pekerjaan sesuai KTP"
                )
            }

            FormSection(title = "Informasi Alamat KTP") {

                AppTextField(
                    value = address,
                    onValueChange = { address = it },
                    placeholder = "Masukkan alamat sesuai KTP",
                    label = "Alamat Lengkap",
                    singleLine = false,
                    minLines = 2
                )

                DropdownField(
                    value = province,
                    onValueChange = { province = it },
                    label = "Provinsi",
                    options = listOf("DKI Jakarta"),
                    placeholder = "Pilih Provinsi"
                )

                DropdownField(
                    value = city,
                    onValueChange = { city = it },
                    label = "Kota/Kabupaten",
                    options = listOf("Jakarta Pusat"),
                    placeholder = "Pilih Kota/Kabupaten"
                )

                DropdownField(
                    value = district,
                    onValueChange = { district = it },
                    label = "Kecamatan",
                    options = listOf("Tanah Abang"),
                    placeholder = "Pilih Kecamatan"
                )

                DropdownField(
                    value = subDistrict,
                    onValueChange = { subDistrict = it },
                    label = "Kelurahan",
                    options = listOf("Kebon Sirih"),
                    placeholder = "Pilih Kelurahan"
                )

                AppTextField(
                    value = postalCode,
                    onValueChange = { if (it.length <= 5) postalCode = it },
                    placeholder = "Masukkan Kode Pos",
                    label = "Kode Pos",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }


            FormSection(title = "Alamat Domisili") {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { sameAsKtp = !sameAsKtp }
                        .padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = sameAsKtp,
                        onCheckedChange = { sameAsKtp = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = PrimaryBlue
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Alamat domisili sama dengan alamat pada KTP",
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                }

                AnimatedVisibility(
                    visible = !sameAsKtp,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                        AppTextField(
                            value = domicileAddress,
                            onValueChange = { domicileAddress = it },
                            placeholder = "Masukkan alamat domisili",
                            label = "Alamat Lengkap",
                            singleLine = false,
                            minLines = 2
                        )

                        DropdownField(
                            value = domicileProvince,
                            onValueChange = { domicileProvince = it },
                            label = "Provinsi",
                            options = listOf("DKI Jakarta", "Jawa Barat", "Jawa Tengah"),
                            placeholder = "Pilih Provinsi"
                        )

                        DropdownField(
                            value = domicileCity,
                            onValueChange = { domicileCity = it },
                            label = "Kota/Kabupaten",
                            options = listOf("Jakarta Selatan", "Jakarta Timur"),
                            placeholder = "Pilih Kota/Kabupaten"
                        )

                        DropdownField(
                            value = domicileDistrict,
                            onValueChange = { domicileDistrict = it },
                            label = "Kecamatan",
                            options = listOf("Setiabudi", "Pancoran"),
                            placeholder = "Pilih Kecamatan"
                        )

                        DropdownField(
                            value = domicileSubDistrict,
                            onValueChange = { domicileSubDistrict = it },
                            label = "Kelurahan",
                            options = listOf("Kuningan", "Karet"),
                            placeholder = "Pilih Kelurahan"
                        )

                        AppTextField(
                            value = domicilePostalCode,
                            onValueChange = { if (it.length <= 5) domicilePostalCode = it },
                            placeholder = "Masukkan Kode Pos",
                            label = "Kode Pos",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun KtpPhotoSection(
    primaryUri: Uri?,
    secondaryUri: Uri?,
    onPrimarySelected: (Uri) -> Unit,
    onSecondarySelected: (Uri) -> Unit
) {
    val primaryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { onPrimarySelected(it) } }

    val secondaryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { onSecondarySelected(it) } }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        FieldLabel(text = "Foto KTP", required = true)
        Text(
            text = "Ambil 2 foto KTP untuk hasil yang lebih baik. Pastikan KTP terlihat jelas dan tidak biru.",
            fontSize = 11.sp,
            color = TextSecondary,
            lineHeight = 16.sp
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            KtpPhotoBox(
                uri = primaryUri,
                label = "KTP Utama",
                modifier = Modifier.weight(1f),
                onClick = { primaryLauncher.launch("image/*") }
            )
            // Secondary KTP
            KtpPhotoBox(
                uri = secondaryUri,
                label = "KTP Pendukung",
                modifier = Modifier.weight(1f),
                onClick = { secondaryLauncher.launch("image/*") }
            )
        }
    }
}

@Composable
fun KtpPhotoBox(
    uri: Uri?,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.aspectRatio(1.6f),
        shape = RoundedCornerShape(10.dp),
        color = if (uri != null) Color.Transparent else BackgroundGray,
        border = BorderStroke(
            1.5.dp,
            if (uri != null) PrimaryBlue else DividerColor
        )
    ) {
        if (uri != null) {
            Box {
                AsyncImage(
                    model = uri,
                    contentDescription = label,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(20.dp)
                ) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(3.dp)
                    )
                }
            }
        } else {
            Box(contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Outlined.CameraAlt,
                        contentDescription = null,
                        tint = TextHint,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        color = TextHint
                    )
                }
            }
        }
    }
}

@Composable
fun FormSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = PrimaryBlue
        )
        HorizontalDivider(color = DividerColor)
        content()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>,
    placeholder: String,
    required: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        FieldLabel(text = label, required = required)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                placeholder = {
                    Text(text = placeholder, fontSize = 13.sp, color = TextHint)
                },
                trailingIcon = {
                    Icon(
                        if (expanded) Icons.Filled.KeyboardArrowUp
                        else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = TextHint
                    )
                },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = DividerColor,
                    focusedContainerColor = CardWhite,
                    unfocusedContainerColor = CardWhite
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    color = TextPrimary
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(CardWhite)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                fontSize = 13.sp,
                                color = if (option == value) PrimaryBlue else TextPrimary,
                                fontWeight = if (option == value) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        },
                        trailingIcon = if (option == value) {
                            {
                                Icon(
                                    Icons.Filled.Check, null,
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else null
                    )
                }
            }
        }
    }
}

@Composable
fun InfoBannerForm() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(10.dp),
        color = AccentBlue.copy(alpha = 0.08f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = CircleShape,
                color = AccentBlue,
                modifier = Modifier.size(20.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "!",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                text = "Nomor Handphone, NIK, Foto KTP, dan Foto DH wajib diisi sebelum disimpan / di-upload",
                fontSize = 12.sp,
                color = AccentBlue,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun AddMemberBottomBar(
    isEnabled: Boolean,
    isLoading: Boolean,
    onSaveDraft: () -> Unit,
    onUpload: () -> Unit
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
                    onClick = onUpload,
                    enabled = !isLoading && isEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue, contentColor = Color.White, disabledContainerColor = PrimaryBlue.copy(alpha = 0.5f))
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text("Upload", fontWeight = FontWeight.SemiBold)
                    }
                }


            OutlinedButton(
                onClick = onSaveDraft,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, PrimaryBlue)
            ) {
                Text(
                    text = "Simpan sebagai Draft",
                    color = PrimaryBlue,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    label: String,
    modifier: Modifier = Modifier,
    required: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    supportingText: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        FieldLabel(text = label, required = required)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth(),
            placeholder = {
                Text(text = placeholder, fontSize = 13.sp, color = TextHint)
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            keyboardOptions = keyboardOptions,
            isError = isError,
            singleLine = singleLine,
            minLines = minLines,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = DividerColor,
                errorBorderColor = ErrorRed,
                focusedContainerColor = CardWhite,
                unfocusedContainerColor = CardWhite,
                cursorColor = PrimaryBlue
            ),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 14.sp,
                color = TextPrimary
            ),
            supportingText = when {
                isError && errorMessage != null -> {
                    { Text(text = errorMessage, fontSize = 11.sp, color = ErrorRed) }
                }

                supportingText != null -> {
                    { Text(text = supportingText, fontSize = 11.sp, color = TextHint) }
                }

                else -> null
            }
        )
    }
}

fun buildMember(
    phone: String, nik: String,
    ktpFileUri: Uri?, ktpFileSecondaryUri: Uri?,
    fullName: String, birthPlace: String, birthDate: String,
    status: String, occupation: String,
    address: String, province: String, city: String,
    district: String, subDistrict: String, postalCode: String,
    sameAsKtp: Boolean,
    domicileAddress: String, domicileProvince: String, domicileCity: String,
    domicileDistrict: String, domicileSubDistrict: String, domicilePostalCode: String
) = Member(
    id = 0,
    name = fullName,
    nik = nik,
    phone = phone,
    birthPlace = birthPlace,
    birthDate = birthDate,
    status = status,
    occupation = occupation,
    address = address,
    province = province,
    city = city,
    district = district,
    subDistrict = subDistrict,
    postalCode = postalCode,
    domicileAddress = if (sameAsKtp) address else domicileAddress,
    domicileProvince = if (sameAsKtp) province else domicileProvince,
    domicileCity = if (sameAsKtp) city else domicileCity,
    domicileDistrict = if (sameAsKtp) district else domicileDistrict,
    domicileSubDistrict = if (sameAsKtp) subDistrict else domicileSubDistrict,
    domicilePostalCode = if (sameAsKtp) postalCode else domicilePostalCode,
    ktpFilePath = ktpFileUri?.toString() ?: "",
    ktpFileSecondaryPath = ktpFileSecondaryUri?.toString() ?: "",
    ktpUrl = "",
    ktpUrlSecondary = "",
)