package com.p4r4d0x.skintker.presenter.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.p4r4d0x.skintker.presenter.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeViewModel()

        setContent {
            MyScreen()
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) {
            when (it) {
                is SurveyState.LogQuestions -> {
                    it.state.forEach { logState ->
                        Log.d("ALRALR", "${logState.answer}")
                    }

                }
                is SurveyState.Result -> {

                }

            }
        }

    }

    @Preview
    @Composable
    private fun MyScreen() {
        val items = listOf(
            Screen.Log,
            Screen.History,
            Screen.Resume
        )

        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                BottomNavigation {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { screen ->
                        BottomNavigationItem(
                            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                            label = { Text(stringResource(screen.resourceId)) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController,
                startDestination = Screen.Log.route,
                Modifier.padding(innerPadding)
            ) {
                composable(Screen.Log.route) { LogScreen(viewModel) }
                composable(Screen.Resume.route) { ResumeScreen() }
                composable(Screen.History.route) { HistoryScreen(viewModel) }
            }
        }
    }
}