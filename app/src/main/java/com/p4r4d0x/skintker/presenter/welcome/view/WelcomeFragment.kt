package com.p4r4d0x.skintker.presenter.welcome.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.presenter.FragmentScreen
import com.p4r4d0x.skintker.presenter.navigate
import com.p4r4d0x.skintker.presenter.welcome.viewmodel.WelcomeViewModel
import com.p4r4d0x.skintker.theme.SkintkerTheme
import org.koin.android.ext.android.inject

class WelcomeFragment : Fragment() {

    private val viewModel: WelcomeViewModel by inject()

    override fun onResume() {
        super.onResume()
        observeViewModel()
        viewModel.checkLogReportedToday()
    }

    private fun observeViewModel() {
        viewModel.navigateTo.observe(viewLifecycleOwner) { navigateToEvent ->
            navigateToEvent.getContentIfNotHandled()?.let { navigateTo ->
                navigate(navigateTo, FragmentScreen.Welcome)
            }
        }
        viewModel.logReported.observe(viewLifecycleOwner) { logAlreadyReported ->
            viewModel.handleContinue(logAlreadyReported)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                SkintkerTheme {
                    Surface(
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = CenterHorizontally
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
                        }


                    }

                }
            }
        }
    }
}
