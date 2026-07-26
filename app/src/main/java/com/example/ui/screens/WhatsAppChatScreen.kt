package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ChatGroupEntity
import com.example.data.ChatMessageEntity
import com.example.ui.SmartPlusViewModel
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGold
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateCardSurface
import com.example.ui.theme.SlateDarkBackground
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun WhatsAppChatScreen(
    viewModel: SmartPlusViewModel,
    chatGroups: List<ChatGroupEntity>,
    activeGroup: ChatGroupEntity?,
    activeMessages: List<ChatMessageEntity>
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Tous, 1: Groupes, 2: Chaînes
    var showCreateModal by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    if (activeGroup != null) {
        // Open Active Conversation View
        WhatsAppConversationView(
            group = activeGroup,
            messages = activeMessages,
            onBack = { viewModel.closeChatGroup() },
            onSendMessage = { text -> viewModel.sendChatMessage(text) }
        )
    } else {
        // Chat Group & Channel List Screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SlateDarkBackground)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "WhatsApp Esports",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = CyberGold
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CyberCyan.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "LIVE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CyberCyan,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Row {
                        IconButton(onClick = { showCreateModal = true }, modifier = Modifier.testTag("create_chat_button")) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Nouveau Groupe", tint = CyberGold)
                        }
                    }
                }

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Rechercher un groupe, une chaîne...", color = TextMuted) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = TextMuted) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = SlateBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = SlateCardSurface,
                        unfocusedContainerColor = SlateCardSurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("chat_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Top Category Tabs
                val tabs = listOf("Tous", "Groupes de discussion", "Chaînes")
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = SlateDarkBackground,
                    contentColor = CyberGold,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = CyberGold
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selectedTab == index) CyberGold else TextMuted
                                    )
                                )
                            },
                            modifier = Modifier.testTag("chat_tab_$index")
                        )
                    }
                }

                // Filtered List
                val filteredGroups = chatGroups.filter { group ->
                    val matchesQuery = group.name.contains(searchQuery, ignoreCase = true) ||
                            group.gameType.contains(searchQuery, ignoreCase = true)
                    val matchesTab = when (selectedTab) {
                        1 -> group.type == "GROUP"
                        2 -> group.type == "CHANNEL"
                        else -> true
                    }
                    matchesQuery && matchesTab
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(filteredGroups) { group ->
                        ChatItemRow(
                            group = group,
                            onClick = { viewModel.openChatGroup(group) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // Floating Action Button
            FloatingActionButton(
                onClick = { showCreateModal = true },
                containerColor = CyberGold,
                contentColor = Color.Black,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .testTag("fab_create_group_channel")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Nouveau")
            }
        }
    }

    if (showCreateModal) {
        CreateGroupOrChannelBottomSheet(
            onDismiss = { showCreateModal = false },
            onCreate = { name, type, gameType, desc ->
                viewModel.createGroupOrChannel(name, type, gameType, desc)
                showCreateModal = false
            }
        )
    }
}

@Composable
fun ChatItemRow(
    group: ChatGroupEntity,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SlateCardSurface),
        shape = RoundedCornerShape(14.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                listOf(SlateBorder, if (group.type == "CHANNEL") CyberCyan.copy(alpha = 0.4f) else CyberGold.copy(alpha = 0.3f))
            )
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("chat_item_${group.id}")
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Group Avatar Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (group.type == "CHANNEL") CyberCyan.copy(alpha = 0.2f) else CyberGold.copy(alpha = 0.2f))
                    .border(1.dp, if (group.type == "CHANNEL") CyberCyan else CyberGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (group.type == "CHANNEL") Icons.Default.RssFeed else Icons.Default.Group,
                    contentDescription = group.name,
                    tint = if (group.type == "CHANNEL") CyberCyan else CyberGold,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = group.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            ),
                            maxLines = 1
                        )
                        if (group.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = CyberCyan,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Text(
                        text = group.lastMessageTime,
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = group.lastMessage,
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )

                    if (group.unreadCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(CyberGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${group.unreadCount}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(SlateDarkBackground)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (group.type == "CHANNEL") "Chaîne" else "Groupe",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextMuted,
                                    fontSize = 9.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WhatsAppConversationView(
    group: ChatGroupEntity,
    messages: List<ChatMessageEntity>,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateDarkBackground)
    ) {
        // Top WhatsApp Custom Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SlateCardSurface)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("back_from_chat_button")) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (group.type == "CHANNEL") CyberCyan.copy(alpha = 0.2f) else CyberGold.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (group.type == "CHANNEL") Icons.Default.RssFeed else Icons.Default.Group,
                    contentDescription = group.name,
                    tint = if (group.type == "CHANNEL") CyberCyan else CyberGold,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = group.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        ),
                        maxLines = 1
                    )
                    if (group.isVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = CyberCyan,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Text(
                    text = if (group.type == "CHANNEL") "${group.membersCount} abonnés • Chaîne Officielle" else "${group.membersCount} membres • en ligne",
                    style = MaterialTheme.typography.labelSmall.copy(color = CyberCyan)
                )
            }

            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Default.Phone, contentDescription = "Appel", tint = TextSecondary, modifier = Modifier.size(20.dp))
            }
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Infos", tint = TextSecondary, modifier = Modifier.size(20.dp))
            }
        }

        // WhatsApp Chat Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                // Top Info Badge
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (group.type == "CHANNEL") "🔒 Vous suivez la chaîne officielle. Seuls les admins peuvent publier." else "🔒 Les messages sont chiffrés pour votre squad.",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
                        modifier = Modifier
                            .background(SlateCardSurface, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(messages) { msg ->
                ChatMessageBubble(msg = msg, isChannel = group.type == "CHANNEL")
                Spacer(modifier = Modifier.height(6.dp))
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Bottom WhatsApp Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SlateCardSurface)
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Default.AttachFile, contentDescription = "Attach", tint = TextMuted)
            }

            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Tapez un message...", color = TextMuted) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    if (inputText.isNotBlank()) {
                        onSendMessage(inputText)
                        inputText = ""
                    }
                }),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = SlateBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = SlateDarkBackground,
                    unfocusedContainerColor = SlateDarkBackground
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_message_field")
            )

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        onSendMessage(inputText)
                        inputText = ""
                    }
                },
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(CyberGold)
                    .testTag("send_chat_message_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun ChatMessageBubble(msg: ChatMessageEntity, isChannel: Boolean) {
    val isMyMsg = msg.isMe

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMyMsg) Alignment.End else Alignment.Start
    ) {
        if (!isMyMsg && !isChannel) {
            Text(
                text = msg.senderName,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = CyberGold
                ),
                modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
            )
        }

        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = if (isMyMsg) 14.dp else 2.dp,
                        bottomEnd = if (isMyMsg) 2.dp else 14.dp
                    )
                )
                .background(if (isMyMsg) Color(0xFF134E4A) else SlateCardSurface)
                .border(
                    1.dp,
                    if (isMyMsg) CyberCyan.copy(alpha = 0.4f) else SlateBorder,
                    RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = if (isMyMsg) 14.dp else 2.dp,
                        bottomEnd = if (isMyMsg) 2.dp else 14.dp
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Column {
                Text(
                    text = msg.message,
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = msg.timestamp,
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp)
                    )

                    if (isMyMsg) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = "Read",
                            tint = CyberCyan,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupOrChannelBottomSheet(
    onDismiss: () -> Unit,
    onCreate: (name: String, type: String, gameType: String, desc: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var name by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("GROUP") } // "GROUP" or "CHANNEL"
    var selectedGame by remember { mutableStateOf("Free Fire") }
    var description by remember { mutableStateOf("") }

    val games = listOf("Free Fire", "PUBG Mobile", "COD Mobile", "Général")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SlateDarkBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Créer un Groupe ou une Chaîne",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Type Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (selectedType == "GROUP") SlateCardSurface else SlateDarkBackground, RoundedCornerShape(10.dp))
                        .border(1.dp, if (selectedType == "GROUP") CyberGold else SlateBorder, RoundedCornerShape(10.dp))
                        .clickable { selectedType = "GROUP" }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👥 Groupe de discussion simple",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (selectedType == "GROUP") CyberGold else TextSecondary
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (selectedType == "CHANNEL") SlateCardSurface else SlateDarkBackground, RoundedCornerShape(10.dp))
                        .border(1.dp, if (selectedType == "CHANNEL") CyberCyan else SlateBorder, RoundedCornerShape(10.dp))
                        .clickable { selectedType = "CHANNEL" }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📢 Chaîne (Actualités)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (selectedType == "CHANNEL") CyberCyan else TextSecondary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nom (${if (selectedType == "CHANNEL") "Chaîne" else "Groupe"})") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberGold,
                    unfocusedBorderColor = SlateBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier.fillMaxWidth().testTag("new_group_name_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Catégorie de Jeu :", style = MaterialTheme.typography.labelMedium.copy(color = TextMuted))
            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                games.forEach { g ->
                    val isSelected = selectedGame == g
                    Box(
                        modifier = Modifier
                            .background(if (isSelected) CyberCyan.copy(alpha = 0.2f) else SlateCardSurface, RoundedCornerShape(8.dp))
                            .border(1.dp, if (isSelected) CyberCyan else SlateBorder, RoundedCornerShape(8.dp))
                            .clickable { selectedGame = g }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = g,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isSelected) CyberCyan else TextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = SlateBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier.fillMaxWidth().testTag("new_group_desc_input")
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { onCreate(name, selectedType, selectedGame, description) },
                colors = ButtonDefaults.buttonColors(containerColor = CyberGold, contentColor = Color.Black),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("confirm_create_group_button")
            ) {
                Text(
                    text = "Créer sur Smart Plus",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
