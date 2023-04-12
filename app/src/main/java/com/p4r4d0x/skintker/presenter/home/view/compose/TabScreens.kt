package com.p4r4d0x.skintker.presenter.home.view.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.presenter.home.view.Screen
import com.p4r4d0x.skintker.presenter.home.viewmodel.HomeViewModel
import com.p4r4d0x.skintker.presenter.main.FragmentScreen
import java.util.*

@Composable
fun TabScreen(
    viewModel: HomeViewModel,
    navPressed: (FragmentScreen) -> Unit,
    removeLog: (Date) -> Unit
) {
    val fabShape = RoundedCornerShape(50)
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            HomeTopBar(navPressed)
        },
        floatingActionButton = {
            FloatingActionButtonHome(fabShape, navPressed)
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            HomeBottomBar(navController, fabShape)
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.History.route,
            Modifier.padding(innerPadding)
        ) {

            composable(Screen.History.route) {
                HistoryScreen(viewModel, removeLog)
            }
            composable(Screen.Resume.route) {
                ResumeScreen(viewModel)
            }
        }
    }
}

@Composable
fun ResumeScreen(viewModel: HomeViewModel) {
    viewModel.possibleCauses.observeAsState().value?.let { causes ->
        ResumeBody(causes = causes)
    } ?: run {
        StandaloneEmptyUserStats()
    }
}

@Composable
fun HistoryScreen(viewModel: HomeViewModel, removeLog: (Date) -> Unit) {
    viewModel.logList.observeAsState().value?.let { logs ->
        if (logs.isEmpty()) {
            EmptyHistoryContents()
        } else {
            HistoryContents(logs, removeLog)
        }
    }
}

@Composable
fun HomeTopBar(navigationPressed: (FragmentScreen) -> Unit) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primaryVariant,
        elevation = 0.dp
    ) {

        //TopAppBar Content
        Box(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(top = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.align(Alignment.TopCenter),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    style = MaterialTheme.typography.h6,
                    text = stringResource(id = R.string.app_name)
                )
            }
            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = {
                    navigationPressed(FragmentScreen.Settings)
                },
                enabled = true,
            ) {
                Icon(
                    Icons.Rounded.Settings,
                    contentDescription = "Back",
                )
            }
        }
    }
}

@Composable
fun HomeBottomBar(navController: NavHostController, fabShape: Shape) {
    val items = listOf(
        Screen.History,
        Screen.Resume
    )
    BottomAppBar(
        cutoutShape = fabShape,
        content = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                screen.icon,
                                contentDescription = null
                            )
                        },
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
        })
}

@Composable
fun FloatingActionButtonHome(fabShape: Shape, navigationPressed: (FragmentScreen) -> Unit) {
    FloatingActionButton(
        onClick = {
            navigationPressed(FragmentScreen.Survey)
        },
        shape = fabShape,
        backgroundColor = MaterialTheme.colors.primaryVariant
    ) {
        Icon(Icons.Outlined.Edit, "")
    }
}

