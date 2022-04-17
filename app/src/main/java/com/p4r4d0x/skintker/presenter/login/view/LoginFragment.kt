package com.p4r4d0x.skintker.presenter.login.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.presenter.login.LoginLoadingState
import com.p4r4d0x.skintker.presenter.login.view.compose.LoginScreen
import com.p4r4d0x.skintker.presenter.login.viewmodel.LoginViewModel
import com.p4r4d0x.skintker.presenter.main.FragmentScreen
import com.p4r4d0x.skintker.presenter.main.navigate
import com.p4r4d0x.skintker.theme.SkintkerTheme
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by inject()

    override fun onResume() {
        super.onResume()
        observeViewModel()
    }

    private fun observeViewModel() {
//        viewModel.loadingState.observe(viewLifecycleOwner) { loadingState ->
//            if (loadingState == LoginLoadingState.LOADED) {
//                navigate(FragmentScreen.Welcome, FragmentScreen.Login)
//            }
//        }
        navigate(FragmentScreen.Welcome, FragmentScreen.Login)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            id = R.id.settings_fragment
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setContent {
                SkintkerTheme {
                    LoginScreen(viewModel, requireContext())
                }
            }
        }
    }
}