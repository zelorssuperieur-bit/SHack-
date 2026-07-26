package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AuthState
import com.example.ui.SmartPlusViewModel
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CommunityScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.TopUpBottomSheet
import com.example.ui.screens.TournamentPaymentBottomSheet
import com.example.ui.screens.TournamentsScreen
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGold
import com.example.ui.theme.SmartPlusTheme
import com.example.ui.theme.SlateCardSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

enum class NavTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
) {
    HOME("Accueil", Icons.Filled.Home, Icons.Outlined.Home, "nav_tab_home"),
    TOURNAMENTS("Tournois", Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents, "nav_tab_tournaments"),
    COMMUNITY("Communauté", Icons.Filled.People, Icons.Outlined.People, "nav_tab_community"),
    PROFILE("Profil", Icons.Filled.Person, Icons.Outlined.Person, "nav_tab_profile")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartPlusTheme {
                SmartPlusApp()
            }
        }
    }
}

@Composable
fun SmartPlusApp(viewModel: SmartPlusViewModel = viewModel()) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val phoneNumber by viewModel.phoneNumber.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val tournaments by viewModel.tournaments.collectAsStateWithLifecycle()
    val posts by viewModel.posts.collectAsStateWithLifecycle()
    val communities by viewModel.communities.collectAsStateWithLifecycle()
    val payments by viewModel.payments.collectAsStateWithLifecycle()
    val chatGroups by viewModel.chatGroups.collectAsStateWithLifecycle()
    val activeGroup by viewModel.activeGroup.collectAsStateWithLifecycle()
    val activeMessages by viewModel.activeMessages.collectAsStateWithLifecycle()
    val selectedGameFilter by viewModel.selectedGameFilter.collectAsStateWithLifecycle()
    val paymentModalTournament by viewModel.paymentModalTournament.collectAsStateWithLifecycle()
    val showTopUpModal by viewModel.showTopUpModal.collectAsStateWithLifecycle()
    val snackMessage by viewModel.snackMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackMessage) {
        snackMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnack()
        }
    }

    if (authState != AuthState.LOGGED_IN) {
        AuthScreen(
            viewModel = viewModel,
            authState = authState,
            phoneNumber = phoneNumber
        )
    } else {
        var selectedTab by remember { mutableIntStateOf(0) }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                NavigationBar(
                    containerColor = SlateCardSurface,
                    contentColor = CyberGold,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("bottom_navigation_bar")
                ) {
                    NavTab.entries.forEachIndexed { index, tab ->
                        val isSelected = selectedTab == index
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedTab = index },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.title,
                                    tint = if (isSelected) CyberGold else TextMuted
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) CyberGold else TextMuted
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = CyberCyan.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier.testTag(tab.tag)
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    0 -> HomeScreen(
                        viewModel = viewModel,
                        userProfile = userProfile,
                        tournaments = tournaments,
                        posts = posts,
                        onNavigateToTournaments = { selectedTab = 1 },
                        onNavigateToCommunity = { selectedTab = 2 },
                        onNavigateToProfile = { selectedTab = 3 }
                    )
                    1 -> TournamentsScreen(
                        viewModel = viewModel,
                        tournaments = tournaments,
                        selectedFilter = selectedGameFilter,
                        onSelectFilter = { viewModel.setGameFilter(it) }
                    )
                    2 -> CommunityScreen(
                        viewModel = viewModel,
                        posts = posts,
                        communities = communities,
                        chatGroups = chatGroups,
                        activeGroup = activeGroup,
                        activeMessages = activeMessages
                    )
                    3 -> ProfileScreen(
                        viewModel = viewModel,
                        userProfile = userProfile,
                        payments = payments
                    )
                }
            }
        }

        // Modals
        paymentModalTournament?.let { tournament ->
            TournamentPaymentBottomSheet(
                tournament = tournament,
                userProfile = userProfile,
                onDismiss = { viewModel.closePaymentModal() },
                onConfirmPayment = { method ->
                    viewModel.processTournamentPayment(method)
                }
            )
        }

        if (showTopUpModal) {
            TopUpBottomSheet(
                onDismiss = { viewModel.closeTopUpModal() },
                onConfirmTopUp = { amount, method ->
                    viewModel.processTopUp(amount, method)
                }
            )
        }
    }
}
