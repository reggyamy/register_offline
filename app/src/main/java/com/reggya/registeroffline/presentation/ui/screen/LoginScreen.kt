package com.reggya.registeroffline.presentation.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.reggya.registeroffline.presentation.utils.UiState
import com.reggya.registeroffline.presentation.ui.viewmodel.UserViewModel
import com.reggya.registeroffline.presentation.theme.BackgroundGray
import com.reggya.registeroffline.presentation.theme.CardWhite
import com.reggya.registeroffline.presentation.theme.DividerColor
import com.reggya.registeroffline.presentation.theme.ErrorRed
import com.reggya.registeroffline.presentation.theme.PrimaryBlue
import com.reggya.registeroffline.presentation.theme.TextHint
import com.reggya.registeroffline.presentation.theme.TextPrimary
import com.reggya.registeroffline.presentation.theme.TextSecondary

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    viewModel: UserViewModel = hiltViewModel()
) {
    val loginState by viewModel.loginState.collectAsState()
    val focusManager = LocalFocusManager.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }


    LaunchedEffect(loginState) {
        if (loginState is UiState.Success) {
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(56.dp))

            AppHeader()

            Spacer(modifier = Modifier.height(40.dp))


                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Masuk ke Akun Verifikator",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            color = TextPrimary,
                            lineHeight = 28.sp
                        )
                        Text(
                            text = "Masukkan email dan password untuk masuk",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FieldLabel(text = "Email", required = true)
                        AppTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = "Masukkan email di sini",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Email,
                                    contentDescription = null,
                                    tint = TextHint,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FieldLabel(text = "Password")
                        AppTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = "Masukkan password",
                            visualTransformation = if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    viewModel.login(email, password)
                                }
                            ),
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Lock,
                                    contentDescription = null,
                                    tint = TextHint,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { passwordVisible = !passwordVisible }
                                ) {
                                    Icon(
                                        imageVector = if (passwordVisible)
                                            Icons.Outlined.Visibility
                                        else
                                            Icons.Outlined.VisibilityOff,
                                        contentDescription = null,
                                        tint = TextHint,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        )
                    }

                    AnimatedVisibility(
                        visible = loginState is UiState.Error,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    ErrorRed.copy(alpha = 0.08f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(10.dp)
                        ) {
                            Icon(
                                Icons.Filled.ErrorOutline,
                                contentDescription = null,
                                tint = ErrorRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = (loginState as? UiState.Error)?.message ?: "",
                                fontSize = 12.sp,
                                color = ErrorRed
                            )
                        }
                    }

                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.login(email, password)
                        },
                        enabled = email.isNotBlank() &&
                                password.isNotBlank() &&
                                loginState !is UiState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue,
                            disabledContainerColor = PrimaryBlue.copy(alpha = 0.4f)

                        )
                    ) {
                        AnimatedContent(
                            targetState = loginState is UiState.Loading,
                            label = "login_button"
                        ) { isLoading ->
                            if (isLoading) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Memproses...",
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            } else {
                                Text(
                                    text = "Login",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }


            Spacer(modifier = Modifier.height(24.dp))


            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Belum punya akun? ",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
                Text(
                    text = "Klik Bantuan",
                    fontSize = 13.sp,
                    color = PrimaryBlue,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { /* open bantuan */ }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
@Composable
fun AppHeader() {
    Row (
        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp , horizontal = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            shape = RoundedCornerShape(5.dp),
            color = PrimaryBlue,
            modifier = Modifier.size(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.AccountBox,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Text(
            text = "Register Offline",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = PrimaryBlue
        )
    }
}

@Composable
fun FieldLabel(text: String, required: Boolean = false) {
    Row {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        if (required) {
            Text(
                text = " *",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = ErrorRed
            )
        }
    }
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 13.sp,
                color = TextHint
            )
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        isError = isError,
        enabled = enabled,
        readOnly = readOnly,
        singleLine = singleLine,
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
        supportingText = if (isError && errorMessage != null) {
            { Text(text = errorMessage, fontSize = 11.sp, color = ErrorRed) }
        } else null
    )
}