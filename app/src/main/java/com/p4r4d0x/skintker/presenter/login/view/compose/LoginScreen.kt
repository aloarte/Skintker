package com.p4r4d0x.skintker.presenter.login.view.compose

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.presenter.login.viewmodel.LoginViewModel


@Composable
fun LoginScreen(viewModel: LoginViewModel, context: Context) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)

            Log.e("ALRALR", "rememberLauncherForActivityResult:  $account  $credential")

            viewModel.signWithCredential(credential)
        } catch (e: ApiException) {
            Log.e("ALRALR", "Google sign in failed  $e")
        }
    }

    Scaffold {
        LoginScreenContent(viewModel, launcher, context)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LoginScreenContent(
    viewModel: LoginViewModel,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    context: Context
) {

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(stringResource(R.string.default_web_client_id)).requestEmail().build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
//    val state = viewModel.loadingState.collectAsState()?.value ?: ""
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =/* if (state.value == LOADING) Arrangement.Center else*/ Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
//                if (state.value == LOADING) {
//                    CircularProgressIndicator()
//                } else {
                Icon(
                    tint = Color.Unspecified,
                    painter = painterResource(id = R.drawable.googleg_standard_color_18),
                    contentDescription = null
                )
                Text(
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.onSurface,
                    text = "Sign in with Google"
                )
                Icon(
                    tint = Color.Transparent, imageVector = Icons.Default.MailOutline,
                    contentDescription = null
                )
//                }
            }
//            when (state.value.status) {
//                LoginLoadingState.LoginStatus.Success -> {
//                    Text("Success")
//                }
//                LoginLoadingState.LoginStatus.Failed -> {
//
//                    Text(state.value.message ?: "Error")
//
//                }
//                LoginLoadingState.LoginStatus.LoggedIn -> {
//                    Text("Already logged In")
//
//                }
//                else -> {
//
//                }
        }
    }
}

