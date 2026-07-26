package com.example.data

import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SmartPlusRepository(private val dao: SmartPlusDao) {

    val userProfile: Flow<UserProfileEntity?> = dao.getUserProfile()
    val tournaments: Flow<List<TournamentEntity>> = dao.getAllTournaments()
    val posts: Flow<List<PostEntity>> = dao.getAllPosts()
    val communities: Flow<List<CommunityEntity>> = dao.getAllCommunities()
    val payments: Flow<List<PaymentTransactionEntity>> = dao.getAllPayments()
    val chatGroups: Flow<List<ChatGroupEntity>> = dao.getAllChatGroups()

    fun getMessagesForGroup(groupId: String): Flow<List<ChatMessageEntity>> {
        return dao.getMessagesForGroup(groupId)
    }

    suspend fun seedInitialDataIfNeeded() {
        // Seed default profile if empty
        val defaultProfile = UserProfileEntity(
            id = 1,
            userId = "user_smart_772",
            username = "Alex Viper",
            photoUrl = "",
            bio = "Pro Gamer Free Fire & PUBG | Champion Cup 2026 🏆",
            gameId = "VIPER_FF_99",
            followersCount = 1840,
            followingCount = 210,
            walletBalanceFcfa = 25000,
            isVerified = true,
            rankTitle = "Grand Maître"
        )
        dao.insertOrUpdateProfile(defaultProfile)

        // Seed Tournaments
        val initialTournaments = listOf(
            TournamentEntity(
                id = "tourn_ff_01",
                title = "Tournoi Free Fire Elite Cup",
                gameCategory = "Free Fire",
                priceFcfa = 1000,
                playersCount = 42,
                maxPlayers = 48,
                prizePoolFcfa = 50000,
                startTime = "Aujourd'hui à 20:00",
                status = "EN COURS",
                bannerType = "FREE_FIRE"
            ),
            TournamentEntity(
                id = "tourn_pubg_02",
                title = "Tournoi PUBG Mobile Master",
                gameCategory = "PUBG Mobile",
                priceFcfa = 500,
                playersCount = 88,
                maxPlayers = 100,
                prizePoolFcfa = 75000,
                startTime = "Demain à 18:30",
                status = "A VENIR",
                bannerType = "PUBG"
            ),
            TournamentEntity(
                id = "tourn_cod_03",
                title = "COD Mobile Call of Champions",
                gameCategory = "COD Mobile",
                priceFcfa = 2000,
                playersCount = 16,
                maxPlayers = 32,
                prizePoolFcfa = 100000,
                startTime = "Samedi à 21:00",
                status = "A VENIR",
                bannerType = "HERO"
            ),
            TournamentEntity(
                id = "tourn_mlbb_04",
                title = "Mobile Legends Clash of Kings",
                gameCategory = "Mobile Legends",
                priceFcfa = 1500,
                playersCount = 10,
                maxPlayers = 20,
                prizePoolFcfa = 60000,
                startTime = "Dimanche à 16:00",
                status = "A VENIR",
                bannerType = "HERO"
            )
        )
        dao.insertTournaments(initialTournaments)

        // Seed Posts
        val initialPosts = listOf(
            PostEntity(
                id = "post_101",
                authorName = "Jordan 'Sniper' K.",
                authorTag = "@jordan_pro",
                content = "Victoire écrasante sur la dernière partie de Free Fire ! Squad au sommet 💥 18 Kills ! Qui est chaud pour le tournoi de ce soir ?",
                gameTag = "#FreeFire",
                timestamp = "Il y a 15 min",
                likesCount = 84,
                commentsCount = 12,
                mediaType = "IMAGE"
            ),
            PostEntity(
                id = "post_102",
                authorName = "Smart Plus Official",
                authorTag = "@smartplus",
                content = "🎉 Le grand tournoi PUBG Mobile de la semaine est ouvert ! Inscription à seulement 500 FCFA avec un Cashprize de 75 000 FCFA. Rejoignez vite !",
                gameTag = "#Esports #PUBGMobile",
                timestamp = "Il y a 1h",
                likesCount = 245,
                commentsCount = 38,
                mediaType = "NONE"
            ),
            PostEntity(
                id = "post_103",
                authorName = "Sarra Gaming",
                authorTag = "@sarra_gm",
                content = "On recrute 2 joueurs sérieux pour notre squad COD Mobile. Rank Legende exigé. Envoyez votre ID en DM !",
                gameTag = "#CODMobile",
                timestamp = "Il y a 3h",
                likesCount = 56,
                commentsCount = 19,
                mediaType = "NONE"
            )
        )
        dao.insertPosts(initialPosts)

        // Seed Communities
        val initialCommunities = listOf(
            CommunityEntity(
                id = "comm_1",
                name = "Free Fire Warriors Africa",
                gameType = "Free Fire",
                membersCount = 12400,
                description = "Communaute officielle des joueurs Free Fire. Partage de clips, recrutement et scrims daily.",
                isJoined = true
            ),
            CommunityEntity(
                id = "comm_2",
                name = "PUBG Squad Pro League",
                gameType = "PUBG Mobile",
                membersCount = 8900,
                description = "La plus grande ligue PUBG Mobile. Tournois hebdomadaires et classements.",
                isJoined = false
            ),
            CommunityEntity(
                id = "comm_3",
                name = "COD Mobile Gladiators",
                gameType = "COD Mobile",
                membersCount = 6100,
                description = "Joueurs competitifs COD Mobile Search & Destroy et Battle Royale.",
                isJoined = false
            )
        )
        dao.insertCommunities(initialCommunities)

        // Seed initial transaction history
        val initialPayments = listOf(
            PaymentTransactionEntity(
                id = "pay_901",
                method = "Orange Money",
                amountFcfa = 1000,
                title = "Participation Tournoi Free Fire",
                date = "26 Juil 2026 - 12:40",
                status = "SUCCESS"
            ),
            PaymentTransactionEntity(
                id = "pay_902",
                method = "Mobile Money",
                amountFcfa = 5000,
                title = "Rechargement Portefeuille",
                date = "25 Juil 2026 - 18:15",
                status = "SUCCESS"
            )
        )
        initialPayments.forEach { dao.insertPayment(it) }

        // Seed WhatsApp Chat Groups & Channels
        val initialChatGroups = listOf(
            ChatGroupEntity(
                id = "group_ff_squad",
                name = "🔥 Squad Free Fire Elite",
                type = "GROUP",
                gameType = "Free Fire",
                membersCount = 4,
                description = "Groupe de discussion pour la squad de tournoi Free Fire. Stratégies et rendez-vous.",
                lastMessage = "Jordan: Je suis connecté pour le scrim de 20h !",
                lastMessageTime = "14:25",
                unreadCount = 2,
                iconType = "FIRE"
            ),
            ChatGroupEntity(
                id = "channel_smartplus_official",
                name = "Smart Plus News & Codes 📢",
                type = "CHANNEL",
                gameType = "Général",
                membersCount = 18400,
                description = "Chaîne officielle Smart Plus. Annonces de cashprizes, giveaways et codes de réduction.",
                lastMessage = "🎁 Code promo valide aujourd'hui: SMARTFREE50 pour 50% sur votre 1ère inscription !",
                lastMessageTime = "12:10",
                unreadCount = 1,
                isVerified = true,
                iconType = "CHANNEL"
            ),
            ChatGroupEntity(
                id = "group_pubg_clash",
                name = "PUBG Master Squad Central",
                type = "GROUP",
                gameType = "PUBG Mobile",
                membersCount = 6,
                description = "Discussion entre joueurs PUBG Mobile. Recherche de coéquipiers pour les tournois.",
                lastMessage = "Alex Viper: Qui gère les tirs de précision au sniper ?",
                lastMessageTime = "Hier",
                unreadCount = 0,
                iconType = "PUBG"
            ),
            ChatGroupEntity(
                id = "channel_esports_africa",
                name = "Chaîne Esport Afrique 🏆",
                type = "CHANNEL",
                gameType = "Général",
                membersCount = 12500,
                description = "Actualités de la scène compétitive mobile en Afrique. Classements et streams live.",
                lastMessage = "🔴 Direct du tournoi Free Fire Cup en cours ! Cliquez pour suivre la finale.",
                lastMessageTime = "22/07",
                unreadCount = 0,
                isVerified = true,
                iconType = "CHANNEL"
            )
        )
        dao.insertChatGroups(initialChatGroups)

        // Seed initial Chat Messages
        val initialMessages = listOf(
            ChatMessageEntity(
                id = "msg_101",
                groupId = "group_ff_squad",
                senderName = "Jordan 'Sniper'",
                senderTag = "@jordan_pro",
                message = "Salut l'équipe ! Prêts pour le tournoi Free Fire de 20h ?",
                timestamp = "14:15",
                isMe = false
            ),
            ChatMessageEntity(
                id = "msg_102",
                groupId = "group_ff_squad",
                senderName = "Sarra Gaming",
                senderTag = "@sarra_gm",
                message = "Yes ! J'ai rechargé mon compte sur Smart Plus, mon slot est déjà réservé 😎",
                timestamp = "14:18",
                isMe = false
            ),
            ChatMessageEntity(
                id = "msg_103",
                groupId = "group_ff_squad",
                senderName = "Alex Viper",
                senderTag = "@alex_viper",
                message = "Parfait ! On applique la tactique B sur la zone Purgatoire 🔥",
                timestamp = "14:22",
                isMe = true
            ),
            ChatMessageEntity(
                id = "msg_104",
                groupId = "group_ff_squad",
                senderName = "Jordan 'Sniper'",
                senderTag = "@jordan_pro",
                message = "Je suis connecté pour le scrim de 20h !",
                timestamp = "14:25",
                isMe = false
            ),
            // Channel Official
            ChatMessageEntity(
                id = "msg_201",
                groupId = "channel_smartplus_official",
                senderName = "Smart Plus Admin",
                senderTag = "@admin",
                message = "📢 NOUVEAU TOURNOI DISPONIBLE ! Cashprize total de 75 000 FCFA sur PUBG Mobile.",
                timestamp = "10:00",
                isMe = false
            ),
            ChatMessageEntity(
                id = "msg_202",
                groupId = "channel_smartplus_official",
                senderName = "Smart Plus Admin",
                senderTag = "@admin",
                message = "🎁 Code promo valide aujourd'hui: SMARTFREE50 pour 50% sur votre 1ère inscription !",
                timestamp = "12:10",
                isMe = false
            )
        )
        dao.insertChatMessages(initialMessages)
    }

    suspend fun sendMessage(groupId: String, text: String, senderName: String) {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeNow = sdf.format(Date())

        val newMsg = ChatMessageEntity(
            id = "msg_" + System.currentTimeMillis(),
            groupId = groupId,
            senderName = senderName,
            senderTag = "@" + senderName.lowercase().replace(" ", "_"),
            message = text,
            timestamp = timeNow,
            isMe = true
        )
        dao.insertChatMessage(newMsg)
        dao.updateGroupLastMessage(groupId, "$senderName: $text", timeNow)
    }

    suspend fun createNewGroupOrChannel(
        name: String,
        type: String, // "GROUP" or "CHANNEL"
        gameType: String,
        description: String
    ) {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeNow = sdf.format(Date())

        val id = (if (type == "CHANNEL") "channel_" else "group_") + System.currentTimeMillis()
        val newGroup = ChatGroupEntity(
            id = id,
            name = name,
            type = type,
            gameType = gameType,
            membersCount = 1,
            description = description,
            lastMessage = "Nouveau ${if (type == "CHANNEL") "chaîne" else "groupe"} créé !",
            lastMessageTime = timeNow,
            unreadCount = 0,
            iconType = if (type == "CHANNEL") "CHANNEL" else "SQUAD"
        )
        dao.insertChatGroup(newGroup)

        // Insert first system message
        val welcomeMsg = ChatMessageEntity(
            id = "msg_init_" + System.currentTimeMillis(),
            groupId = id,
            senderName = "Système",
            senderTag = "@system",
            message = "Bienvenue dans ${if (type == "CHANNEL") "la chaîne" else "le groupe"} '$name' !",
            timestamp = timeNow,
            isMe = true
        )
        dao.insertChatMessage(welcomeMsg)
    }

    suspend fun updateProfile(username: String, bio: String, gameId: String) {
        dao.getUserProfile()
        val current = UserProfileEntity(
            id = 1,
            username = username,
            bio = bio,
            gameId = gameId
        )
        dao.insertOrUpdateProfile(current)
    }

    suspend fun processTournamentPayment(
        tournamentId: String,
        tournamentTitle: String,
        amountFcfa: Int,
        paymentMethod: String
    ): Boolean {
        val dateFormat = SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        // Create transaction
        val transaction = PaymentTransactionEntity(
            id = "pay_" + System.currentTimeMillis(),
            method = paymentMethod,
            amountFcfa = amountFcfa,
            title = "Participation: $tournamentTitle",
            date = currentDate,
            status = "SUCCESS"
        )
        dao.insertPayment(transaction)
        dao.registerForTournament(tournamentId)
        dao.deductWalletBalance(amountFcfa)
        return true
    }

    suspend fun addWalletFunds(amountFcfa: Int, paymentMethod: String) {
        val dateFormat = SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        val transaction = PaymentTransactionEntity(
            id = "topup_" + System.currentTimeMillis(),
            method = paymentMethod,
            amountFcfa = amountFcfa,
            title = "Rechargement Portefeuille",
            date = currentDate,
            status = "SUCCESS"
        )
        dao.insertPayment(transaction)
        dao.addWalletBalance(amountFcfa)
    }

    suspend fun toggleLike(postId: String) {
        dao.toggleLikePost(postId)
    }

    suspend fun toggleJoinCommunity(communityId: String) {
        dao.toggleJoinCommunity(communityId)
    }

    suspend fun createPost(content: String, gameTag: String) {
        val newPost = PostEntity(
            id = "post_" + System.currentTimeMillis(),
            authorName = "Alex Viper",
            authorTag = "@alex_viper",
            content = content,
            gameTag = if (gameTag.isBlank()) "#Gaming" else if (!gameTag.startsWith("#")) "#$gameTag" else gameTag,
            timestamp = "A l'instant",
            likesCount = 1,
            commentsCount = 0,
            isLiked = true,
            mediaType = "NONE"
        )
        dao.insertPost(newPost)
    }
}
