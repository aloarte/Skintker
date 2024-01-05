package com.p4r4d0x.skintker.presenter.login.view.compose

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.presenter.login.viewmodel.LoginViewModel
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(viewModel: LoginViewModel, context: Context) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val loginError = stringResource(id = R.string.google_sso_error)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            viewModel.signWithCredential(credential)
        } catch (e: ApiException) {
            Firebase.crashlytics.recordException(e)
            Firebase.crashlytics.log("Google SSO failed. Status: ${e.status}, Code: ${e.statusCode}, Message: ${e.message}")
            scope.launch {
                snackbarHostState.showSnackbar(loginError)
            }
        }
    }

    Scaffold {
        Box(Modifier.padding(it)) {
            LoginScreenContent(launcher, context){
                viewModel.signAnonymous()
            }
            SnackbarHost(
                modifier = Modifier.align(Alignment.BottomCenter),
                hostState = snackbarHostState
            ) {
                Snackbar(
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.onSurface,
                    snackbarData = SkintkerSnackbarData(
                        duration = SnackbarDuration.Long,
                        message = loginError,
                        actionLabel = null
                    )
                )
            }
        }
    }
}

class SkintkerSnackbarData(
    override val actionLabel: String?,
    override val duration: SnackbarDuration,
    override val message: String
) : SnackbarData {
    override fun dismiss() {
    }

    override fun performAction() {
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LoginScreenContent(
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    context: Context,
    onAnonLogin: () -> Unit
) {
    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_logo_background),
                contentDescription = null,// decorative element
                modifier = Modifier
                    .size(300.dp)
                    .shadow(
                        elevation = 0.dp
                    ),
                tint = Color.Unspecified
            )
            Text(
                stringResource(id = R.string.login_description),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(vertical = 5.dp, horizontal = 30.dp)
            )

            Text(
                stringResource(id = R.string.login_anon_description),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(vertical = 5.dp, horizontal = 30.dp)
            )

            Divider(
                modifier = Modifier
                    .height(40.dp),
                color = Color.Transparent
            )

            GoogleSignInRow(launcher = launcher, context = context)
            GoogleSignInRowAnonymous(onAnonLogin)

        }
    }
}

@Composable
fun GoogleSignInRow(
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    context: Context
) {

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(stringResource(R.string.default_web_client_id)).requestEmail().build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            border = ButtonDefaults.outlinedBorder.copy(width = 1.dp),
            onClick = {
                launcher.launch(googleSignInClient.signInIntent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    tint = Color.Unspecified,
                    painter = painterResource(id = R.drawable.googleg_standard_color_18),
                    contentDescription = null
                )
                Text(
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.onSurface,
                    text = stringResource(id = R.string.btn_sign_in)
                )
                Icon(
                    tint = Color.Transparent, imageVector = Icons.Default.MailOutline,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun GoogleSignInRowAnonymous(onAnonLogin: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            border = ButtonDefaults.outlinedBorder.copy(width = 1.dp),
            onClick = {
                onAnonLogin.invoke()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.onSurface,
                    text = stringResource(id = R.string.btn_sign_in_anon)
                )

            }
        }
    }
}
