package com.p4r4d0x.skintker.presenter.welcome.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.p4r4d0x.skintker.presenter.FragmentScreen
import com.p4r4d0x.skintker.presenter.navigate
import com.p4r4d0x.skintker.presenter.welcome.viewmodel.WelcomeViewModel
import com.p4r4d0x.skintker.theme.SkintkerTheme
import org.koin.android.ext.android.inject

class WelcomeFragment : Fragment() {

    private val viewModel: WelcomeViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.navigateTo.observe(viewLifecycleOwner) { navigateToEvent ->
            navigateToEvent.getContentIfNotHandled()?.let { navigateTo ->
                navigate(navigateTo, FragmentScreen.Welcome)
            }
        }

        return ComposeView(requireContext()).apply {
            setContent {
                SkintkerTheme {
                    viewModel.handleContinue(true)
                }
            }
        }
    }
}
