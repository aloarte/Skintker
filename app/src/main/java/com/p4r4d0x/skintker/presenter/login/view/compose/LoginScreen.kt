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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    .size(400.dp)
                    .shadow(
                        elevation = 0.dp
                    ),
                tint = Color.Unspecified
            )
            Text(
                stringResource(id = R.string.login_description),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(vertical = 0.dp, horizontal = 30.dp)
            )

            GoogleSignInRow(launcher = launcher, context = context)
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
                    text = "Sign in with Google"
                )
                Icon(
                    tint = Color.Transparent, imageVector = Icons.Default.MailOutline,
                    contentDescription = null
                )
            }
        }
    }
}

